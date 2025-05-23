package com.jnu.capstone.dto;

import lombok.Data;

@Data
public class UserSignupRequestDto {
    private String email;
    private String password;
    private String nickname;
    private int campusId;
    private String department;
    private int studentNum;
}
