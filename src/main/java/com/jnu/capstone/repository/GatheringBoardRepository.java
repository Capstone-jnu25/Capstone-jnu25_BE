package com.jnu.capstone.repository;

import com.jnu.capstone.entity.BoardType;
import com.jnu.capstone.entity.GatheringBoard;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
@Repository
public interface GatheringBoardRepository extends JpaRepository<GatheringBoard, Integer> {
    // GatheringBoardRepository.java
    Page<GatheringBoard> findByBoardTypeAndPost_Campus_CampusIdAndPost_IsDeletedFalse(
            BoardType boardType, int campusId, Pageable pageable);
    List<GatheringBoard> findByBoardTypeInAndDueDateBefore(List<BoardType> boardTypes, LocalDate dueDate);
}

