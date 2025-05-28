package com.jnu.capstone.controller;

import com.jnu.capstone.dto.ChatroomResponseDto;
import com.jnu.capstone.service.ChatroomService;
import com.jnu.capstone.util.JwtTokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/chatrooms")
public class ChatroomController {

    @Autowired
    private ChatroomService chatroomService;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    // ✅ JWT에서 사용자 ID 추출
    private int extractUserId(String authHeader) {
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            throw new IllegalArgumentException("유효하지 않은 인증 토큰입니다.");
        }
        String token = authHeader.substring(7);
        return jwtTokenProvider.getUserIdFromToken(token);
    }
    @GetMapping
    public ResponseEntity<?> getMyChatrooms(@RequestHeader("Authorization") String authHeader) {
        int userId = extractUserId(authHeader);
        List<ChatroomResponseDto> chatrooms = chatroomService.getMyChatrooms(userId);
        return ResponseEntity.ok(Map.of(
                "status", "success",
                "data", chatrooms
        ));
    }
}
