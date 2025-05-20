package com.jnu.capstone.repository;

import com.jnu.capstone.entity.ChatJoin;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
public interface ChatJoinRepository extends JpaRepository<ChatJoin, Integer> {
    List<ChatJoin> findByUserId(int userId);
}
