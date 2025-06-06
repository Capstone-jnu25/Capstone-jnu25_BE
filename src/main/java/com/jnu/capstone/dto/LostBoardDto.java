package com.jnu.capstone.dto;

import java.util.Date;

public class LostBoardDto {
    private int postId;
    private String title;
    private String nickname; // 작성자 닉네임
    private String place;
    private String contents;  // 추가
    private Date writeTime;
    private String photo;
    private boolean isLost;
    private Double lostLatitude;
    private Double lostLongitude;
    private String relativeTime;// "몇 분 전", "1시간 전" 등


    // Getters and Setters
    public String getRelativeTime() { return relativeTime; }
    public void setRelativeTime(String relativeTime) { this.relativeTime = relativeTime; }

    public String getTitle(){ return title; }
    public void setTitle(String title) { this.title = title; }

    public String getNickname(){ return nickname; }
    public void setNickname(String nickname) { this.nickname = nickname; }

    public int getPostId() { return postId; }
    public void setPostId(int postId) { this.postId = postId; }

    public String getPlace() { return place; }
    public void setPlace(String place) { this.place = place; }

    public String getContents() { return contents; }
    public void setContents(String contents) { this.contents = contents; }

    public Date getWriteTime() { return writeTime; }
    public void setWriteTime(Date writeTime) { this.writeTime = writeTime; }

    public String getPhoto() { return photo; }
    public void setPhoto(String photo) { this.photo = photo; }

    public boolean isLost() { return isLost; }
    public void setLost(boolean lost) { isLost = lost; }

    public Double getLostLatitude() { return lostLatitude; }
    public void setLostLatitude(Double lostLatitude) { this.lostLatitude = lostLatitude; }

    public Double getLostLongitude() { return lostLongitude; }
    public void setLostLongitude(Double lostLongitude) { this.lostLongitude = lostLongitude; }
}
