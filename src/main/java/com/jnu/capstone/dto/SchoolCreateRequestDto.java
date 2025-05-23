package com.jnu.capstone.dto;

import lombok.Data;

@Data
public class SchoolCreateRequestDto {
    private String campusName;
    private double latitude;
    private double longitude;
}
