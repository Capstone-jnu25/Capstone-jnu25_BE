package com.jnu.capstone.repository;

import com.jnu.capstone.entity.Message;
import com.jnu.capstone.entity.Chatroom;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MessageRepository extends JpaRepository<Message, Long> {
    @Query("SELECT m FROM Message m JOIN FETCH m.sender WHERE m.chatroom = :chatroom ORDER BY m.sendTime ASC")
    Page<Message> findByChatroomWithSender(@Param("chatroom") Chatroom chatroom, Pageable pageable);

    Optional<Message> findTopByChatroomOrderBySendTimeDesc(Chatroom chatroom);
}

