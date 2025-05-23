package com.jnu.capstone.dto;

public class ChatroomResponseDto {
    private int chattingRoomId;
    private String chatTitle;
    private String lastMessage;
    private String boardType;

    public ChatroomResponseDto(int chattingRoomId, String chatTitle, String lastMessage, String boardType) {
        this.chattingRoomId = chattingRoomId;
        this.chatTitle = chatTitle;
        this.lastMessage = lastMessage;
        this.boardType = boardType;
    }

    public int getChattingRoomId() {
        return chattingRoomId;
    }

    public void setChattingRoomId(int chattingRoomId) {
        this.chattingRoomId = chattingRoomId;
    }

    public String getChatTitle() {
        return chatTitle;
    }

    public void setChatTitle(String chatTitle) {
        this.chatTitle = chatTitle;
    }

    public String getLastMessage() {
        return lastMessage;
    }

    public void setLastMessage(String lastMessage) {
        this.lastMessage = lastMessage;
    }

    public String getBoardType() {
        return boardType;
    }

    public void setBoardType(String boardType) {
        this.boardType = boardType;
    }
}
