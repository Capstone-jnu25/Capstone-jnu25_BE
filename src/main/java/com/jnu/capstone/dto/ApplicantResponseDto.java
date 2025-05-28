package com.jnu.capstone.dto;

import com.jnu.capstone.entity.Applicant;
import lombok.AllArgsConstructor;
import lombok.Getter;

// 신청자 목록 조회 DTO
@Getter
@AllArgsConstructor
// 신청자 목록 조회 DTO
public class ApplicantResponseDto {
    private int applicantId;
    private String nickname;
    private String department;
    private String applicationText;
    private boolean isAccepted;
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