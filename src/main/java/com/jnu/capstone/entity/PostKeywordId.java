package com.jnu.capstone.entity;

import java.io.Serializable;
import java.util.Objects;

public class PostKeywordId implements Serializable {
    private String keywordText;
    private int postId;

    // 기본 생성자
    public PostKeywordId() {}

    // 생성자
    public PostKeywordId(String keywordText, int postId) {
        this.keywordText = keywordText;
        this.postId = postId;
    }

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

    // equals() and hashCode() 구현 (필수)
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PostKeywordId that = (PostKeywordId) o;
        return postId == that.postId && Objects.equals(keywordText, that.keywordText);
    }

    @Override
    public int hashCode() {
        return Objects.hash(keywordText, postId);
    }
}
