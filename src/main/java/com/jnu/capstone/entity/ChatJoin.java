package com.jnu.capstone.entity;

import jakarta.persistence.*;
import java.io.Serializable;
import java.util.Objects;

@Entity
@Table(name = "chat_join")
@IdClass(ChatJoinId.class)
public class ChatJoin implements Serializable {

    @Id
    @Column(name = "user_id")
    private int user_id;

    @Id
    @Column(name = "chatting_room_id")  // 필드명 수정
    private int chatting_room_id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", insertable = false, updatable = false, nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "chatting_room_id", insertable = false, updatable = false, nullable = false)
    private Chatroom chatroom;

    // 기본 생성자
    public ChatJoin() {}

    // 생성자
    public ChatJoin(int user_id, int chatting_room_id) {
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

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Chatroom getChatroom() {
        return chatroom;
    }

    public void setChatroom(Chatroom chatroom) {
        this.chatroom = chatroom;
    }

    // equals()와 hashCode() 구현
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ChatJoin that = (ChatJoin) o;
        return user_id == that.user_id && chatting_room_id == that.chatting_room_id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(user_id, chatting_room_id);
    }
}

