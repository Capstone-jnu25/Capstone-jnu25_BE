package com.jnu.capstone.service;

import com.jnu.capstone.dto.PrivateChatResponseDto;
import com.jnu.capstone.entity.*;
import com.jnu.capstone.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PrivateChatService {

    private final ChatroomRepository chatroomRepository;
    private final ChatJoinRepository chatJoinRepository;
    private final UserRepository userRepository;
    private final PostRepository postRepository;

    @Transactional
    public PrivateChatResponseDto createOrGetPrivateChat(int postId, int requesterId) {

        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("해당 게시글이 존재하지 않습니다."));

        int writerId = post.getUser().getUserId();
        if (requesterId == writerId) {
            throw new IllegalArgumentException("자신의 게시글에는 채팅을 시작할 수 없습니다.");
        }

        User requester = userRepository.findById(requesterId)
                .orElseThrow(() -> new IllegalArgumentException("요청자 정보가 없습니다."));
        User writer = userRepository.findById(writerId)
                .orElseThrow(() -> new IllegalArgumentException("작성자 정보가 없습니다."));

        // 기존 채팅방 있는지 확인
        Chatroom existingRoom = chatroomRepository
                .findPrivateChatroom(postId, requesterId, writerId)
                .orElse(null);

        String title = requester.getNickname() + "," + writer.getNickname();

        if (existingRoom != null) {
            String opponentNickname = getOpponentNickname(existingRoom.getChatTitle(), requester.getNickname());
            return new PrivateChatResponseDto(existingRoom.getChattingRoomId(), opponentNickname);
        }

        // 채팅방 새로 생성
        Chatroom chatroom = new Chatroom();
        chatroom.setPost(post);
        chatroom.setChatTitle(title); // 닉네임1,닉네임2 형태 저장
        chatroomRepository.save(chatroom);

        // chat_join에 2명 등록
        chatJoinRepository.save(new ChatJoin(requesterId, chatroom.getChattingRoomId()));
        chatJoinRepository.save(new ChatJoin(writerId, chatroom.getChattingRoomId()));

        String opponentNickname = getOpponentNickname(title, requester.getNickname());
        return new PrivateChatResponseDto(chatroom.getChattingRoomId(), opponentNickname);
    }

    // ✅ 요청자의 닉네임을 기준으로 상대방 닉네임 추출
    private String getOpponentNickname(String chatTitle, String requesterNickname) {
        String[] nicknames = chatTitle.split(",");
        if (nicknames.length != 2) return "알 수 없음";

        if (nicknames[0].equals(requesterNickname)) {
            return nicknames[1];
        } else {
            return nicknames[0];
        }
    }
}
