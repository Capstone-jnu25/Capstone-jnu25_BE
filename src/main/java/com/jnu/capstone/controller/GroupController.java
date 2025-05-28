package com.jnu.capstone.controller;

import com.jnu.capstone.dto.GroupDto;
import com.jnu.capstone.service.ApplicantService;
import com.jnu.capstone.util.JwtTokenProvider;
import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/group")
@RequiredArgsConstructor
public class GroupController {
    private final ApplicantService chatroomService; // 또는 ChatJoinService
    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    // ✅ JWT 토큰에서 userId 추출하는 메서드
    private int extractUserId(String authHeader) {
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            throw new IllegalArgumentException("유효하지 않은 인증 토큰입니다.");
        }
        String token = authHeader.substring(7);
        return jwtTokenProvider.getUserIdFromToken(token);
    }
    @GetMapping
    public ResponseEntity<?> getMyGroups(@RequestHeader("Authorization") String authHeader) {
        int userId = extractUserId(authHeader);
        List<GroupDto> groups = chatroomService.getMyGroups(userId);
        return ResponseEntity.ok(Map.of(
                "status", "success",
                "data", groups
        ));
    }
}

