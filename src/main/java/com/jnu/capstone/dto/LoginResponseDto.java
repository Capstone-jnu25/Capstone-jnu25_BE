package com.jnu.capstone.dto;

import lombok.Data;

@Data
public class LoginResponseDto {
    private int userId;
    private String email;
    private String nickname;
    private String token;
    private double latitude;   // 위도
    private double longitude;  // 경도

    public LoginResponseDto(int userId, String email, String nickname, String token, double latitude, double longitude) {
        this.userId = userId;
        this.email = email;
        this.nickname = nickname;
        this.token = token;
        this.latitude = latitude;
        this.longitude = longitude;
    }
}
