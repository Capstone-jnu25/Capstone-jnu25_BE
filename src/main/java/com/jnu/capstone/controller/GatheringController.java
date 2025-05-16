package com.jnu.capstone.controller;

import com.jnu.capstone.dto.PostResponseDto;
import com.jnu.capstone.dto.GatheringDetailResponseDto;
import com.jnu.capstone.service.GatheringService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/gathering")
public class GatheringController {

    @Autowired
    private GatheringService gatheringService;

    @GetMapping
    public ResponseEntity<?> getGatheringPosts(
            @RequestParam("boardType") String boardType,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size
    ) {
        // 페이징 설정
        Pageable pageable = PageRequest.of(page, size);

        // 서비스 호출
        Page<PostResponseDto> posts = gatheringService.getGatheringPosts(boardType, pageable);

        // 응답 형식 맞추기
        return ResponseEntity.ok().body(posts);
    }

    @GetMapping("/{postId}")
    public ResponseEntity<?> getGatheringDetail(@PathVariable int postId) {
        GatheringDetailResponseDto responseDto = gatheringService.getGatheringDetail(postId);
        return ResponseEntity.ok().body(responseDto);
    }
}
