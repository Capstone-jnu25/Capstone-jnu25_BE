package com.jnu.capstone.controller;

import com.jnu.capstone.dto.ChatMessagesPageResponseDto;
import com.jnu.capstone.repository.ChatJoinRepository;
import com.jnu.capstone.repository.ChatroomRepository;
import com.jnu.capstone.repository.MessageRepository;
import com.jnu.capstone.repository.UserRepository;
import com.jnu.capstone.service.MessageService;
import com.jnu.capstone.util.JwtTokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/chatrooms")
public class MessageController {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ChatroomRepository chatroomRepository;
    @Autowired
    private ChatJoinRepository chatJoinRepository;
    @Autowired
    private MessageRepository messageRepository;

    @Autowired
    private MessageService messageService;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    // ✅ JWT 토큰에서 userId 추출
    private int extractUserId(String authHeader) {
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            throw new IllegalArgumentException("유효하지 않은 인증 토큰입니다.");
        }
        String token = authHeader.substring(7);
        return jwtTokenProvider.getUserIdFromToken(token);
    }
    @GetMapping("/{chattingRoomId}/messages")
    public ResponseEntity<?> getMessages(
            @PathVariable Integer chattingRoomId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "30") int size
    ) {
        Pageable pageable = PageRequest.of(page, size);
        ChatMessagesPageResponseDto chatData = messageService.getMessages(chattingRoomId, pageable);

        return ResponseEntity.ok(Map.of(
                "status", "success",
                "data", chatData
        ));
    }
}
