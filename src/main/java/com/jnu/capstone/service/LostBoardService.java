package com.jnu.capstone.service;

import com.jnu.capstone.dto.AiResponseDto;
import com.jnu.capstone.dto.LostBoardCreateRequestDto;
import com.jnu.capstone.dto.LostBoardDto;
import com.jnu.capstone.dto.LostItemMapResponseDto;
import com.jnu.capstone.entity.*;
import com.jnu.capstone.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class LostBoardService {

    @Autowired
    private LostBoardRepository lostBoardRepository;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AiRecommendService aiRecommendService;

    @Autowired
    private PostKeywordRepository postKeywordRepository;

    @Autowired
    private NotificationService notificationService;

    @Autowired
    private KeywordRepository keywordRepository;

    @Transactional
    public int createLostBoard(int userId, LostBoardCreateRequestDto dto) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));

        Post post = new Post();
        post.setUser(user);
        post.setCampus(user.getCampus());
        post.setTitle(dto.getTitle());
        post.setContents(dto.getContents());
        post.setBoardType(BoardType.LOST);
        post.setIsDeleted(false);

        post = postRepository.save(post);

        LostBoard lostBoard = new LostBoard();
        lostBoard.setPost(post);
        lostBoard.setPlace(dto.getPlace());
        lostBoard.setWriteTime(new Date());
        lostBoard.setPhoto(dto.getPhoto());
        lostBoard.setLost(dto.isLost());
        lostBoard.setLostLatitude(dto.getLostLatitude());
        lostBoard.setLostLongitude(dto.getLostLongitude());

        lostBoardRepository.save(lostBoard);

        post.setLostBoard(lostBoard);



        return post.getPostId(); // 👉 postId 반환

    }

    public AiResponseDto sendToAiServer(int postId, MultipartFile image) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("게시글을 찾을 수 없습니다."));

        // ✅ AI 서버 호출 + 키워드 저장 + 알림 전송
        AiResponseDto aiResponse = aiRecommendService.sendToAiServer(post, image);
        List<String> keywords = aiResponse.getKeywords();

        // 1. post_keyword 테이블에 저장
        for (String keyword : keywords) {
            postKeywordRepository.save(PostKeyword.builder()
                    .postId(post.getPostId())
                    .keywordText(keyword)
                    .build());
        }

        // 2. 분실물 게시글일 경우 → keyword 테이블에도 저장
        LostBoard lostBoard = post.getLostBoard();
        if (lostBoard != null && lostBoard.isLost()) {
            for (String keyword : keywords) {
                boolean exists = keywordRepository.existsByKeywordTextAndBoardTypeAndUser_UserId(
                        keyword, BoardType.LOST, post.getUser().getUserId()
                );
                if (!exists) {
                    Keyword newKeyword = Keyword.builder()
                            .keywordText(keyword)
                            .boardType(BoardType.LOST)
                            .user(post.getUser())
                            .build();
                    keywordRepository.save(newKeyword);
                }
            }
        }

        // 3. 푸시 알림
        notificationService.notifyUsersByKeywords(
                keywords,
                post.getBoardType(),
                postId
        );

        return aiResponse;
    }

    public List<LostItemMapResponseDto> getFoundItemsForMap() {
        List<LostBoard> foundItems = lostBoardRepository.findByIsLostWithPost(false);  // false = 습득물

        return foundItems.stream()
                .map(item -> new LostItemMapResponseDto(
                        item.getPost().getPostId(),
                        item.getPost().getTitle(),
                        item.getLostLatitude(),
                        item.getLostLongitude()
                ))
                .collect(Collectors.toList());
    }

    public List<LostBoardDto> getLostBoardsByType(boolean isLost) {
        return lostBoardRepository.findByIsLostWithPost(isLost)
                .stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }


    public LostBoardDto getLostBoardByPostId(int postId) {
        LostBoard board = lostBoardRepository.findWithPostByPostId(postId)
                .orElseThrow(() -> new IllegalArgumentException("게시글을 찾을 수 없습니다."));
        return convertToDto(board);
    }

    private LostBoardDto convertToDto(LostBoard board) {
        LostBoardDto dto = new LostBoardDto();
        dto.setPostId(board.getPostId());
        dto.setPlace(board.getPlace());
        dto.setContents(board.getPost().getContents()); // ✅ Post로부터 상세 설명 가져오기
        dto.setWriteTime(board.getWriteTime());
        dto.setPhoto(board.getPhoto());
        dto.setLost(board.isLost());
        dto.setLostLatitude(board.getLostLatitude());
        dto.setLostLongitude(board.getLostLongitude());
        dto.setRelativeTime(getRelativeTime(board.getWriteTime()));
        dto.setNickname(board.getPost().getUser().getNickname());
        dto.setTitle(board.getPost().getTitle());
        dto.setUserId(board.getPost().getUser().getUserId()); // ✅ 닉네임 설정
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

    // 자기 학교 것만 보이게 하는 거 .
    public List<LostBoardDto> getLostBoardsByUserCampusAndType(int userId, boolean isLost) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));
        int campusId = user.getCampus().getCampusId();

        return lostBoardRepository.findByCampusAndIsLost(campusId, isLost).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    // 마커 자기 학교 것만 보이게 하는 거.
    public List<LostItemMapResponseDto> getFoundItemsForMapByUserCampus(int userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));
        int campusId = user.getCampus().getCampusId();

        List<LostBoard> foundItems = lostBoardRepository.findByCampusAndIsLost(campusId, false);

        return foundItems.stream()
                .filter(item -> item.getLostLatitude() != null && item.getLostLongitude() != null)
                .map(item -> new LostItemMapResponseDto(
                        item.getPost().getPostId(),
                        item.getPost().getTitle(),
                        item.getLostLatitude(),
                        item.getLostLongitude()
                ))
                .collect(Collectors.toList());
    }

    public List<LostBoardDto> searchBoardsByCampusAndType(int userId, String query, boolean isLost) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));
        int campusId = user.getCampus().getCampusId();

        return lostBoardRepository.searchByCampusAndTextAndType(campusId, query, isLost).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());

    }

    public List<LostBoardDto> getLostBoardDetailsByPostIds(List<Integer> postIds) {
        return lostBoardRepository.findWithPostByPostIds(postIds).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }



}
