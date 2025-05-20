package com.jnu.capstone.service;

import com.jnu.capstone.dto.MessageResponseDto;
import com.jnu.capstone.entity.Chatroom;
import com.jnu.capstone.entity.Message;
import com.jnu.capstone.repository.ChatroomRepository;
import com.jnu.capstone.repository.MessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class MessageService {

    @Autowired
    private ChatroomRepository chatroomRepository;

    @Autowired
    private MessageRepository messageRepository;

    public Page<MessageResponseDto> getMessages(int chattingRoomId, Pageable pageable) {
        Chatroom chatroom = chatroomRepository.findById(chattingRoomId)
                .orElseThrow(() -> new IllegalArgumentException("채팅방이 존재하지 않습니다."));

        // 과거 메시지부터 불러오기 (오래된 메시지 -> 최신 메시지)
        Page<Message> messages = messageRepository.findByChatroomOrderBySendTimeAsc(chatroom, pageable);

        return messages.map(message -> new MessageResponseDto(
                message.getMessageId(),
                message.getSender().getUserId(),
                message.getDetailMessage(),
                message.getSendTime()
        ));
    }
}
