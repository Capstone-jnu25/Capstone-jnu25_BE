package com.jnu.capstone.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jnu.capstone.dto.AiResponseDto;
import com.jnu.capstone.dto.SecondhandBoardCreateRequestDto;
import com.jnu.capstone.dto.SecondhandBoardDto;
import com.jnu.capstone.service.ImageUploadService;
import com.jnu.capstone.service.SecondhandBoardService;
import com.jnu.capstone.util.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/secondhand")
public class SecondhandBoardController {

    private final SecondhandBoardService secondhandBoardService;
    private final JwtTokenProvider jwtTokenProvider;
    private final ImageUploadService imageUploadService;

    // ✅ 게시글 등록 API (multipart/form-data 지원)
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> createBoard(
            @RequestPart(value = "data", required = true) String rawJson,
            @RequestPart("image") MultipartFile image,
            @RequestHeader("Authorization") String token) {

        try {
            // 사용자 ID 추출
            String jwt = token.replace("Bearer ", "");
            int userId = jwtTokenProvider.getUserIdFromToken(jwt);

            // 1. 이미지 업로드 → URL 획득
            String imageUrl = imageUploadService.uploadImage(image);

            // 2. JSON → DTO 변환
            ObjectMapper objectMapper = new ObjectMapper();
            SecondhandBoardCreateRequestDto dto = objectMapper.readValue(rawJson, SecondhandBoardCreateRequestDto.class);

            // 3. 이미지 URL DTO에 세팅
            dto.setPhoto(imageUrl);

            // ⭐️ 4. 게시글 먼저 DB에 저장하고 postId 반환
            int postId = secondhandBoardService.createSecondhandBoard(dto, userId);

            // ⭐️ 5. postId 포함해서 AI 서버 요청
            AiResponseDto aiResponse = secondhandBoardService.sendToAiServer(postId, image);

            // 6. 응답 구성
            Map<String, Object> responseMap = new HashMap<>();
            responseMap.put("message", "중고거래 게시글이 등록되었습니다.");
            responseMap.put("keywords", aiResponse != null ? aiResponse.getKeywords() : List.of());
            responseMap.put("postIds", aiResponse != null ? aiResponse.getPostIds() : List.of());

            return ResponseEntity.ok(responseMap);

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body(Map.of("message", "게시글 등록 중 오류 발생", "error", e.getMessage()));
        }
    }

    // 나머지 목록, 상세, 검색은 그대로 유지
    @GetMapping
    public ResponseEntity<?> getAllBoards(@RequestHeader("Authorization") String authHeader) {
        String token = authHeader.replace("Bearer ", "");
        int userId = jwtTokenProvider.getUserIdFromToken(token);
        List<SecondhandBoardDto> boards = secondhandBoardService.getBoardsByUserCampus(userId);

        return ResponseEntity.ok(Map.of("status", "success", "data", boards));
    }

    @GetMapping("/{postId}")
    public SecondhandBoardDto getBoardById(@PathVariable int postId) {
        return secondhandBoardService.getBoardByPostId(postId);
    }

    @GetMapping("/search")
    public ResponseEntity<?> searchBoards(
            @RequestHeader("Authorization") String authHeader,
            @RequestParam("query") String query) {

        String token = authHeader.replace("Bearer ", "");
        int userId = jwtTokenProvider.getUserIdFromToken(token);
        List<SecondhandBoardDto> result = secondhandBoardService.searchBoardsByCampusAndQuery(userId, query);

        return ResponseEntity.ok(Map.of("status", "success", "data", result));
    }
    @PostMapping("/recommend")
    public ResponseEntity<?> getRecommendedSecondhandBoards(
            @RequestHeader("Authorization") String token,
            @RequestBody Map<String, List<Integer>> requestBody) {

        String jwt = token.replace("Bearer ", "");
        int userId = jwtTokenProvider.getUserIdFromToken(jwt);

        List<Integer> postIds = requestBody.getOrDefault("postIds", List.of());
        List<SecondhandBoardDto> boards = secondhandBoardService.getSecondhandBoardDetailsByPostIds(postIds);

        return ResponseEntity.ok(Map.of("status", "success", "data", boards));
    }

}
