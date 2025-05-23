package com.jnu.capstone.repository;

import com.jnu.capstone.entity.LostBoard;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LostBoardRepository extends JpaRepository<LostBoard, Integer> {

    // 분실물 or 습득물 필터링
    List<LostBoard> findByIsLost(boolean isLost);
}
