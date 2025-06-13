package com.jnu.capstone.repository;

import com.jnu.capstone.entity.Chatroom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface ChatroomRepository extends JpaRepository<Chatroom, Integer> {
    // Post ID로 채팅방 찾기
    Optional<Chatroom> findByPost_PostId(int postId);

    @Query("SELECT c FROM Chatroom c " +
            "JOIN ChatJoin cj1 ON c = cj1.chatroom " +
            "JOIN ChatJoin cj2 ON c = cj2.chatroom " +
            "WHERE c.post.postId = :postId AND " +
            "((cj1.user.userId = :user1 AND cj2.user.userId = :user2) OR " +
            " (cj1.user.userId = :user2 AND cj2.user.userId = :user1))")
    Optional<Chatroom> findPrivateChatroom(@Param("postId") int postId,
                                           @Param("user1") int user1,
                                           @Param("user2") int user2);
}
