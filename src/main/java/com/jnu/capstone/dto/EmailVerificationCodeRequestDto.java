package com.jnu.capstone.dto;

import lombok.Data;

@Data
public class EmailVerificationCodeRequestDto {
    private String email;
    private String univName;
    private int code;
}
