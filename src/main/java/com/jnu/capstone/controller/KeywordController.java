package com.jnu.capstone.controller;

import com.jnu.capstone.dto.KeywordCreateRequestDto;
import com.jnu.capstone.dto.KeywordDto;
import com.jnu.capstone.service.KeywordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.jnu.capstone.util.JwtTokenProvider;

import java.util.List;

@RestController
@RequestMapping("/api/keywords")
public class KeywordController {

    @Autowired
    private KeywordService keywordService;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    // ✅ 키워드 등록
    @PostMapping
    public ResponseEntity<Void> createKeyword(
            @RequestHeader("Authorization") String token,
            @RequestBody KeywordCreateRequestDto requestDto) {

        String jwt = token.replace("Bearer ", "");
        int userId = jwtTokenProvider.getUserIdFromToken(jwt);

        keywordService.createKeyword(userId, requestDto);
        return ResponseEntity.ok().build();
    }

    // ✅ 특정 게시판의 키워드 목록 조회
    @GetMapping("/{boardType}")
    public ResponseEntity<List<String>> getKeywordsByBoardType(
            @RequestHeader("Authorization") String token,
            @PathVariable String boardType) {

        String jwt = token.replace("Bearer ", "");
        int userId = jwtTokenProvider.getUserIdFromToken(jwt);

        List<String> keywords = keywordService.getKeywordsByBoardType(userId, boardType);
        return ResponseEntity.ok(keywords);
    }

    //전체 키워드 불러오기(게시판 타입도 함께)
    @GetMapping
    public ResponseEntity<List<KeywordDto>> getAllKeywords(
            @RequestHeader("Authorization") String token) {

        String jwt = token.replace("Bearer ", "");
        int userId = jwtTokenProvider.getUserIdFromToken(jwt);

        List<KeywordDto> keywords = keywordService.getAllKeywordsWithBoardType(userId);
        return ResponseEntity.ok(keywords);
    }


    // ✅ 키워드 삭제 (본인만 가능)
    @DeleteMapping("/{keywordId}")
    public ResponseEntity<Void> deleteKeyword(
            @RequestHeader("Authorization") String token,
            @PathVariable int keywordId) {

        String jwt = token.replace("Bearer ", "");
        int userId = jwtTokenProvider.getUserIdFromToken(jwt);

        keywordService.deleteKeyword(userId, keywordId);
        return ResponseEntity.noContent().build();
    }
}

//@RestController
//@RequestMapping("/api/keywords")
//public class KeywordController {
//
//    @Autowired
//    private KeywordRepository keywordRepository;
//
//    @GetMapping
//    public List<Keyword> getAllKeywords() {
//        return keywordRepository.findAll();
//    }
//
//    @PostMapping
//    public Keyword createKeyword(@RequestBody KeywordCreateRequestDto requestDto) {
//        Keyword keyword = new Keyword();
//        keyword.setKeywordText(requestDto.getKeywordText());
//        keyword.setBoardType(requestDto.getBoardType());
//        return keywordRepository.save(keyword);
//    }
//
//    @DeleteMapping("/{id}")
//    public void deleteKeyword(@PathVariable int id) {
//        keywordRepository.deleteById(id);
//    }
//}
