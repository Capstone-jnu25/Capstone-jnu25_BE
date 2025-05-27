package com.jnu.capstone.dto;

import lombok.Data;

@Data
public class UserSignupRequestDto {
    private int campusId;
    private String email;
    private String password;
    private String nickname;
//    private String department;
    private int studentNum;
}
