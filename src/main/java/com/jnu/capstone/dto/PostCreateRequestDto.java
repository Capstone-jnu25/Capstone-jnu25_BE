package com.jnu.capstone.dto;

import java.time.LocalDate;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;

@Getter
@Setter
@NoArgsConstructor
public class PostCreateRequestDto {
    private String title;
    private String time;
    private String place;
    private String contents;
    private LocalDate dueDate;
    private String gender;
    private int maxParticipants;
    private boolean automatic;
    private String boardType;
}
