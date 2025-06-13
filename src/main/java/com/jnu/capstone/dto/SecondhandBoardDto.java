package com.jnu.capstone.dto;

import java.util.Date;

public class SecondhandBoardDto {
    private int postId;
    private int userId;
    private String title;
    private String nickname;
    private String place;
    private Date writeTime;
    private String photo;
    private int price;
    private String relativeTime;
    private String contents;  // 추가

    // Getters and Setters
    public int getPostId() { return postId; }
    public void setPostId(int postId) { this.postId = postId; }

    public String getTitle(){ return title; }
    public void setTitle(String title) { this.title = title; }

    public int getUserId(){ return userId; }
    public void setUserId(int userId) { this.userId = userId; }

    public String getNickname() { return nickname; }
    public void setNickname(String nickname) { this.nickname = nickname; }

    public String getPlace() { return place; }
    public void setPlace(String place) { this.place = place; }

    public String getContents() { return contents; }
    public void setContents(String contents) { this.contents = contents; }

    public Date getWriteTime() { return writeTime; }
    public void setWriteTime(Date writeTime) { this.writeTime = writeTime; }

    public String getPhoto() { return photo; }
    public void setPhoto(String photo) { this.photo = photo; }

    public int getPrice() { return price; }
    public void setPrice(int price) { this.price = price; }

    public String getRelativeTime() { return relativeTime; }
    public void setRelativeTime(String relativeTime) { this.relativeTime = relativeTime; }
}
