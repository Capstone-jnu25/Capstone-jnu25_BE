package com.jnu.capstone.dto;

import java.time.LocalDate;

// 게시글 통합 응답 DTO
public class PostResponseDto {
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
    private boolean isClosed;
    private long dDay;

    // 기본 생성자
    public PostResponseDto() {}

    // 전체 필드 초기화 (dDay 포함)
    public PostResponseDto(int postId, String title, String contents, String place, String time, LocalDate dueDate, String gender, int maxParticipants, int currentParticipants, String boardType, boolean isClosed, long dDay) {
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
        this.isClosed = isClosed;
        this.dDay = dDay;
    }

    // D-Day 계산 메서드
    private long calculateDDay(LocalDate dueDate) {
        LocalDate today = LocalDate.now();
        return Math.max(0, today.until(dueDate).getDays());
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
    public void setDueDate(LocalDate dueDate) {
        this.dueDate = dueDate;
        this.dDay = calculateDDay(dueDate);
    }

    public String getGender() { return gender; }
    public void setGender(String gender) { this.gender = gender; }

    public int getMaxParticipants() { return maxParticipants; }
    public void setMaxParticipants(int maxParticipants) { this.maxParticipants = maxParticipants; }

    public int getCurrentParticipants() { return currentParticipants; }
    public void setCurrentParticipants(int currentParticipants) { this.currentParticipants = currentParticipants; }

    public String getBoardType() { return boardType; }
    public void setBoardType(String boardType) { this.boardType = boardType; }

    public boolean isClosed() { return isClosed; }
    public void setClosed(boolean closed) { this.isClosed = closed; }

    public long getDDay() { return dDay; }
    public void setDDay(long dDay) { this.dDay = dDay; }
}
