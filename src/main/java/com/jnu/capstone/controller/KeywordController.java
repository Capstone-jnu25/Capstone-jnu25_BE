package com.jnu.capstone.controller;

import com.jnu.capstone.entity.Keyword;
import com.jnu.capstone.dto.KeywordCreateRequestDto;
import com.jnu.capstone.repository.KeywordRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/keywords")
public class KeywordController {

    @Autowired
    private KeywordRepository keywordRepository;

    @GetMapping
    public List<Keyword> getAllKeywords() {
        return keywordRepository.findAll();
    }

    @PostMapping
    public Keyword createKeyword(@RequestBody KeywordCreateRequestDto requestDto) {
        Keyword keyword = new Keyword();
        keyword.setKeywordText(requestDto.getKeywordText());
        keyword.setBoardType(requestDto.getBoardType());
        return keywordRepository.save(keyword);
    }

    @DeleteMapping("/{id}")
    public void deleteKeyword(@PathVariable int id) {
        keywordRepository.deleteById(id);
    }
}
