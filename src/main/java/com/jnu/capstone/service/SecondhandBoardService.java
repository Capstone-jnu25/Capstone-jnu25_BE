package com.jnu.capstone.service;

import com.jnu.capstone.dto.SecondhandBoardCreateRequestDto;
import com.jnu.capstone.dto.SecondhandBoardDto;
import com.jnu.capstone.entity.Post;
import com.jnu.capstone.entity.SecondhandBoard;
import com.jnu.capstone.repository.PostRepository;
import com.jnu.capstone.repository.SecondhandBoardRepository;
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

    // 게시글 등록
    public void createSecondhandBoard(SecondhandBoardCreateRequestDto dto) {
        Post post = postRepository.findById(dto.getPostId())
                .orElseThrow(() -> new IllegalArgumentException("해당 postId의 게시글이 존재하지 않습니다."));

        SecondhandBoard board = new SecondhandBoard();
        board.setPost(post);
        board.setPostId(dto.getPostId());
        board.setPlace(dto.getPlace());
        board.setWriteTime(dto.getWriteTime());
        board.setPhoto(dto.getPhoto());
        board.setPrice(dto.getPrice());

        secondhandBoardRepository.save(board);
    }

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
