package com.jnu.capstone.repository;

import com.jnu.capstone.entity.BoardType;
import com.jnu.capstone.entity.GatheringBoard;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GatheringBoardRepository extends JpaRepository<GatheringBoard, Integer> {
    Page<GatheringBoard> findByBoardTypeAndPost_Campus_CampusId(BoardType boardType, int campusId, Pageable pageable);
}

