package com.jnu.capstone.service;

import com.jnu.capstone.dto.AiResponseDto;
import com.jnu.capstone.dto.SecondhandBoardCreateRequestDto;
import com.jnu.capstone.dto.SecondhandBoardDto;
import com.jnu.capstone.entity.*;
import com.jnu.capstone.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional; // ✅ 추가
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class SecondhandBoardService {

    @Autowired
    private SecondhandBoardRepository secondhandBoardRepository;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private SchoolRepository schoolRepository;

    @Autowired
    private AiRecommendService aiRecommendService;
    @Autowired
    private NotificationService notificationService;
    @Autowired
    private PostKeywordRepository postKeywordRepository;

    @Transactional
    public int createSecondhandBoard(SecondhandBoardCreateRequestDto dto, int userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));
        School campus = user.getCampus();

        Post post = new Post();
        post.setTitle(dto.getTitle());
        post.setContents(dto.getContents());
        post.setBoardType(BoardType.SECONDHAND);
        post.setUser(user);
        post.setCampus(campus);
        post = postRepository.save(post);

        SecondhandBoard board = new SecondhandBoard();
        board.setPost(post);
        board.setPlace(dto.getPlace());
        board.setWriteTime(new Date());
        board.setPhoto(dto.getPhoto());
        board.setPrice(dto.getPrice());

        secondhandBoardRepository.save(board);

        return post.getPostId();
    }

    public AiResponseDto sendToAiServer(int postId, MultipartFile image) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("postId에 해당하는 게시글이 없습니다."));

        // AI 서버 호출
        AiResponseDto aiResponse = aiRecommendService.sendToAiServer(post, image);

        // 키워드 저장
        for (String keyword : aiResponse.getKeywords()) {
            postKeywordRepository.save(PostKeyword.builder()
                    .postId(postId)
                    .keywordText(keyword)
                    .build());
        }

        // 알림 전송
        notificationService.notifyUsersByKeywords(
                aiResponse.getKeywords(),
                post.getBoardType(),
                postId
        );

        return aiResponse;
    }

    // 게시글 전체 목록 조회
    public List<SecondhandBoardDto> getAllBoards() {
        return secondhandBoardRepository.findAllWithPost()
                .stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }


    // 게시글 상세 조회
    public SecondhandBoardDto getBoardByPostId(int postId) {
        SecondhandBoard board = secondhandBoardRepository.findWithPostByPostId(postId)
                .orElseThrow(() -> new IllegalArgumentException("게시글을 찾을 수 없습니다."));
        return convertToDto(board);
    }

    private SecondhandBoardDto convertToDto(SecondhandBoard board) {
        SecondhandBoardDto dto = new SecondhandBoardDto();
        dto.setPostId(board.getPostId());
        dto.setPlace(board.getPlace());
        dto.setContents(board.getPost().getContents()); // ✅ Post로부터 상세 설명 가져오기
        dto.setWriteTime(board.getWriteTime());
        dto.setPhoto(board.getPhoto());
        dto.setPrice(board.getPrice());
        dto.setRelativeTime(getRelativeTime(board.getWriteTime()));
        dto.setNickname(board.getPost().getUser().getNickname()); // ✅ 닉네임 설정
        dto.setTitle(board.getPost().getTitle());
        dto.setUserId(board.getPost().getUser().getUserId());
        return dto;
    }

    private String getRelativeTime(Date writeTime) {
        long diffMillis = new Date().getTime() - writeTime.getTime();
        long minutes = diffMillis / (60 * 1000);
        if (minutes < 1) return "방금 전";
        if (minutes < 60) return minutes + "분 전";
        long hours = minutes / 60;
        if (hours < 24) return hours + "시간 전";
        long days = hours / 24;
        return days + "일 전";
    }

    // 새로 추가할 메서드
    public List<SecondhandBoardDto> getBoardsByUserCampus(int userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));
        int campusId = user.getCampus().getCampusId();

        return secondhandBoardRepository.findAllByCampusId(campusId).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public List<SecondhandBoardDto> searchBoardsByCampusAndQuery(int userId, String query) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));
        int campusId = user.getCampus().getCampusId();

        return secondhandBoardRepository.searchByCampusAndQuery(campusId, query).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public List<SecondhandBoardDto> getSecondhandBoardDetailsByPostIds(List<Integer> postIds) {
        return secondhandBoardRepository.findWithPostByPostIds(postIds).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }




}
