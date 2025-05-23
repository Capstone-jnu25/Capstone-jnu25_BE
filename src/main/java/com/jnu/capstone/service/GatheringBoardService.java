package com.jnu.capstone.service;

import com.jnu.capstone.entity.GatheringBoard;
import com.jnu.capstone.repository.GatheringBoardRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class GatheringBoardService {

    private final GatheringBoardRepository gatheringBoardRepository;

    @Autowired
    public GatheringBoardService(GatheringBoardRepository gatheringBoardRepository) {
        this.gatheringBoardRepository = gatheringBoardRepository;
    }

    // 모든 그룹 게시판 조회
    public List<GatheringBoard> getAllGatheringBoards() {
        return gatheringBoardRepository.findAll();
    }

    // 특정 그룹 게시판 조회
    public Optional<GatheringBoard> getGatheringBoardById(int postId) {
        return gatheringBoardRepository.findById(postId);
    }

    // 그룹 게시판 생성
    public GatheringBoard createGatheringBoard(GatheringBoard gatheringBoard) {
        return gatheringBoardRepository.save(gatheringBoard);
    }

    // 그룹 게시판 수정
    public GatheringBoard updateGatheringBoard(int postId, GatheringBoard updatedBoard) {
        if (gatheringBoardRepository.existsById(postId)) {
            updatedBoard.setPostId(postId);
            return gatheringBoardRepository.save(updatedBoard);
        } else {
            throw new IllegalArgumentException("GatheringBoard not found with ID: " + postId);
        }
    }

    // 그룹 게시판 삭제
    public void deleteGatheringBoard(int postId) {
        gatheringBoardRepository.deleteById(postId);
    }
}