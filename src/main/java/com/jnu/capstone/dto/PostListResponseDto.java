package com.jnu.capstone.dto;

import java.time.LocalDate;
import java.time.Period;

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
    private long dDay;  // 추가 필드

    // 기본 생성자
    public PostListResponseDto() {}

    // 전체 필드 초기화
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
        this.dDay = calculateDDay(dueDate);
    }

    // 추가 생성자 - 마감 여부 계산 포함
    public PostListResponseDto(int postId, String title, String place, String gender, int maxParticipants, int currentParticipants, LocalDate dueDate, String boardType) {
        this(postId, title, place, gender, maxParticipants, currentParticipants, dueDate, boardType, currentParticipants >= maxParticipants || dueDate.isBefore(LocalDate.now()));
    }

    // D-Day 계산 메서드
    private long calculateDDay(LocalDate dueDate) {
        LocalDate today = LocalDate.now();
        return today.until(dueDate).getDays();
    }

    // Getters and Setters
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
    public void setDueDate(LocalDate dueDate) {
        this.dueDate = dueDate;
        this.dDay = calculateDDay(dueDate);  // dDay 필드 업데이트
    }

    public String getBoardType() { return boardType; }
    public void setBoardType(String boardType) { this.boardType = boardType; }

    public boolean isClosed() { return isClosed; }
    public void setClosed(boolean closed) { this.isClosed = closed; }

    public long getDDay() { return dDay; }
    public void setDDay(long dDay) { this.dDay = dDay; }
}
