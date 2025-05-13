package com.jnu.capstone.entity;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "gathering_board")
public class GatheringBoard {
    @Id
    private int postId;

    @Column(nullable = false, length = 20)
    private String place;

    @Column(nullable = false, length = 20)
    private String meetTime;

    @Column(nullable = false)
    private LocalDate dueDate;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private GenderType gender;

    @Column(nullable = false)
    private int maxParticipants;

    @Column(nullable = false)
    private boolean automatic;

    @Column(nullable = false, columnDefinition = "INT DEFAULT 0")
    private int currentParticipants = 0;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private BoardType boardType;

    @OneToOne
    @JoinColumn(name = "post_id", nullable = false)
    private Post post;

    // Getters and Setters
    public int getPostId() { return postId; }
    public void setPostId(int postId) { this.postId = postId; }
    public String getPlace() { return place; }
    public void setPlace(String place) { this.place = place; }
    public String getMeetTime() { return meetTime; }
    public void setMeetTime(String meetTime) { this.meetTime = meetTime; }
    public LocalDate getDueDate() { return dueDate; }
    public void setDueDate(LocalDate dueDate) { this.dueDate = dueDate; }
    public GenderType getGender() { return gender; }
    public void setGender(GenderType gender) { this.gender = gender; }
    public int getMaxParticipants() { return maxParticipants; }
    public void setMaxParticipants(int maxParticipants) { this.maxParticipants = maxParticipants; }
    public boolean isAutomatic() { return automatic; }
    public void setAutomatic(boolean automatic) { this.automatic = automatic; }
    public int getCurrentParticipants() { return currentParticipants; }
    public void setCurrentParticipants(int currentParticipants) { this.currentParticipants = currentParticipants; }
    public BoardType getBoardType() { return boardType; }
    public void setBoardType(BoardType boardType) { this.boardType = boardType; }
    public Post getPost() { return post; }
    public void setPost(Post post) { this.post = post; }
}
