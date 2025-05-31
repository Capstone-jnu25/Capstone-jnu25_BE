package com.jnu.capstone.service;

import com.jnu.capstone.dto.ChatroomResponseDto;
import com.jnu.capstone.entity.ChatJoin;
import com.jnu.capstone.entity.User;
import com.jnu.capstone.repository.ChatJoinRepository;
import com.jnu.capstone.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ChatroomService {

    @Autowired
    private ChatJoinRepository chatJoinRepository;
    @Autowired
    private UserRepository userRepository;
    @Transactional(readOnly = true)
    public List<ChatroomResponseDto> getMyChatrooms(int userId) {
        // 요청자 닉네임 조회
        String myNickname = userRepository.findById(userId)
                .map(User::getNickname)
                .orElse("알 수 없음");

        List<ChatJoin> chatJoins = chatJoinRepository.findByUserId(userId);

        return chatJoins.stream()
                .filter(chatJoin -> !chatJoin.getChatroom().getPost().isDeleted())
                .map(chatJoin -> {
                    var chatroom = chatJoin.getChatroom();
                    String title = chatroom.getChatTitle();
                    String finalTitle = title;

                    // 1:1 채팅인 경우 상대 닉네임만 추출
                    if (title.contains(",") && title.split(",").length == 2) {
                        finalTitle = extractOpponentNickname(title, myNickname);
                    }

                    return new ChatroomResponseDto(
                            chatroom.getChattingRoomId(),
                            finalTitle,
                            "마지막 메시지",
                            chatroom.getPost().getBoardType().toString()
                    );
                })
                .collect(Collectors.toList());
    }

    private String extractOpponentNickname(String chatTitle, String myNickname) {
        String[] names = chatTitle.split(",");
        if (names.length != 2) return chatTitle;
        return names[0].equals(myNickname) ? names[1] : names[0];
    }
}

