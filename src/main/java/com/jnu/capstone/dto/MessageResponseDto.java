package com.jnu.capstone.dto;

import java.time.LocalDateTime;

public class MessageResponseDto {
    private long messageId;
    private int senderId;
    private String detailMessage;
    private LocalDateTime sendTime;

    public MessageResponseDto(long messageId, int senderId, String detailMessage, LocalDateTime sendTime) {
        this.messageId = messageId;
        this.senderId = senderId;
        this.detailMessage = detailMessage;
        this.sendTime = sendTime;
    }

    // Getter and Setter
    public long getMessageId() {
        return messageId;
    }

    public void setMessageId(long messageId) {
        this.messageId = messageId;
    }

    public int getSenderId() {
        return senderId;
    }

    public void setSenderId(int senderId) {
        this.senderId = senderId;
    }

    public String getDetailMessage() {
        return detailMessage;
    }

    public void setDetailMessage(String detailMessage) {
        this.detailMessage = detailMessage;
    }

    public LocalDateTime getSendTime() {
        return sendTime;
    }

    public void setSendTime(LocalDateTime sendTime) {
        this.sendTime = sendTime;
    }
}
