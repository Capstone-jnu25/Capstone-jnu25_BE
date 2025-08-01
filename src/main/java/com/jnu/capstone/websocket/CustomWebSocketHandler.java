package com.jnu.capstone.websocket;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jnu.capstone.dto.MessageResponseDto;
import com.jnu.capstone.entity.Chatroom;
import com.jnu.capstone.entity.Message;
import com.jnu.capstone.entity.User;
import com.jnu.capstone.repository.ChatJoinRepository;
import com.jnu.capstone.repository.ChatroomRepository;
import com.jnu.capstone.repository.MessageRepository;
import com.jnu.capstone.repository.UserRepository;
import com.jnu.capstone.service.FcmService;
import com.jnu.capstone.util.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.*;
import org.springframework.web.socket.handler.TextWebSocketHandler;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.databind.SerializationFeature;

import java.time.LocalDateTime;
import java.util.*;

@Component
@RequiredArgsConstructor
public class CustomWebSocketHandler extends TextWebSocketHandler {

    private final UserRepository userRepository;
    private final ChatroomRepository chatroomRepository;
    private final MessageRepository messageRepository;
    private final JwtTokenProvider jwtTokenProvider;

    // 클라이언트와 연결된 세션들 저장 (채팅방 ID로 그룹핑)
    private final Map<Integer, List<WebSocketSession>> roomSessions = new HashMap<>();
    private final ChatJoinRepository chatJoinRepository;
    private final FcmService fcmService;

    private final ObjectMapper objectMapper = new ObjectMapper()
            .registerModule(new JavaTimeModule())
            .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

    @Override
    public void afterConnectionEstablished(WebSocketSession session) {
        System.out.println("✅ WebSocket 연결됨: " + session.getId());
    }

    @Override
    public void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        // ChatMessageDto 객체로 안전하게 역직렬화
        ChatMessageDto payload = objectMapper.readValue(message.getPayload(), ChatMessageDto.class);

        String token = payload.token;
        int chattingRoomId = payload.chattingRoomId;
        String detailMessage = payload.detailMessage;

        int userId = jwtTokenProvider.getUserIdFromToken(token);

        Chatroom chatroom = chatroomRepository.findById(chattingRoomId)
                .orElseThrow(() -> new IllegalArgumentException("채팅방 없음"));
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("유저 없음"));

        // 메시지 저장
        Message savedMessage = new Message(chatroom, user, detailMessage);
        savedMessage.setSendTime(LocalDateTime.now());
        messageRepository.save(savedMessage);

        // 응답 메시지 JSON 구성
        MessageResponseDto responseDto = new MessageResponseDto(
                savedMessage.getMessageId(),
                user.getUserId(),
                user.getNickname(),
                detailMessage,
                savedMessage.getSendTime()
        );

        String broadcast = objectMapper.writeValueAsString(responseDto);

        // 세션 저장 (채팅방별로 관리)
        roomSessions.putIfAbsent(chattingRoomId, new ArrayList<>());
        if (!roomSessions.get(chattingRoomId).contains(session)) {
            roomSessions.get(chattingRoomId).add(session);
        }

        // 같은 채팅방 사용자에게 브로드캐스트
        for (WebSocketSession ws : roomSessions.get(chattingRoomId)) {
            if (ws.isOpen()) {
                ws.sendMessage(new TextMessage(broadcast));
            }
        }

        // FCM 알림 전송 (발신자 제외)
        chatJoinRepository.findByChatroom(chatroom).stream()
                .map(join -> join.getUser())
                .filter(u -> u.getUserId() != userId)
                .forEach(u -> {
                    if (u.getFcmToken() != null && !u.getFcmToken().isEmpty()) {
                        fcmService.sendMessageTo(
                                u.getFcmToken(),
                                user.getNickname(),
                                detailMessage
                        );
                    }
                });


        System.out.println("📤 메시지 전송 완료 to room " + chattingRoomId);
    }

    public static class ChatMessageDto {
        public String token;
        public int chattingRoomId;
        public String detailMessage;
        public ChatMessageDto() {}
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) {
        System.out.println("❌ WebSocket 연결 종료: " + session.getId());

        // 모든 채팅방에서 세션 제거
        roomSessions.values().forEach(list -> list.remove(session));
    }
}
