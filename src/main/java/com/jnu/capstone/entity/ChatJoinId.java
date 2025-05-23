package com.jnu.capstone.entity;

import java.io.Serializable;
import java.util.Objects;

public class ChatJoinId implements Serializable {
    private int userId;
    private int chattingRoomId;

    // 기본 생성자
    public ChatJoinId() {}

    // 생성자
    public ChatJoinId(int userId, int chattingRoomId) {
        this.userId = userId;
        this.chattingRoomId = chattingRoomId;
    }

    // Getter and Setter
    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getChattingRoomId() {
        return chattingRoomId;
    }

    public void setChattingRoomId(int chattingRoomId) {
        this.chattingRoomId = chattingRoomId;
    }

    // equals()와 hashCode() 구현
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ChatJoinId that = (ChatJoinId) o;
        return userId == that.userId && chattingRoomId == that.chattingRoomId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId, chattingRoomId);
    }
}
