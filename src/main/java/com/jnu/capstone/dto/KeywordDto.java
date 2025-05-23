package com.jnu.capstone.dto;

import com.jnu.capstone.entity.BoardType;

public class KeywordDto {
    private int id;
    private int userId;
    private String keywordText;
    private BoardType boardType;

    // Getters and Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }

    public String getKeywordText() { return keywordText; }
    public void setKeywordText(String keywordText) { this.keywordText = keywordText; }

    public BoardType getBoardType() { return boardType; }
    public void setBoardType(BoardType boardType) { this.boardType = boardType; }
}
