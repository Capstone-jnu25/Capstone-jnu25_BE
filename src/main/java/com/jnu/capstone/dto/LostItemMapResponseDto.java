package com.jnu.capstone.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class LostItemMapResponseDto {
    private int postId;
    private String title;
    private double latitude;
    private double longitude;
}
