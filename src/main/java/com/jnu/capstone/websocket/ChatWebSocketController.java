package com.jnu.capstone.websocket;

import com.jnu.capstone.dto.MessageResponseDto;
import com.jnu.capstone.entity.Chatroom;
import com.jnu.capstone.entity.Message;
import com.jnu.capstone.entity.User;
import com.jnu.capstone.repository.ChatroomRepository;
import com.jnu.capstone.repository.UserRepository;
import com.jnu.capstone.repository.MessageRepository;
import com.jnu.capstone.util.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import java.time.LocalDateTime;
import java.util.Map;

@Controller
@RequiredArgsConstructor
public class ChatWebSocketController {

    private final SimpMessagingTemplate messagingTemplate;
    private final ChatroomRepository chatroomRepository;
    private final UserRepository userRepository;
    private final MessageRepository messageRepository;
    private final JwtTokenProvider jwtTokenProvider;

    @MessageMapping("/chat/send") // 클라이언트에서 보내는 주소: /app/chat/send
    public void handleMessage(@Payload Map<String, String> payload) {
        String token = payload.get("token");
        int chattingRoomId = Integer.parseInt(payload.get("chattingRoomId"));
        String detailMessage = payload.get("detailMessage");

        int userId = jwtTokenProvider.getUserIdFromToken(token);

        Chatroom chatroom = chatroomRepository.findById(chattingRoomId)
                .orElseThrow(() -> new IllegalArgumentException("채팅방 없음"));

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("유저 없음"));

        // 메시지 저장
        Message message = new Message(chatroom, user, detailMessage);
        message.setSendTime(LocalDateTime.now());
        messageRepository.save(message);

        // 응답 DTO
        MessageResponseDto dto = new MessageResponseDto(
                message.getMessageId(),
                user.getUserId(),
                message.getDetailMessage(),
                message.getSendTime()
        );

        // /topic/chatroom/10 같은 채널로 브로드캐스트
        messagingTemplate.convertAndSend("/topic/chatroom/" + chattingRoomId, dto);
    }
}
