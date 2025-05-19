package com.jnu.capstone.entity;

import java.io.Serializable;
import java.util.Objects;

public class ChatJoinId implements Serializable {
    private int user_id;
    private int chatting_room_id;

    // 기본 생성자
    public ChatJoinId() {}

    // 생성자
    public ChatJoinId(int user_id, int chatting_room_id) {
        this.user_id = user_id;
        this.chatting_room_id = chatting_room_id;
    }

    // Getter and Setter
    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public int getChatting_room_id() {
        return chatting_room_id;
    }

    public void setChatting_room_id(int chatting_room_id) {
        this.chatting_room_id = chatting_room_id;
    }

    // equals()와 hashCode() 구현
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ChatJoinId that = (ChatJoinId) o;
        return user_id == that.user_id && chatting_room_id == that.chatting_room_id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(user_id, chatting_room_id);
    }
}
