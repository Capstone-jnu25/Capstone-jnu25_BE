package com.jnu.capstone.dto;

import com.jnu.capstone.entity.Applicant;

// 신청자 목록 조회 DTO
public class ApplicantResponseDto {
    private int applicantId;
    private String nickname;
    private String department;
    private String applicationText;
    private boolean isAccepted;

    public ApplicantResponseDto(int applicantId, String nickname, String department, String applicationText, boolean isAccepted) {
        this.applicantId = applicantId;
        this.nickname = nickname;
        this.department = department;
        this.applicationText = applicationText;
        this.isAccepted = isAccepted;
    }

    public int getApplicantId() {
        return applicantId;
    }

    public String getNickname() {
        return nickname;
    }

    public String getDepartment() {
        return department;
    }

    public String getApplicationText() {
        return applicationText;
    }

    public boolean isAccepted() {
        return isAccepted;
    }

    // 엔티티에서 DTO로 변환하는 편의 메서드
    public static ApplicantResponseDto fromEntity(Applicant applicant) {
        return new ApplicantResponseDto(
                applicant.getApplicantId(),
                applicant.getUser().getNickname(),
                applicant.getUser().getDepartment(),
                applicant.getApplicationText(),
                applicant.isAccepted()
        );
    }
}