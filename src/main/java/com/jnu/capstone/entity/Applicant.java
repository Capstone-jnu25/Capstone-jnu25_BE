package com.jnu.capstone.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "applicant")
public class Applicant {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int applicantId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id", nullable = false)
    private Post post;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false, length = 255)
    private String applicationText = "";

    @PrePersist
    public void prePersist() {
        if (this.applicationText == null) {
            this.applicationText = "";
        }
    }

    // Getters and Setters
    public int getApplicantId() { return applicantId; }
    public void setApplicantId(int applicantId) { this.applicantId = applicantId; }
    public Post getPost() { return post; }
    public void setPost(Post post) { this.post = post; }
    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }
    public String getApplicationText() { return applicationText; }
    public void setApplicationText(String applicationText) { this.applicationText = applicationText; }
}
