package com.jnu.capstone.dto;

import com.jnu.capstone.entity.User;
import lombok.Data;

@Data
public class UserResponseDto {
    private int userId;
    private String email;
    private String nickname;
    private String department;
    private int studentNum;
    private int goodCount;
    private int badCount;
    private String profileImageUrl;  // 추가된 필드

    public static UserResponseDto fromEntity(User user) {
        UserResponseDto dto = new UserResponseDto();
        dto.setUserId(user.getUserId());
        dto.setEmail(user.getEmail());
        dto.setNickname(user.getNickname());
        dto.setDepartment(user.getDepartment());
        dto.setStudentNum(user.getStudentNum());
        dto.setGoodCount(user.getGoodCount());
        dto.setBadCount(user.getBadCount());
//        dto.setProfileImageUrl(user.getProfileImageUrl());
        return dto;
    }
}
