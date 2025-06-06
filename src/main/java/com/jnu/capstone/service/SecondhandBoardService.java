package com.jnu.capstone.service;

import com.jnu.capstone.dto.SecondhandBoardCreateRequestDto;
import com.jnu.capstone.dto.SecondhandBoardDto;
import com.jnu.capstone.entity.*;
import com.jnu.capstone.repository.PostRepository;
import com.jnu.capstone.repository.SchoolRepository;
import com.jnu.capstone.repository.SecondhandBoardRepository;
import com.jnu.capstone.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional; // ✅ 추가

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

    @Transactional  // ✅ 이거 꼭 추가해 주세요!
    public void createSecondhandBoard(SecondhandBoardCreateRequestDto dto, int userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));
        School campus = user.getCampus();

        // 1. Post 생성 및 저장
        Post post = new Post();
        post.setTitle(dto.getTitle());
        post.setContents(dto.getContents());
        post.setBoardType(BoardType.SECONDHAND);
        post.setUser(user);
        post.setCampus(campus);
        post = postRepository.save(post); // 영속성 컨텍스트에 저장됨

        // 2. SecondhandBoard 생성
        SecondhandBoard board = new SecondhandBoard();
        board.setPost(post); // @MapsId: postId도 자동 설정됨
        board.setPlace(dto.getPlace());
        board.setWriteTime(new Date());
        board.setPhoto(dto.getPhoto());
        board.setPrice(dto.getPrice());

        secondhandBoardRepository.save(board); // 정상 저장
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





}
