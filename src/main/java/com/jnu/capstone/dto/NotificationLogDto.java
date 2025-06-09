package com.jnu.capstone.dto;

import com.jnu.capstone.entity.BoardType;
import com.jnu.capstone.entity.NotificationLog;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class NotificationLogDto {

    private String keywordText;
    private BoardType boardType;
    private int postId;

    public static NotificationLogDto from(NotificationLog log) {
        return new NotificationLogDto(
                log.getKeywordText(),
                log.getBoardType(),
                log.getPostId()
        );
    }
}
