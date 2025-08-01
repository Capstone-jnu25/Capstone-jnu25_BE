package com.jnu.capstone.entity;

import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "post")
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int postId;

    @Column(nullable = false, length = 40)
    private String title;

    @Column(nullable = false, length = 1000, columnDefinition = "VARCHAR(1000) DEFAULT ''")
    private String contents = "";

    @Column(nullable = false)
    private boolean isDeleted = false;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private BoardType boardType;
    // User와의 관계 설정
    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "campus_id", referencedColumnName = "campus_id", nullable = false)
    private School campus;

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Applicant> applicants;

    @OneToOne(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
    private GatheringBoard gatheringBoard;

    @OneToOne(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private LostBoard lostBoard;

    @OneToOne(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private SecondhandBoard secondhandBoard;

    // Getters and Setters
    public int getPostId() { return postId; }
    public void setPostId(int postId) { this.postId = postId; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getContents() { return contents; }
    public void setContents(String contents) { this.contents = contents; }

    public BoardType getBoardType() { return boardType; }
    public void setBoardType(BoardType boardType) { this.boardType = boardType; }

    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }

    public School getCampus() { return campus; }
    public void setCampus(School campus) { this.campus = campus; }
    public boolean isDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(boolean isDeleted) {
        this.isDeleted = isDeleted;
    }

    public List<Applicant> getApplicants() { return applicants; }
    public void setApplicants(List<Applicant> applicants) { this.applicants = applicants; }
    public GatheringBoard getGatheringBoard() { return gatheringBoard; }
    public void setGatheringBoard(GatheringBoard gatheringBoard) {
        this.gatheringBoard = gatheringBoard;
        gatheringBoard.setPost(this); // 양방향 연관관계 설정
    }

    public LostBoard getLostBoard() {
        return lostBoard;
    }

    public void setLostBoard(LostBoard lostBoard) {
        this.lostBoard = lostBoard;
        if (lostBoard != null) {
            lostBoard.setPost(this);
        }
    }

    public SecondhandBoard getSecondhandBoard() {
        return secondhandBoard;
    }

    public void setSecondhandBoard(SecondhandBoard secondhandBoard) {
        this.secondhandBoard = secondhandBoard;
        if (secondhandBoard != null) {
            secondhandBoard.setPost(this);
        }
    }

}
