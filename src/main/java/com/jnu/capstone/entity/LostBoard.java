package com.jnu.capstone.entity;

import jakarta.persistence.*;
import java.util.Date;

@Entity
@Table(name = "lost_board")
public class LostBoard {

    @Id
    @Column(name = "post_id")
    private int postId;

    @OneToOne(fetch = FetchType.LAZY)
    @MapsId
    @JoinColumn(name = "post_id")
    private Post post;

    @Column(name = "place", nullable = false, length = 40)
    private String place;


    @Column(name = "write_time", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)  // ✅ 시간까지 저장
    private Date writeTime;
//    @Column(name = "write_time", nullable = false)
//    @Temporal(TemporalType.DATE)
//    private Date writeTime;

    @Column(name = "photo", nullable = false, length = 255)
    private String photo;

    @Column(name = "is_lost", nullable = false)
    private boolean isLost;

//    @Column(name = "lost_latitude", precision = 10, scale = 8)
    @Column(name = "lost_latitude", columnDefinition = "DECIMAL(11,8)")
    private Double lostLatitude;

//    @Column(name = "lost_longitude", precision = 10, scale = 8)
    @Column(name = "lost_longitude", columnDefinition = "DECIMAL(11,8)")
    private Double lostLongitude;

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

    //여기서부터
    private boolean lost;

    public boolean isLost() { return isLost; }
    public void setLost(boolean isLost) { this.isLost = isLost; }
    //여기까지 수정함. 원래코드는 바로 밑에 써두겠음.
    /// public boolean isLost() { return isLost; }
    /// public void setLost(boolean lost) { isLost = lost; }

    public Double getLostLatitude() { return lostLatitude; }
    public void setLostLatitude(Double lostLatitude) { this.lostLatitude = lostLatitude; }

    public Double getLostLongitude() { return lostLongitude; }
    public void setLostLongitude(Double lostLongitude) { this.lostLongitude = lostLongitude; }
}
