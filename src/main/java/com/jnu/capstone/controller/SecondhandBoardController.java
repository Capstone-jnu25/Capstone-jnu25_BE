package com.jnu.capstone.controller;

import com.jnu.capstone.dto.SecondhandBoardCreateRequestDto;
import com.jnu.capstone.dto.SecondhandBoardDto;
import com.jnu.capstone.service.SecondhandBoardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.jnu.capstone.util.JwtTokenProvider;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/secondhand")
public class SecondhandBoardController {

    @Autowired
    private SecondhandBoardService secondhandBoardService;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    // 게시글 등록
    @PostMapping
        public String createBoard(@RequestBody SecondhandBoardCreateRequestDto dto,
                              @RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationHeader) {

        String token = authorizationHeader.replace("Bearer ", "");
        int userId = jwtTokenProvider.getUserIdFromToken(token); // 사용자 ID 추출

        secondhandBoardService.createSecondhandBoard(dto, userId);
        return "중고거래 게시글이 등록되었습니다.";
    }

    // 전체 목록 조회
    @GetMapping
    public ResponseEntity<?> getAllBoards(@RequestHeader(HttpHeaders.AUTHORIZATION) String authHeader) {
        String token = authHeader.replace("Bearer ", "");
        int userId = jwtTokenProvider.getUserIdFromToken(token);

        List<SecondhandBoardDto> boards = secondhandBoardService.getBoardsByUserCampus(userId);

        return ResponseEntity.ok(Map.of(
                "status", "success",
                "data", boards
        ));
    }

    // 게시글 상세 조회
    @GetMapping("/{postId}")
    public SecondhandBoardDto getBoardById(@PathVariable int postId) {
        return secondhandBoardService.getBoardByPostId(postId);
    }

    // 검색기능(text검색)
    @GetMapping("/search")
    public ResponseEntity<?> searchBoards(
            @RequestHeader("Authorization") String authHeader,
            @RequestParam("query") String query
    ) {
        String token = authHeader.replace("Bearer ", "");
        int userId = jwtTokenProvider.getUserIdFromToken(token);

        List<SecondhandBoardDto> result = secondhandBoardService.searchBoardsByCampusAndQuery(userId, query);

        return ResponseEntity.ok(Map.of(
                "status", "success",
                "data", result
        ));
    }

}
