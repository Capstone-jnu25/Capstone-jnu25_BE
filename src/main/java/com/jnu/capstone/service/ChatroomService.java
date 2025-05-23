package com.jnu.capstone.service;

import com.jnu.capstone.dto.ChatroomResponseDto;
import com.jnu.capstone.entity.ChatJoin;
import com.jnu.capstone.repository.ChatJoinRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ChatroomService {

    @Autowired
    private ChatJoinRepository chatJoinRepository;

    @Transactional(readOnly = true)
    public List<ChatroomResponseDto> getMyChatrooms(int userId) {
        // 사용자가 참여한 모든 채팅방 조회
        List<ChatJoin> chatJoins = chatJoinRepository.findByUserId(userId);

        return chatJoins.stream()
                .filter(chatJoin -> !chatJoin.getChatroom().getPost().isDeleted()) // ✅ 삭제된 게시글의 채팅방 숨기기
                .map(chatJoin -> {
                    var chatroom = chatJoin.getChatroom();
                    return new ChatroomResponseDto(
                            chatroom.getChattingRoomId(),
                            chatroom.getChatTitle(),
                            "마지막 메시지",
                            chatroom.getPost().getBoardType().toString()
                    );
                })
                .collect(Collectors.toList());
    }
}

