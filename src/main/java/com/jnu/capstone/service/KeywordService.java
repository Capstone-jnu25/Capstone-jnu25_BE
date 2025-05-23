package com.jnu.capstone.service;

import com.jnu.capstone.dto.KeywordCreateRequestDto;

import java.util.List;

public interface KeywordService {

    // 키워드 등록
    void createKeyword(int userId, KeywordCreateRequestDto requestDto);

    // 게시판 종류별 키워드 조회
    List<String> getKeywordsByBoardType(int userId, String boardType);
}
