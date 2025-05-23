package com.jnu.capstone.dto;

import java.util.Date;

public class LostBoardCreateRequestDto {
    private int postId;
    private String place;
    private Date writeTime;
    private String photo;
    private boolean isLost;
    private Double lostLatitude;
    private Double lostLongitude;

    // Getters and Setters
    public int getPostId() { return postId; }
    public void setPostId(int postId) { this.postId = postId; }

    public String getPlace() { return place; }
    public void setPlace(String place) { this.place = place; }

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
