package com.jnu.capstone.controller;

import com.jnu.capstone.dto.ChatroomResponseDto;
import com.jnu.capstone.service.ChatroomService;
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

    @GetMapping
    public ResponseEntity<?> getMyChatrooms(@RequestHeader("User-Id") int userId) {
        List<ChatroomResponseDto> chatrooms = chatroomService.getMyChatrooms(userId);
        return ResponseEntity.ok(Map.of(
                "status", "success",
                "data", chatrooms
        ));
    }
}
