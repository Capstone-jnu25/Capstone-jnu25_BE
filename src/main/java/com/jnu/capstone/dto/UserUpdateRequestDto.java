package com.jnu.capstone.dto;

import lombok.Data;

@Data
public class UserUpdateRequestDto {
    private String nickname;
    private String department;
    private int studentNum;
}
