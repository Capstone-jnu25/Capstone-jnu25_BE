package com.jnu.capstone.service;

import com.jnu.capstone.dto.LostBoardCreateRequestDto;
import com.jnu.capstone.dto.LostBoardDto;
import com.jnu.capstone.entity.LostBoard;
import com.jnu.capstone.entity.Post;
import com.jnu.capstone.repository.LostBoardRepository;
import com.jnu.capstone.repository.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class LostBoardService {

    @Autowired
    private LostBoardRepository lostBoardRepository;

    @Autowired
    private PostRepository postRepository;

    // 게시글 생성
    public void createLostBoard(LostBoardCreateRequestDto dto) {
        Post post = postRepository.findById(dto.getPostId())
                .orElseThrow(() -> new IllegalArgumentException("해당 postId가 존재하지 않습니다."));

        LostBoard lostBoard = new LostBoard();
        lostBoard.setPost(post);
        lostBoard.setPostId(dto.getPostId());
        lostBoard.setPlace(dto.getPlace());
        lostBoard.setWriteTime(dto.getWriteTime());
        lostBoard.setPhoto(dto.getPhoto());
        lostBoard.setLost(dto.isLost());
        lostBoard.setLostLatitude(dto.getLostLatitude());
        lostBoard.setLostLongitude(dto.getLostLongitude());

        lostBoardRepository.save(lostBoard);
    }

    // 분실 or 습득 글 목록 조회
    public List<LostBoardDto> getLostBoardsByType(boolean isLost) {
        List<LostBoard> boards = lostBoardRepository.findByIsLost(isLost);
        return boards.stream().map(this::convertToDto).collect(Collectors.toList());
    }

    // 게시글 상세 조회
    public LostBoardDto getLostBoardByPostId(int postId) {
        LostBoard board = lostBoardRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("게시글을 찾을 수 없습니다."));
        return convertToDto(board);
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

    // 엔티티 → DTO 변환 메서드
    private LostBoardDto convertToDto(LostBoard board) {
        LostBoardDto dto = new LostBoardDto();
        dto.setPostId(board.getPostId());
        dto.setPlace(board.getPlace());
        dto.setWriteTime(board.getWriteTime());
        dto.setPhoto(board.getPhoto());
        dto.setLost(board.isLost());
        dto.setLostLatitude(board.getLostLatitude());
        dto.setLostLongitude(board.getLostLongitude());
        return dto;
    }
}
