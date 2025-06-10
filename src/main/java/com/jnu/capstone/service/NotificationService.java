package com.jnu.capstone.service;

import com.jnu.capstone.entity.User;
import com.jnu.capstone.entity.Keyword;
import com.jnu.capstone.entity.BoardType;
import com.jnu.capstone.entity.NotificationLog;
import com.jnu.capstone.repository.UserRepository;
import com.jnu.capstone.repository.KeywordRepository;
import com.jnu.capstone.repository.NotificationLogRepository;
import com.jnu.capstone.service.FcmService;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
@Service
@RequiredArgsConstructor
public class NotificationService {

    private final KeywordRepository keywordRepository;
    private final UserRepository userRepository;
    private final FcmService fcmService;
    private final NotificationLogRepository notificationLogRepository;
    /**
     * 게시글 키워드를 기반으로 사용자에게 FCM 알림을 전송
     */
    public void notifyUsersByKeywords(List<String> postKeywords, BoardType boardType, int postId) {
        Set<Integer> notifiedUserIds = new HashSet<>();

        String boardLabel = getBoardTypeLabel(boardType);

        for (String keywordText : postKeywords) {
            // 게시판 + 키워드 기준으로 사용자 조회
            List<Keyword> matchingUserKeywords =
                    keywordRepository.findByKeywordTextAndBoardType(keywordText, boardType);

            for (Keyword keyword : matchingUserKeywords) {
                int userId = keyword.getUser().getUserId();
                if (notifiedUserIds.contains(userId)) continue;

                userRepository.findById(userId).ifPresent(user -> {
                    String token = user.getFcmToken();
                    if (token != null && !token.isEmpty()) {
                        fcmService.sendMessageTo(
                                token,
                                "🔔 " + boardLabel + " 키워드 알림",
                                "등록한 키워드 '" + keywordText + "'와 일치하는 새 게시글이 등록되었습니다!"
                        );
                        NotificationLog log = NotificationLog.of(user, keywordText, boardType, postId);
                        notificationLogRepository.save(log);

                        notifiedUserIds.add(userId);
                    }
                });
            }
        }
    }

    public void notifyMatchingUsersByTitleOrContent(String title, String content, BoardType boardType, int postId, int writerUserId) {
        Set<Integer> notifiedUserIds = new HashSet<>();

        String boardLabel = getBoardTypeLabel(boardType);

        List<Keyword> registeredKeywords = keywordRepository.findByBoardType(boardType);

        for (Keyword keyword : registeredKeywords) {
            String keywordText = keyword.getKeywordText();
            if (title.contains(keywordText) || content.contains(keywordText)) {
                User user = keyword.getUser();
                if (user.getUserId() == writerUserId) continue; // 작성자 제외
                String token = user.getFcmToken();
                if (token != null && !token.isEmpty() && !notifiedUserIds.contains(user.getUserId())) {
                    fcmService.sendMessageTo(
                            token,
                            "🔔 " + boardLabel + " 키워드 알림",
                            "‘" + title + "’ 게시글이 등록되었습니다!"
                    );
                    NotificationLog log = NotificationLog.of(user, keywordText, boardType, postId);
                    notificationLogRepository.save(log);
                    notifiedUserIds.add(user.getUserId());
                }
            }
        }
    }


    private String getBoardTypeLabel(BoardType boardType) {
        return switch (boardType) {
            case STUDY -> "스터디";
            case MEETUP -> "번개";
            case LOST -> "분실물";
            case SECONDHAND -> "중고거래";
        };
    }

}
