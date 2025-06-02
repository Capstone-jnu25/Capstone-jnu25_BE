package com.jnu.capstone.repository;

import com.jnu.capstone.entity.BoardType;
import com.jnu.capstone.entity.Keyword;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface KeywordRepository extends JpaRepository<Keyword, Integer> {
    List<Keyword> findByUser_UserIdAndBoardType(int userId, BoardType boardType);

    // 🔹 전체 키워드 목록 (게시판 타입 상관없이)
    List<Keyword> findByUser_UserId(int userId);
    List<Keyword> findByBoardType(BoardType boardType);
}
