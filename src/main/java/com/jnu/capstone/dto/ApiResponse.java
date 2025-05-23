package com.jnu.capstone.dto;

import org.springframework.data.domain.Page;

// 공통 응답 DTO
public class ApiResponse {
    private String status;
    private Page<PostResponseDto> data;

    public ApiResponse(String status, Page<PostResponseDto> data) {
        this.status = status;
        this.data = data;
    }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public Page<PostResponseDto> getData() { return data; }
    public void setData(Page<PostResponseDto> data) { this.data = data; }
}
