package com.jnu.capstone.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class ChatroomResponseDto {
    private int chattingRoomId;
    private String chatTitle;
    private String lastMessage;
    private String boardType;
}
