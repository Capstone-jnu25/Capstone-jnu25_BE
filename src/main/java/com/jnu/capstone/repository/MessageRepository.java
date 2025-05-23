package com.jnu.capstone.repository;

import com.jnu.capstone.entity.Message;
import com.jnu.capstone.entity.Chatroom;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MessageRepository extends JpaRepository<Message, Long> {
    Page<Message> findByChatroomOrderBySendTimeAsc(Chatroom chatroom, Pageable pageable);
}

