package com.jnu.capstone.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "chatroom")
public class Chatroom {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "chatting_room_id")  // 필드명 수정
    private int chattingRoomId;

    @OneToOne
    @JoinColumn(name = "post_id", nullable = false, unique = true)
    private Post post;

    @Column(nullable = false, length = 100)
    private String chatTitle;

    // Getters and Setters
    public int getChattingRoomId() {
        return chattingRoomId;
    }

    public void setChattingRoomId(int chattingRoomId) {
        this.chattingRoomId = chattingRoomId;
    }

    public Post getPost() {
        return post;
    }

    public void setPost(Post post) {
        this.post = post;
    }

    public String getChatTitle() {
        return chatTitle;
    }

    public void setChatTitle(String chatTitle) {
        this.chatTitle = chatTitle;
    }
}

