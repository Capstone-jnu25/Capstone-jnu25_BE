package com.jnu.capstone.dto;

import lombok.Data;

@Data
public class LoginResponseDto {
    private int userId;
    private String email;
    private String nickname;
    private String token;

    public LoginResponseDto(int userId, String email, String nickname, String token) {
        this.userId = userId;
        this.email = email;
        this.nickname = nickname;
        this.token = token;
    }
}
