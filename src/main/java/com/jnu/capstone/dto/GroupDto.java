package com.jnu.capstone.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class GroupDto {
    private int postId;
    private String title;
    private String boardType;
}
