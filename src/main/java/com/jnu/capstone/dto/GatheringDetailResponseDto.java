package com.jnu.capstone.dto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class GatheringDetailResponseDto {
    private int postId;
    private String title;
    private String dDay;
    private String contents;
    private String place;
    private String gender;
    private String time;
    private boolean isClosed;
    private int authorId;
}
