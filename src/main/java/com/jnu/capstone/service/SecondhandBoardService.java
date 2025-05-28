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

    public void createSecondhandBoard(SecondhandBoardCreateRequestDto dto, int userId) {
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
        board.setPostId(post.getPostId());
        board.setPlace(dto.getPlace());
        board.setWriteTime(new Date()); // 서버에서 시간 설정
        board.setPhoto(dto.getPhoto());
        board.setPrice(dto.getPrice());

        secondhandBoardRepository.save(board);
    }


    // 게시글 등록
//    public void createSecondhandBoard(SecondhandBoardCreateRequestDto dto, int userId, int campusId) {
//        // 1. Post 엔티티 생성
//        User user = userRepository.findById(userId)
//                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 사용자입니다."));
//        School campus = schoolRepository.findById(campusId)
//                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 캠퍼스입니다."));
//
//        Post post = new Post();
//        post.setTitle(dto.getTitle());
//        post.setContents(dto.getContents());
//        post.setBoardType(BoardType.SECONDHAND);
//        post.setUser(user);
//        post.setCampus(campus);
//        post = postRepository.save(post); // postId 자동 생성됨
//
//        // 2. SecondhandBoard 생성
//        SecondhandBoard board = new SecondhandBoard();
//        board.setPost(post);
//        board.setPostId(post.getPostId());
//        board.setPlace(dto.getPlace());
//        board.setWriteTime(new Date());
//        board.setPhoto(dto.getPhoto());
//        board.setPrice(dto.getPrice());
//
//        secondhandBoardRepository.save(board);
//    }

//    public void createSecondhandBoard(SecondhandBoardCreateRequestDto dto) {
//        Post post = postRepository.findById(dto.getPostId())
//                .orElseThrow(() -> new IllegalArgumentException("해당 postId의 게시글이 존재하지 않습니다."));
//
//        SecondhandBoard board = new SecondhandBoard();
//        board.setPost(post);
//        board.setPostId(dto.getPostId());
//        board.setPlace(dto.getPlace());
//        board.setWriteTime(dto.getWriteTime());
//        board.setPhoto(dto.getPhoto());
//        board.setPrice(dto.getPrice());
//
//        secondhandBoardRepository.save(board);
//    }

    // 게시글 전체 목록 조회
    public List<SecondhandBoardDto> getAllBoards() {
        return secondhandBoardRepository.findAll()
                .stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    // 게시글 상세 조회
    public SecondhandBoardDto getBoardByPostId(int postId) {
        SecondhandBoard board = secondhandBoardRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("게시글을 찾을 수 없습니다."));
        return convertToDto(board);
    }

    private SecondhandBoardDto convertToDto(SecondhandBoard board) {
        SecondhandBoardDto dto = new SecondhandBoardDto();
        dto.setPostId(board.getPostId());
        dto.setPlace(board.getPlace());
        dto.setWriteTime(board.getWriteTime());
        dto.setPhoto(board.getPhoto());
        dto.setPrice(board.getPrice());
        dto.setRelativeTime(getRelativeTime(board.getWriteTime())); // ⬅️ 추가
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



}
