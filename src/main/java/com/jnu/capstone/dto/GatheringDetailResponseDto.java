package com.jnu.capstone.dto;

public class GatheringDetailResponseDto {
    private int postId;
    private String title;
    private String dDay;
    private String contents;
    private String place;
    private String gender;
    private String time;
    private boolean isClosed;

    public GatheringDetailResponseDto(int postId, String title, String dDay, String contents, String place, String gender, String time, boolean isClosed) {
        this.postId = postId;
        this.title = title;
        this.dDay = dDay;
        this.contents = contents;
        this.place = place;
        this.gender = gender;
        this.time = time;
        this.isClosed = isClosed;
    }

    // Getters and Setters
    public int getPostId() { return postId; }
    public void setPostId(int postId) { this.postId = postId; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getDDay() { return dDay; }
    public void setDDay(String dDay) { this.dDay = dDay; }

    public String getContents() { return contents; }
    public void setContents(String contents) { this.contents = contents; }

    public String getPlace() { return place; }
    public void setPlace(String place) { this.place = place; }

    public String getGender() { return gender; }
    public void setGender(String gender) { this.gender = gender; }

    public String getTime() { return time; }
    public void setTime(String time) { this.time = time; }

    public boolean isClosed() { return isClosed; }
    public void setClosed(boolean closed) { isClosed = closed; }
}
