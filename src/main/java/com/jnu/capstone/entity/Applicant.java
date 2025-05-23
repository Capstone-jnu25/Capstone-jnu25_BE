package com.jnu.capstone.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "applicant")
public class Applicant {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int applicantId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id", nullable = false)
    private Post post;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "application_text", nullable = false, length = 255)
    private String applicationText = "";

    @Column(name = "is_accepted", nullable = false)
    private boolean isAccepted = false;

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

    public int getUserId() { return user.getUserId(); }

    public String getApplicationText() { return applicationText; }
    public void setApplicationText(String applicationText) { this.applicationText = applicationText; }

    public boolean isAccepted() { return isAccepted; }
    public void setAccepted(boolean isAccepted) { this.isAccepted = isAccepted; }

    @Override
    public String toString() {
        return "Applicant{" +
                "applicantId=" + applicantId +
                ", post=" + (post != null ? post.getPostId() : null) +
                ", user=" + (user != null ? user.getUserId() : null) +
                ", applicationText='" + applicationText + '\'' +
                ", isAccepted=" + isAccepted +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Applicant applicant = (Applicant) o;
        return applicantId == applicant.applicantId;
    }

    @Override
    public int hashCode() {
        return Integer.hashCode(applicantId);
    }


//    public int getApplicantId() { return applicantId; }
//    public void setApplicantId(int applicantId) { this.applicantId = applicantId; }
//    public Post getPost() { return post; }
//    public void setPost(Post post) { this.post = post; }
//    public User getUser() { return user; }
//    public void setUser(User user) { this.user = user; }
//    public String getApplicationText() { return applicationText; }
//    public void setApplicationText(String applicationText) { this.applicationText = applicationText; }
}