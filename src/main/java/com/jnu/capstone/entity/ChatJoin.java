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
    private int userId;

    @Id
    @Column(name = "chatting_room_id")
    private int chattingRoomId;

    @ManyToOne(fetch = FetchType.EAGER)  // Lazy -> Eager로 수정
    @JoinColumn(name = "user_id", insertable = false, updatable = false, nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.EAGER)  // Lazy -> Eager로 수정
    @JoinColumn(name = "chatting_room_id", insertable = false, updatable = false, nullable = false)
    private Chatroom chatroom;

    // 기본 생성자
    public ChatJoin() {}

    // 생성자
    public ChatJoin(int userId, int chattingRoomId) {
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

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
        this.userId = user.getUserId();  // 연결된 User의 ID 설정
    }

    public Chatroom getChatroom() {
        return chatroom;
    }

    public void setChatroom(Chatroom chatroom) {
        this.chatroom = chatroom;
        this.chattingRoomId = chatroom.getChattingRoomId();  // 연결된 Chatroom의 ID 설정
    }

    // equals()와 hashCode() 구현
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ChatJoin that = (ChatJoin) o;
        return userId == that.userId && chattingRoomId == that.chattingRoomId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId, chattingRoomId);
    }
}
