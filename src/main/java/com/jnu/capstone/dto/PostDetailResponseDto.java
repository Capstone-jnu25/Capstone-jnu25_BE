package com.jnu.capstone.dto;

import java.time.LocalDate;

// 게시글 상세 조회 DTO
public class PostDetailResponseDto {
    private int postId;
    private String title;
    private String contents;
    private String place;
    private String time;
    private LocalDate dueDate;
    private String gender;
    private int maxParticipants;
    private int currentParticipants;
    private String boardType;

    // 기본 생성자
    public PostDetailResponseDto() {}

    // 전체 필드 초기화
    public PostDetailResponseDto(int postId, String title, String contents, String place, String time, LocalDate dueDate, String gender, int maxParticipants, int currentParticipants, String boardType) {
        this.postId = postId;
        this.title = title;
        this.contents = contents;
        this.place = place;
        this.time = time;
        this.dueDate = dueDate;
        this.gender = gender;
        this.maxParticipants = maxParticipants;
        this.currentParticipants = currentParticipants;
        this.boardType = boardType;
    }

    // Getters and Setters
    public int getPostId() { return postId; }
    public void setPostId(int postId) { this.postId = postId; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getContents() { return contents; }
    public void setContents(String contents) { this.contents = contents; }

    public String getPlace() { return place; }
    public void setPlace(String place) { this.place = place; }

    public String getTime() { return time; }
    public void setTime(String time) { this.time = time; }

    public LocalDate getDueDate() { return dueDate; }
    public void setDueDate(LocalDate dueDate) { this.dueDate = dueDate; }

    public String getGender() { return gender; }
    public void setGender(String gender) { this.gender = gender; }

    public int getMaxParticipants() { return maxParticipants; }
    public void setMaxParticipants(int maxParticipants) { this.maxParticipants = maxParticipants; }

    public int getCurrentParticipants() { return currentParticipants; }
    public void setCurrentParticipants(int currentParticipants) { this.currentParticipants = currentParticipants; }

    public String getBoardType() { return boardType; }
    public void setBoardType(String boardType) { this.boardType = boardType; }
}
