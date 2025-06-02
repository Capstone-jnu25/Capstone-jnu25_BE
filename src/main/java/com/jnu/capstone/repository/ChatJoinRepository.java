package com.jnu.capstone.repository;

import com.jnu.capstone.entity.ChatJoin;
import com.jnu.capstone.entity.ChatJoinId;
import com.jnu.capstone.entity.Chatroom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChatJoinRepository extends JpaRepository<ChatJoin, ChatJoinId> {
    // 특정 유저의 모든 참여 채팅방 조회
    List<ChatJoin> findByUserId(int userId);
    List<ChatJoin> findByChatroom(Chatroom chatroom);
    // 특정 유저가 특정 채팅방에 참여 중인지 확인
    boolean existsByUserIdAndChattingRoomId(int userId, int chattingRoomId);
}
