package com.jnu.capstone.controller;

import com.jnu.capstone.dto.LostBoardCreateRequestDto;
import com.jnu.capstone.dto.LostBoardDto;
import com.jnu.capstone.dto.LostItemMapResponseDto;
import com.jnu.capstone.service.LostBoardService;
import com.jnu.capstone.util.JwtTokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/lostboards")
public class LostBoardController {

    @Autowired
    private LostBoardService lostBoardService;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    // 게시글 생성
    @PostMapping
    public String createLostBoard(
            @RequestHeader("Authorization") String token,
            @RequestBody LostBoardCreateRequestDto requestDto) {

        String jwt = token.replace("Bearer ", "");
        int userId = jwtTokenProvider.getUserIdFromToken(jwt);

        lostBoardService.createLostBoard(userId, requestDto);
        return "게시글이 성공적으로 작성되었습니다.";
    }

    // 지도에 마커표시하기 위해 필요한 리스트.
    @GetMapping("/found/map")
    public ResponseEntity<List<LostItemMapResponseDto>> getFoundItemsForMap(
            @RequestHeader("Authorization") String token
    ) {
        String jwt = token.replace("Bearer ", "");
        int userId = jwtTokenProvider.getUserIdFromToken(jwt);

        List<LostItemMapResponseDto> result = lostBoardService.getFoundItemsForMapByUserCampus(userId);
        return ResponseEntity.ok(result);
    }

    // 게시글 목록 조회 (분실/습득 필터링)
    // 🔁수정된 목록 조회 (학교 필터링 포함)
    @GetMapping
    public ResponseEntity<?> getAllLostBoards(
            @RequestHeader("Authorization") String token,
            @RequestParam boolean isLost
    ) {
        String jwt = token.replace("Bearer ", "");
        int userId = jwtTokenProvider.getUserIdFromToken(jwt);

        List<LostBoardDto> boards = lostBoardService.getLostBoardsByUserCampusAndType(userId, isLost);

        return ResponseEntity.ok().body(
                java.util.Map.of(
                        "status", "success",
                        "data", boards
                )
        );
    }


    // 게시글 상세 조회
    @GetMapping("/{postId}")
    public LostBoardDto getLostBoardById(@PathVariable int postId) {
        return lostBoardService.getLostBoardByPostId(postId);
    }

    //검색 (텍스트로)
    @GetMapping("/search")
    public ResponseEntity<?> searchLostBoards(
            @RequestHeader("Authorization") String authHeader,
            @RequestParam("query") String query,
            @RequestParam("isLost") boolean isLost
    ) {
        String token = authHeader.replace("Bearer ", "");
        int userId = jwtTokenProvider.getUserIdFromToken(token);

        List<LostBoardDto> result = lostBoardService.searchBoardsByCampusAndType(userId, query, isLost);

        return ResponseEntity.ok(Map.of(
                "status", "success",
                "data", result
        ));
    }



}
