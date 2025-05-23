package com.jnu.capstone.controller;

import com.jnu.capstone.dto.PostCreateRequestDto;
import com.jnu.capstone.dto.PostResponseDto;
import com.jnu.capstone.dto.GatheringDetailResponseDto;
import com.jnu.capstone.service.GatheringService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
@RestController
@RequestMapping("/api/gathering")
public class GatheringController {

    @Autowired
    private GatheringService gatheringService;

    @GetMapping
    public ResponseEntity<?> getGatheringPosts(
            @RequestHeader("User-Id") int userId,
            @RequestParam("boardType") String boardType,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size
    ) {
        // 페이징 설정
        Pageable pageable = PageRequest.of(page, size);

        // 서비스 호출
        Page<PostResponseDto> posts = gatheringService.getGatheringPosts(userId, boardType, pageable);

        Map<String, Object> response = Map.of(
                "status", "success",
                "data", posts
        );

        return ResponseEntity.ok(response);
    }


    @GetMapping("/{postId}")
    public ResponseEntity<?> getGatheringDetail(@PathVariable int postId) {
        GatheringDetailResponseDto responseDto = gatheringService.getGatheringDetail(postId);
        Map<String, Object> response = Map.of(
                "status", "success",
                "data", responseDto
        );

        return ResponseEntity.ok(response);

    }

    @PostMapping
    public ResponseEntity<?> createGathering(
            @RequestHeader("User-Id") int userId,
            @RequestBody PostCreateRequestDto requestDto
    ) {
        try {
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
