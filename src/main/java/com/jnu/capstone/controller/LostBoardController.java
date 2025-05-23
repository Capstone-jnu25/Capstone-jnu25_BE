package com.jnu.capstone.controller;

import com.jnu.capstone.entity.LostBoard;
import com.jnu.capstone.dto.LostBoardCreateRequestDto;
import com.jnu.capstone.repository.LostBoardRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.jnu.capstone.dto.LostBoardDto;
import com.jnu.capstone.service.LostBoardService;
import java.util.List;

@RestController
@RequestMapping("/api/lostboards")
public class LostBoardController {

    @Autowired
    private LostBoardService lostBoardService;
//    @Autowired
//    private LostBoardRepository lostBoardRepository;

    // 게시글 생성
    @PostMapping
    public String createLostBoard(@RequestBody LostBoardCreateRequestDto requestDto) {
        lostBoardService.createLostBoard(requestDto);
        return "게시글이 성공적으로 작성되었습니다.";
    }

    // 게시글 목록 조회 (분실/습득 필터링)
    @GetMapping
    public List<LostBoardDto> getAllLostBoards(@RequestParam boolean isLost) {
        return lostBoardService.getLostBoardsByType(isLost);
    }

    // 게시글 상세 조회
    @GetMapping("/{postId}")
    public LostBoardDto getLostBoardById(@PathVariable int postId) {
        return lostBoardService.getLostBoardByPostId(postId);
    }

//    @GetMapping
//    public List<LostBoard> getAllLostBoards() {
//        return lostBoardRepository.findAll();
//    }
//
//    @PostMapping
//    public LostBoard createLostBoard(@RequestBody LostBoardCreateRequestDto requestDto) {
//        LostBoard lostBoard = new LostBoard();
//        lostBoard.setPlace(requestDto.getPlace());
//        lostBoard.setWriteTime(requestDto.getWriteTime());
//        lostBoard.setPhoto(requestDto.getPhoto());
//        lostBoard.setLost(requestDto.isLost());
//        return lostBoardRepository.save(lostBoard);
//    }
//
//    @DeleteMapping("/{id}")
//    public void deleteLostBoard(@PathVariable int id) {
//        lostBoardRepository.deleteById(id);
// }
}
