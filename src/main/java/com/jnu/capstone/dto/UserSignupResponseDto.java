package com.jnu.capstone.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserSignupResponseDto {
    private int userId;
    private String email;
    private String nickname;
}
