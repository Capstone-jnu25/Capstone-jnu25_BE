package com.jnu.capstone.controller;

import com.jnu.capstone.dto.PrivateChatResponseDto;
import com.jnu.capstone.entity.*;
import com.jnu.capstone.repository.*;
import com.jnu.capstone.service.PrivateChatService;
import com.jnu.capstone.util.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/private-chats")
public class PrivateChatController {

    private final JwtTokenProvider jwtTokenProvider;
    private final PrivateChatService privateChatService;

    @PostMapping
    public ResponseEntity<?> createOrGetPrivateChat(
            @RequestHeader("Authorization") String authHeader,
            @RequestBody Map<String, Integer> request
    ) {
        String token = authHeader.substring(7); // "Bearer " 제거
        int requesterId = jwtTokenProvider.getUserIdFromToken(token);
        int postId = request.get("postId");

        PrivateChatResponseDto responseDto = privateChatService.createOrGetPrivateChat(postId, requesterId);
        return ResponseEntity.ok(Map.of(
                "status", "success",
                "chattingRoomId", responseDto.getChattingRoomId(),
                "chatTitle", responseDto.getChatTitle()
        ));
    }
}
