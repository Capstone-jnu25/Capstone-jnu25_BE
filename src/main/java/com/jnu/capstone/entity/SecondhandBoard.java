package com.jnu.capstone.entity;

import jakarta.persistence.*;

import java.util.Date;

// SecondhandBoard Entity
@Entity
@Table(name = "secondhand_board")
public class SecondhandBoard {
    @Id
    @Column(name = "post_id")
    private int postId;

    @OneToOne(fetch = FetchType.LAZY)
    @MapsId
    @JoinColumn(name = "post_id") // FK + PK
    private Post post;

    @Column(name = "place", nullable = false, length = 40)
    private String place;

    @Column(name = "write_time", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)  // ✅ 시간까지 저장
    private Date writeTime;

    @Column(name = "photo", nullable = false, length = 255)
    private String photo;

    @Column(name = "price", nullable = false)
    private int price;

    // Getters and Setters
    public int getPostId() { return postId; }
    public void setPostId(int postId) { this.postId = postId; }
    public Post getPost() { return post; }
    public void setPost(Post post) { this.post = post; }
    public String getPlace() { return place; }
    public void setPlace(String place) { this.place = place; }
    public Date getWriteTime() { return writeTime; }
    public void setWriteTime(Date writeTime) { this.writeTime = writeTime; }
    public String getPhoto() { return photo; }
    public void setPhoto(String photo) { this.photo = photo; }
    public int getPrice() { return price; }
    public void setPrice(int price) { this.price = price; }
}
