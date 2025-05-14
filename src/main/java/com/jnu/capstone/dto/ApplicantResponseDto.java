package com.jnu.capstone.dto;

import com.jnu.capstone.entity.Applicant;

// 신청자 목록 조회 DTO
public class ApplicantResponseDto {
    private int applicantId;
    private int userId;
    private String nickname;
    private String applicationText;
    private boolean isAccepted;

    public ApplicantResponseDto() {
        this.applicationText = "";
        this.isAccepted = false;
    }

    public ApplicantResponseDto(int applicantId, int userId, String nickname, String applicationText, boolean isAccepted) {
        this.applicantId = applicantId;
        this.userId = userId;
        this.nickname = nickname;
        this.applicationText = applicationText != null ? applicationText : "";
        this.isAccepted = isAccepted;
    }

    public int getApplicantId() { return applicantId; }
    public void setApplicantId(int applicantId) { this.applicantId = applicantId; }

    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }
    public String getNickname() { return nickname; }
    public void setNickname(String nickname) { this.nickname = nickname; }
    public String getApplicationText() { return applicationText; }
    public void setApplicationText(String applicationText) {
        this.applicationText = applicationText != null ? applicationText : "";
    }

    public boolean isAccepted() { return isAccepted; }
    public void setAccepted(boolean accepted) { this.isAccepted = accepted; }

    // 편의 메서드 - 엔티티에서 DTO로 변환
    public static ApplicantResponseDto fromEntity(Applicant applicant) {
        return new ApplicantResponseDto(
                applicant.getApplicantId(),
                applicant.getUser().getUserId(),
                applicant.getUser().getNickname(),
                applicant.getApplicationText(),
                applicant.isAccepted()
        );
    }
}
