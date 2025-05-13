package com.jnu.capstone.dto;

import java.time.LocalDate;
import java.util.List;

// 신청자 목록 조회 DTO
public class ApplicantResponseDto {
    private int applicantId;
    private int userId;
    private String applicationText;
    private boolean isAccepted;

    // Constructor, Getters, and Setters
    public ApplicantResponseDto(int applicantId, int userId, String applicationText, boolean isAccepted) {
        this.applicantId = applicantId;
        this.userId = userId;
        this.applicationText = applicationText;
        this.isAccepted = isAccepted;
    }

    public int getApplicantId() { return applicantId; }
    public void setApplicantId(int applicantId) { this.applicantId = applicantId; }
    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }
    public String getApplicationText() { return applicationText; }
    public void setApplicationText(String applicationText) { this.applicationText = applicationText; }
    public boolean isAccepted() { return isAccepted; }
    public void setAccepted(boolean accepted) { isAccepted = accepted; }
}