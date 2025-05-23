package com.jnu.capstone.repository;

import com.jnu.capstone.entity.SecondhandBoard;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SecondhandBoardRepository extends JpaRepository<SecondhandBoard, Integer> {
}
