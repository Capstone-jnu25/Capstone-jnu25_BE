package com.jnu.capstone.dto;

import java.time.LocalDate;

// 게시글 목록 조회 DTO
public class PostListResponseDto {
    private int postId;
    private String title;
    private String place;
    private String gender;
    private int maxParticipants;
    private int currentParticipants;
    private LocalDate dueDate;
    private String boardType;
    private boolean isClosed;

    // Constructor, Getters, and Setters
    public PostListResponseDto(int postId, String title, String place, String gender, int maxParticipants, int currentParticipants, LocalDate dueDate, String boardType, boolean isClosed) {
        this.postId = postId;
        this.title = title;
        this.place = place;
        this.gender = gender;
        this.maxParticipants = maxParticipants;
        this.currentParticipants = currentParticipants;
        this.dueDate = dueDate;
        this.boardType = boardType;
        this.isClosed = isClosed;
    }

    public int getPostId() { return postId; }
    public void setPostId(int postId) { this.postId = postId; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getPlace() { return place; }
    public void setPlace(String place) { this.place = place; }
    public String getGender() { return gender; }
    public void setGender(String gender) { this.gender = gender; }
    public int getMaxParticipants() { return maxParticipants; }
    public void setMaxParticipants(int maxParticipants) { this.maxParticipants = maxParticipants; }
    public int getCurrentParticipants() { return currentParticipants; }
    public void setCurrentParticipants(int currentParticipants) { this.currentParticipants = currentParticipants; }
    public LocalDate getDueDate() { return dueDate; }
    public void setDueDate(LocalDate dueDate) { this.dueDate = dueDate; }
    public String getBoardType() { return boardType; }
    public void setBoardType(String boardType) { this.boardType = boardType; }
    public boolean isClosed() { return isClosed; }
    public void setClosed(boolean closed) { isClosed = closed; }
}