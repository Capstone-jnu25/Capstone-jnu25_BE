package com.jnu.capstone.controller;

import com.jnu.capstone.dto.MessageResponseDto;
import com.jnu.capstone.entity.Chatroom;
import com.jnu.capstone.entity.Message;
import com.jnu.capstone.entity.User;
import com.jnu.capstone.service.MessageService;
import com.jnu.capstone.repository.ChatroomRepository;
import com.jnu.capstone.repository.ChatJoinRepository;
import com.jnu.capstone.repository.MessageRepository;
import com.jnu.capstone.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.messaging.simp.SimpMessagingTemplate;

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
    private SimpMessagingTemplate messagingTemplate;
    @GetMapping("/{chattingRoomId}/messages")
    public ResponseEntity<?> getMessages(
            @PathVariable int chattingRoomId,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "30") int size
    ) {
        Pageable pageable = PageRequest.of(page, size);
        Page<MessageResponseDto> messages = messageService.getMessages(chattingRoomId, pageable);

        // 응답 데이터 생성
        Map<String, Object> response = Map.of(
                "status", "success",
                "data", messages.getContent(),
                "pageNumber", messages.getNumber(),
                "pageSize", messages.getSize(),
                "totalElements", messages.getTotalElements(),
                "totalPages", messages.getTotalPages(),
                "last", messages.isLast()
        );

        return ResponseEntity.ok(response);
    }

    @PostMapping("/{chattingRoomId}/messages")
    public ResponseEntity<?> sendMessage(
            @PathVariable int chattingRoomId,
            @RequestHeader("User-Id") int userId,
            @RequestBody Map<String, String> requestBody
    ) {
        // 채팅방 확인
        Chatroom chatroom = chatroomRepository.findById(chattingRoomId)
                .orElseThrow(() -> new IllegalArgumentException("채팅방이 존재하지 않습니다."));

        // 유저 확인
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("유저가 존재하지 않습니다."));

        // 채팅방 참여자 여부 확인
        boolean isParticipant = chatJoinRepository.existsByUserIdAndChattingRoomId(userId, chattingRoomId);
        if (!isParticipant) {
            return ResponseEntity.status(403).body(Map.of(
                    "status", "error",
                    "message", "이 채팅방의 참가자가 아닙니다."
            ));
        }

        // 메시지 내용 확인
        String detailMessage = requestBody.get("detailMessage");
        if (detailMessage == null || detailMessage.trim().isEmpty()) {
            return ResponseEntity.badRequest().body(Map.of(
                    "status", "error",
                    "message", "메시지 내용이 비어있습니다."
            ));
        }

        // 메시지 저장
        Message message = new Message(chatroom, user, detailMessage);
        messageRepository.save(message);

        // WebSocket을 통해 해당 채팅방 구독자에게 메시지 전송
        MessageResponseDto responseDto = new MessageResponseDto(
                message.getMessageId(),
                user.getUserId(),
                message.getDetailMessage(),
                message.getSendTime()
        );
        messagingTemplate.convertAndSend("/topic/chatroom/" + chattingRoomId, responseDto);

        return ResponseEntity.ok(Map.of(
                "status", "success",
                "data", responseDto
        ));
    }
}
