package com.jnu.capstone.dto;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
@Getter
@Setter
@AllArgsConstructor
public class MessageResponseDto {
    private long messageId;
    private int senderId;
    private String senderNickname;
    private String detailMessage;
    private LocalDateTime sendTime;
}
