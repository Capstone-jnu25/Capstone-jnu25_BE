package com.jnu.capstone.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "notification_log")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NotificationLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 알림 대상 사용자
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    // 매칭된 키워드
    @Column(name = "keyword_text", nullable = false)
    private String keywordText;

    // 게시판 종류
    @Enumerated(EnumType.STRING)
    @Column(name = "board_type", nullable = false)
    private BoardType boardType;

    // 관련 게시글 ID
    @Column(name = "post_id", nullable = false)
    private int postId;

    public static NotificationLog of(User user, String keywordText, BoardType boardType, int postId) {
        return NotificationLog.builder()
                .user(user)
                .keywordText(keywordText)
                .boardType(boardType)
                .postId(postId)
                .build();
    }
}
