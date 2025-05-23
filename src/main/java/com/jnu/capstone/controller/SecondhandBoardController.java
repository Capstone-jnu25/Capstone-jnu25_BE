package com.jnu.capstone.controller;

import com.jnu.capstone.dto.SecondhandBoardCreateRequestDto;
import com.jnu.capstone.dto.SecondhandBoardDto;
import com.jnu.capstone.service.SecondhandBoardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/secondhand")
public class SecondhandBoardController {

    @Autowired
    private SecondhandBoardService secondhandBoardService;

    // 게시글 등록
    @PostMapping
    public String createBoard(@RequestBody SecondhandBoardCreateRequestDto dto) {
        secondhandBoardService.createSecondhandBoard(dto);
        return "중고거래 게시글이 등록되었습니다.";
    }

    // 전체 목록 조회
    @GetMapping
    public List<SecondhandBoardDto> getAllBoards() {
        return secondhandBoardService.getAllBoards();
    }

    // 게시글 상세 조회
    @GetMapping("/{postId}")
    public SecondhandBoardDto getBoardById(@PathVariable int postId) {
        return secondhandBoardService.getBoardByPostId(postId);
    }
}


//package com.jnu.capstone.controller;
//
//import com.jnu.capstone.entity.SecondhandBoard;
//import com.jnu.capstone.dto.SecondhandBoardCreateRequestDto;
//import com.jnu.capstone.repository.SecondhandBoardRepository;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.web.bind.annotation.*;
//
//import java.util.List;
//
//@RestController
//@RequestMapping("/api/secondhandboards")
//public class SecondhandBoardController {
//
//    @Autowired
//    private SecondhandBoardRepository secondhandBoardRepository;
//
//    @GetMapping
//    public List<SecondhandBoard> getAllSecondhandBoards() {
//        return secondhandBoardRepository.findAll();
//    }
//
//    @PostMapping
//    public SecondhandBoard createSecondhandBoard(@RequestBody SecondhandBoardCreateRequestDto requestDto) {
//        SecondhandBoard secondhandBoard = new SecondhandBoard();
//        secondhandBoard.setPlace(requestDto.getPlace());
//        secondhandBoard.setWriteTime(requestDto.getWriteTime());
//        secondhandBoard.setPhoto(requestDto.getPhoto());
//        secondhandBoard.setPrice(requestDto.getPrice());
//        return secondhandBoardRepository.save(secondhandBoard);
//    }
//
//    @DeleteMapping("/{id}")
//    public void deleteSecondhandBoard(@PathVariable int id) {
//        secondhandBoardRepository.deleteById(id);
//    }
//}
