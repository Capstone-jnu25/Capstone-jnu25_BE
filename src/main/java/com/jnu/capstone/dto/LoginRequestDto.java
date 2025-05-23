package com.jnu.capstone.dto;

import lombok.Data;

@Data
public class LoginRequestDto {
    private String email;
    private String password;
}
