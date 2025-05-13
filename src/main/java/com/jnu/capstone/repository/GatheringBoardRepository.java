package com.jnu.capstone.repository;

import com.jnu.capstone.entity.GatheringBoard;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GatheringBoardRepository extends JpaRepository<GatheringBoard, Integer> {
}

