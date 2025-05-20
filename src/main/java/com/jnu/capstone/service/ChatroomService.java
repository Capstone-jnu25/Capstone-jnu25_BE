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

        // DTO 변환
        return chatJoins.stream()
                .map(chatJoin -> {
                    var chatroom = chatJoin.getChatroom();

                    // 강제 초기화 (Lazy Loading 문제 해결)
                    String chatTitle = chatroom.getChatTitle();
                    String boardType = chatroom.getPost().getBoardType().toString();

                    return new ChatroomResponseDto(
                            chatroom.getChattingRoomId(),
                            chatTitle,
                            "마지막 메시지",  // TODO: 실제 메시지로 교체 필요
                            boardType
                    );
                })
                .collect(Collectors.toList());
    }
}

