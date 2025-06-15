package com.jnu.capstone.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jnu.capstone.dto.AiResponseDto;
import com.jnu.capstone.dto.LostBoardCreateRequestDto;
import com.jnu.capstone.dto.LostBoardDto;
import com.jnu.capstone.dto.LostItemMapResponseDto;
import com.jnu.capstone.service.ImageUploadService;
import com.jnu.capstone.service.LostBoardService;
import com.jnu.capstone.util.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


import java.util.Date;
import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/lostboards")
public class LostBoardController {

    private final LostBoardService lostBoardService;
    private final JwtTokenProvider jwtTokenProvider;
    private final ImageUploadService imageUploadService; // ✅ 추가됨

    // 게시글 생성 (수정됨)
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> createLostBoard(
            @RequestHeader("Authorization") String token,
            @RequestPart("data") String rawJson,
            @RequestPart("image") MultipartFile image) {

        try {
            String jwt = token.replace("Bearer ", "");
            int userId = jwtTokenProvider.getUserIdFromToken(jwt);

            // 이미지 업로드
            String imageUrl = imageUploadService.uploadImage(image);

            // JSON 파싱
            ObjectMapper objectMapper = new ObjectMapper();
            LostBoardCreateRequestDto dto = objectMapper.readValue(rawJson, LostBoardCreateRequestDto.class);
            dto.setPhoto(imageUrl); // 업로드된 이미지 URL 설정

            // ① 게시글 저장
            int postId = lostBoardService.createLostBoard(userId, dto);

            // ② AI 서버 전송 및 응답 처리
            AiResponseDto aiResponse = lostBoardService.sendToAiServer(postId, image);

            List<String> keywords = aiResponse.getKeywords() != null ? aiResponse.getKeywords() : List.of();
            List<Integer> postIds = aiResponse.getPostIds() != null ? aiResponse.getPostIds() : List.of();

            // ③ 응답 반환
            return ResponseEntity.ok(Map.of(
                    "message", "분실/습득 게시글이 등록되었습니다.",
                    "keywords", keywords,
                    "postIds", postIds
            ));

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body(Map.of("message", "서버 오류", "error", e.getMessage()));
        }
    }



    // 지도 마커 표시용 리스트
    @GetMapping("/found/map")
    public ResponseEntity<List<LostItemMapResponseDto>> getFoundItemsForMap(
            @RequestHeader("Authorization") String token
    ) {
        String jwt = token.replace("Bearer ", "");
        int userId = jwtTokenProvider.getUserIdFromToken(jwt);

        List<LostItemMapResponseDto> result = lostBoardService.getFoundItemsForMapByUserCampus(userId);
        return ResponseEntity.ok(result);
    }

    // 게시글 목록 조회 (isLost 필터링 포함)
    @GetMapping
    public ResponseEntity<?> getAllLostBoards(
            @RequestHeader("Authorization") String token,
            @RequestParam boolean isLost
    ) {
        String jwt = token.replace("Bearer ", "");
        int userId = jwtTokenProvider.getUserIdFromToken(jwt);

        List<LostBoardDto> boards = lostBoardService.getLostBoardsByUserCampusAndType(userId, isLost);

        return ResponseEntity.ok().body(
                Map.of(
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

    // 게시글 검색
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

    // 추천된 postId들로 상세 게시글 목록 반환
    @PostMapping("/recommend")
    public ResponseEntity<?> getRecommendedLostBoards(
            @RequestHeader("Authorization") String token,
            @RequestBody Map<String, List<Integer>> requestBody) {

        String jwt = token.replace("Bearer ", "");
        int userId = jwtTokenProvider.getUserIdFromToken(jwt);

        List<Integer> postIds = requestBody.getOrDefault("postIds", List.of());
        List<LostBoardDto> boards = lostBoardService.getLostBoardDetailsByPostIds(postIds);

        return ResponseEntity.ok(Map.of("status", "success", "data", boards));
    }

}
