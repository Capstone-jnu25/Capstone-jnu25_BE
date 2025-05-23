package com.jnu.capstone.dto;

import java.util.List;

public class ChatMessagesPageResponseDto {
    private int chattingRoomId;
    private String chatTitle;
    private boolean last;
    private int totalPages;
    private long totalElements;
    private int pageSize;
    private int pageNumber;
    private String status;
    private List<MessageResponseDto> data;

    public ChatMessagesPageResponseDto(int chattingRoomId, String chatTitle,
                                       boolean last, int totalPages, long totalElements,
                                       int pageSize, int pageNumber, String status,
                                       List<MessageResponseDto> data) {
        this.chattingRoomId = chattingRoomId;
        this.chatTitle = chatTitle;
        this.last = last;
        this.totalPages = totalPages;
        this.totalElements = totalElements;
        this.pageSize = pageSize;
        this.pageNumber = pageNumber;
        this.status = status;
        this.data = data;
    }
    public int getChattingRoomId() {
        return chattingRoomId;
    }

    public String getChatTitle() {
        return chatTitle;
    }

    public boolean isLast() {
        return last;
    }

    public int getTotalPages() {
        return totalPages;
    }

    public long getTotalElements() {
        return totalElements;
    }

    public int getPageSize() {
        return pageSize;
    }

    public int getPageNumber() {
        return pageNumber;
    }

    public String getStatus() {
        return status;
    }

    public List<MessageResponseDto> getData() {
        return data;
    }

    // 생성자 생략
}

