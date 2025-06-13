package com.jnu.capstone.dto;

public class SecondhandBoardCreateRequestDto {
//    private int postId;
    private String title;
    private String contents;
    private String place;
    private String photo;
    private int price;
//    private Date writeTime;




    // Getters and Setters
//    public int getPostId() { return postId; }
//    public void setPostId(int postId) { this.postId = postId; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getContents() { return contents; }
    public void setContents(String contents) { this.contents = contents; }

    public String getPlace() { return place; }
    public void setPlace(String place) { this.place = place; }

    public String getPhoto() { return photo; }
    public void setPhoto(String photo) { this.photo = photo; }

    public int getPrice() { return price; }
    public void setPrice(int price) { this.price = price; }
}
