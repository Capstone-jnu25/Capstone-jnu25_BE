package com.jnu.capstone.dto;

import java.util.Date;

public class SecondhandBoardDto {
    private int postId;
    private String place;
    private Date writeTime;
    private String photo;
    private int price;
    private String relativeTime;

    // Getters and Setters
    public int getPostId() { return postId; }
    public void setPostId(int postId) { this.postId = postId; }

    public String getPlace() { return place; }
    public void setPlace(String place) { this.place = place; }

    public Date getWriteTime() { return writeTime; }
    public void setWriteTime(Date writeTime) { this.writeTime = writeTime; }

    public String getPhoto() { return photo; }
    public void setPhoto(String photo) { this.photo = photo; }

    public int getPrice() { return price; }
    public void setPrice(int price) { this.price = price; }

    public String getRelativeTime() { return relativeTime; }
    public void setRelativeTime(String relativeTime) { this.relativeTime = relativeTime; }
}
