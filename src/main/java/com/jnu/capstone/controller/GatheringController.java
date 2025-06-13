package com.jnu.capstone.controller;

import com.jnu.capstone.dto.GatheringDetailResponseDto;
import com.jnu.capstone.dto.PostCreateRequestDto;
import com.jnu.capstone.dto.PostResponseDto;
import com.jnu.capstone.service.GatheringService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.jnu.capstone.util.JwtTokenProvider;

import java.util.Map;
@RestController
@RequestMapping("/api/gathering")
public class GatheringController {

    @Autowired
    private GatheringService gatheringService;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    // JWT에서 사용자 ID 추출
    private int extractUserId(String authHeader) {
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            throw new IllegalArgumentException("유효하지 않은 인증 토큰입니다.");
        }
        String token = authHeader.substring(7); // "Bearer " 이후의 토큰
        return jwtTokenProvider.getUserIdFromToken(token);
    }

    @GetMapping
    public ResponseEntity<?> getGatheringPosts(
            @RequestHeader("Authorization") String authHeader,
            @RequestParam("boardType") String boardType,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size
    ) {
        int userId = extractUserId(authHeader);
        // 페이징 설정
        Pageable pageable = PageRequest.of(page, size);

        // 서비스 호출
        Page<PostResponseDto> postsPage = gatheringService.getGatheringPosts(userId, boardType, pageable);

        Map<String, Object> response = Map.of(
                "status", "success",
                "data", postsPage.getContent(), // ✅ content만 반환
                "meta", Map.of(               // ✅ 필요한 메타 정보만 반환
                        "page", postsPage.getNumber(),
                        "size", postsPage.getSize(),
                        "totalElements", postsPage.getTotalElements()
                )
        );

        return ResponseEntity.ok(response);
    }


    @GetMapping("/{postId}")
    public ResponseEntity<?> getGatheringDetail(@PathVariable int postId, @RequestHeader("Authorization") String authHeader) {
        int userId = extractUserId(authHeader);
        GatheringDetailResponseDto responseDto = gatheringService.getGatheringDetail(postId);

        int authorId = responseDto.getAuthorId(); // 작성자 ID 포함 필요
        boolean isAuthor = (userId == authorId);

        Map<String, Object> response = Map.of(
                "status", "success",
                "data", responseDto,
                "isAuthor", isAuthor
        );

        return ResponseEntity.ok(response);

    }

    @PostMapping
    public ResponseEntity<?> createGathering(
            @RequestHeader("Authorization") String authHeader,
            @RequestBody PostCreateRequestDto requestDto
    ) {
        try {
            int userId = extractUserId(authHeader);
            int postId = gatheringService.createGathering(userId, requestDto);

            return ResponseEntity.ok(Map.of(
                    "status", "success",
                    "data", Map.of("post_id", postId)
            ));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of(
                    "status", "error",
                    "message", e.getMessage()
            ));
        }
    }
}
