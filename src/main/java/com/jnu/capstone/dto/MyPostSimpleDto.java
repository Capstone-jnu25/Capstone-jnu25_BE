package com.jnu.capstone.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class MyPostSimpleDto {
    private int postId;
    private String title;
    private String boardType;
}
