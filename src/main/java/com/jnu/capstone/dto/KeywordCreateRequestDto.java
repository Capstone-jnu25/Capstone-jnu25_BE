package com.jnu.capstone.dto;

import com.jnu.capstone.entity.BoardType;

public class KeywordCreateRequestDto {
    private int userId;
    private String keywordId;
    private BoardType boardType;

    // Getters and Setters
    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }

    public String getKeywordText() { return keywordId; }
    public void setKeywordText(String keywordText) { this.keywordId = keywordText; }

    public BoardType getBoardType() { return boardType; }
    public void setBoardType(BoardType boardType) { this.boardType = boardType; }
}
