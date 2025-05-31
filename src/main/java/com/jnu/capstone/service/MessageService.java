package com.jnu.capstone.service;

import com.jnu.capstone.dto.MessageResponseDto;
import com.jnu.capstone.dto.ChatMessagesPageResponseDto;
import com.jnu.capstone.entity.Chatroom;
import com.jnu.capstone.entity.Message;
import com.jnu.capstone.repository.ChatroomRepository;
import com.jnu.capstone.repository.MessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class MessageService {

    @Autowired
    private ChatroomRepository chatroomRepository;

    @Autowired
    private MessageRepository messageRepository;

    public ChatMessagesPageResponseDto getMessages(int chattingRoomId, Pageable pageable) {
        Chatroom chatroom = chatroomRepository.findById(chattingRoomId)
                .orElseThrow(() -> new IllegalArgumentException("채팅방이 존재하지 않습니다."));

        Page<Message> messages = messageRepository.findByChatroomOrderBySendTimeAsc(chatroom, pageable);

        List<MessageResponseDto> messageDtos = messages.getContent().stream()
                .map(m -> new MessageResponseDto(
                        m.getMessageId(),
                        m.getSender().getUserId(),
                        m.getSender().getNickname(),
                        m.getDetailMessage(),
                        m.getSendTime()
                )).toList();

        return new ChatMessagesPageResponseDto(
                chatroom.getChattingRoomId(),
                chatroom.getChatTitle(),
                messages.isLast(),
                messages.getTotalPages(),
                messages.getTotalElements(),
                messages.getSize(),
                messages.getNumber(),
                "success",
                messageDtos
        );
    }


}
