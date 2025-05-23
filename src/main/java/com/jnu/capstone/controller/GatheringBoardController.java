package com.jnu.capstone.controller;

import com.jnu.capstone.entity.GatheringBoard;
import com.jnu.capstone.service.GatheringBoardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/gatherings")
public class GatheringBoardController {

    private final GatheringBoardService gatheringBoardService;

    @Autowired
    public GatheringBoardController(GatheringBoardService gatheringBoardService) {
        this.gatheringBoardService = gatheringBoardService;
    }

    // 모든 그룹 게시판 조회
    @GetMapping
    public ResponseEntity<List<GatheringBoard>> getAllGatheringBoards() {
        List<GatheringBoard> gatheringBoards = gatheringBoardService.getAllGatheringBoards();
        return new ResponseEntity<>(gatheringBoards, HttpStatus.OK);
    }

    // 특정 그룹 게시판 조회
    @GetMapping("/{postId}")
    public ResponseEntity<GatheringBoard> getGatheringBoardById(@PathVariable int postId) {
        Optional<GatheringBoard> gatheringBoard = gatheringBoardService.getGatheringBoardById(postId);
        return gatheringBoard.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    // 그룹 게시판 생성
    @PostMapping
    public ResponseEntity<GatheringBoard> createGatheringBoard(@RequestBody GatheringBoard gatheringBoard) {
        GatheringBoard createdBoard = gatheringBoardService.createGatheringBoard(gatheringBoard);
        return new ResponseEntity<>(createdBoard, HttpStatus.CREATED);
    }

    // 그룹 게시판 수정
    @PutMapping("/{postId}")
    public ResponseEntity<GatheringBoard> updateGatheringBoard(@PathVariable int postId, @RequestBody GatheringBoard updatedBoard) {
        try {
            GatheringBoard board = gatheringBoardService.updateGatheringBoard(postId, updatedBoard);
            return new ResponseEntity<>(board, HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    // 그룹 게시판 삭제
    @DeleteMapping("/{postId}")
    public ResponseEntity<Void> deleteGatheringBoard(@PathVariable int postId) {
        gatheringBoardService.deleteGatheringBoard(postId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
