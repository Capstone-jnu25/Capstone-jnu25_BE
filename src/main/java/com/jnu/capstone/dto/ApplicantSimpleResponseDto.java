package com.jnu.capstone.dto;

import com.jnu.capstone.entity.Applicant;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ApplicantSimpleResponseDto {
    private int applicantId;
    private String nickname;
    private boolean isAccepted;

    // 엔티티에서 DTO로 변환하는 편의 메서드
    public static ApplicantSimpleResponseDto fromEntity(Applicant applicant) {
        return new ApplicantSimpleResponseDto(
                applicant.getApplicantId(),
                applicant.getUser().getNickname(),
                applicant.isAccepted()
        );
    }
}
