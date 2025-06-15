package com.jnu.capstone.dto;

public class LostBoardCreateRequestDto {
    private String place;
    private String photo;
    private boolean lost;
    private Double lostLatitude;
    private Double lostLongitude;
    private String title;     // 게시글 제목
    private String contents;  // 상세 설명 추가

    public LostBoardCreateRequestDto() {}

    // Getters and Setters
    public String getPlace() { return place; }
    public void setPlace(String place) { this.place = place; }

    public String getPhoto() { return photo; }
    public void setPhoto(String photo) { this.photo = photo; }

    public boolean isLost() { return lost; }       // getter는 그대로 isLost
    public void setLost(boolean lost) { this.lost = lost; }

    public Double getLostLatitude() { return lostLatitude; }
    public void setLostLatitude(Double lostLatitude) { this.lostLatitude = lostLatitude; }

    public Double getLostLongitude() { return lostLongitude; }
    public void setLostLongitude(Double lostLongitude) { this.lostLongitude = lostLongitude; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getContents() { return contents; }
    public void setContents(String contents) { this.contents = contents; }
}
