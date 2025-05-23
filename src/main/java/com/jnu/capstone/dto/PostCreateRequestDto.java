package com.jnu.capstone.dto;

import java.time.LocalDate;

public class PostCreateRequestDto {
    private String title;
    private String time;
    private String place;
    private String contents;
    private LocalDate dueDate;
    private String gender;
    private int maxParticipants;
    private boolean automatic;
    private String boardType;

    public PostCreateRequestDto() {}
    // Getters and Setters
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getTime() { return time; }
    public void setTime(String time) { this.time = time; }

    public String getPlace() { return place; }
    public void setPlace(String place) { this.place = place; }

    public String getContents() { return contents; }
    public void setContents(String contents) { this.contents = contents; }

    public LocalDate getDueDate() { return dueDate; }
    public void setDueDate(LocalDate dueDate) { this.dueDate = dueDate; }

    public String getGender() { return gender; }
    public void setGender(String gender) { this.gender = gender; }

    public int getMaxParticipants() { return maxParticipants; }
    public void setMaxParticipants(int maxParticipants) { this.maxParticipants = maxParticipants; }

    public boolean isAutomatic() { return automatic; }
    public void setAutomatic(boolean automatic) { this.automatic = automatic; }

    public String getBoardType() { return boardType; }
    public void setBoardType(String boardType) { this.boardType = boardType; }
}
