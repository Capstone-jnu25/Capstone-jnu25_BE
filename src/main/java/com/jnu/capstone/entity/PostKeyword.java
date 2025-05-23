package com.jnu.capstone.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "post_keyword")
@IdClass(PostKeywordId.class)  // 복합 키 사용
public class PostKeyword {

    @Id
    @Column(name = "keyword_text", nullable = false, length = 50)
    private String keywordText;

    @Id
    @Column(name = "post_id", nullable = false)
    private int postId;

    // Getters and Setters
    public String getKeywordText() {
        return keywordText;
    }

    public void setKeywordText(String keywordText) {
        this.keywordText = keywordText;
    }

    public int getPostId() {
        return postId;
    }

    public void setPostId(int postId) {
        this.postId = postId;
    }
}
