package com.jnu.capstone.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
public class PasswordChangeRequestDto {
//    private String currentPassword;
    private String newPassword;
}
