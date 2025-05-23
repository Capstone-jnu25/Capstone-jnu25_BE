package com.jnu.capstone.dto;

import com.jnu.capstone.entity.Applicant;

public class ApplicantSimpleResponseDto {
    private int applicantId;
    private String nickname;
    private boolean isAccepted;

    public ApplicantSimpleResponseDto(int applicantId, String nickname, boolean isAccepted) {
        this.applicantId = applicantId;
        this.nickname = nickname;
        this.isAccepted = isAccepted;
    }

    public int getApplicantId() {
        return applicantId;
    }

    public String getNickname() {
        return nickname;
    }

    public boolean isAccepted() {
        return isAccepted;
    }

    // 엔티티에서 DTO로 변환하는 편의 메서드
    public static ApplicantSimpleResponseDto fromEntity(Applicant applicant) {
        return new ApplicantSimpleResponseDto(
                applicant.getApplicantId(),
                applicant.getUser().getNickname(),
                applicant.isAccepted()
        );
    }
}
