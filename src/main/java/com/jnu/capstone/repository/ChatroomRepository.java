package com.jnu.capstone.repository;

import com.jnu.capstone.entity.Chatroom;
import com.jnu.capstone.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface ChatroomRepository extends JpaRepository<Chatroom, Integer> {
    // Post ID로 채팅방 찾기
    Optional<Chatroom> findByPost_PostId(int postId);
}
