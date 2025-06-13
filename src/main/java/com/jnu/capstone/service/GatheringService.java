package com.jnu.capstone.service;

import java.time.LocalDate;
import com.jnu.capstone.dto.PostCreateRequestDto;
import com.jnu.capstone.dto.PostResponseDto;
import com.jnu.capstone.dto.GatheringDetailResponseDto;
import com.jnu.capstone.entity.BoardType;
import com.jnu.capstone.entity.GatheringBoard;
import com.jnu.capstone.entity.GenderType;
import com.jnu.capstone.entity.Post;
import com.jnu.capstone.entity.User;
import com.jnu.capstone.entity.Chatroom;
import com.jnu.capstone.entity.ChatJoin;
import com.jnu.capstone.repository.GatheringBoardRepository;
import com.jnu.capstone.repository.KeywordRepository;
import com.jnu.capstone.repository.PostRepository;
import com.jnu.capstone.repository.UserRepository;
import com.jnu.capstone.repository.ApplicantRepository;
import com.jnu.capstone.repository.ChatroomRepository;
import com.jnu.capstone.repository.ChatJoinRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class GatheringService {
    @Autowired
    private GatheringBoardRepository gatheringBoardRepository;
    @Autowired
    private ApplicantRepository applicantRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PostRepository postRepository;
    @Autowired
    private ChatroomRepository chatroomRepository;
    @Autowired
    private ChatJoinRepository chatJoinRepository;
    @Autowired
    private KeywordRepository keywordRepository;
    @Autowired
    private NotificationService notificationService;

    @Autowired
    private FcmService fcmService;
    public Page<PostResponseDto> getGatheringPosts(int userId, String boardType, Pageable pageable) {
        // String -> Enum 변환
        BoardType type = BoardType.valueOf(boardType.toUpperCase());

        // 유저의 학교 정보 가져오기
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("해당 유저가 존재하지 않습니다."));
        int campusId = user.getCampus().getCampusId();

        // 해당 학교의 게시글 목록 조회
        return gatheringBoardRepository
                .findByBoardTypeAndPost_Campus_CampusIdAndPost_IsDeletedFalse(type, campusId, pageable)
                .map(gatheringBoard -> {
                    LocalDate dueDate = gatheringBoard.getDueDate();

                    // D-Day 계산
                    long dDay = Math.max(0, LocalDate.now().until(dueDate).getDays());
                    String dDayText = dDay == 0 ? "D-DAY" : "D-" + dDay;

                    // 마감 조건
                    boolean isClosed = false;

                    if (dueDate.isBefore(LocalDate.now())) {
                        isClosed = true; // 1. 마감일이 지난 경우
                    } else if (gatheringBoard.isAutomatic() && gatheringBoard.getCurrentParticipants() >= gatheringBoard.getMaxParticipants()) {
                        isClosed = true; // 2. 자동 수락 (automatic = true)
                    } else if (!gatheringBoard.isAutomatic()) { // 3. 수동 수락 (automatic = false)
                        int acceptedCount = (int) applicantRepository.countByPostAndIsAccepted(gatheringBoard.getPost(), true);
                        if (acceptedCount >= gatheringBoard.getMaxParticipants()) {
                            isClosed = true;
                        }
                    }

                    return new PostResponseDto(
                            gatheringBoard.getPost().getPostId(),
                            gatheringBoard.getPost().getTitle(),
                            gatheringBoard.getPost().getContents(),
                            gatheringBoard.getPlace(),
                            gatheringBoard.getMeetTime(),
                            dueDate,
                            gatheringBoard.getGender().toString(),
                            gatheringBoard.getMaxParticipants(),
                            gatheringBoard.getCurrentParticipants(),
                            gatheringBoard.getBoardType().toString(),
                            isClosed,
                            dDayText,
                            gatheringBoard.getPost().getUser().getUserId(),
                            gatheringBoard.getPost().getUser().getNickname()
                    );
                });
    }

    public GatheringDetailResponseDto getGatheringDetail(int postId) {
        GatheringBoard gatheringBoard = gatheringBoardRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("해당 게시글이 존재하지 않습니다."));

        if (gatheringBoard.getPost().isDeleted()) {
            throw new IllegalArgumentException("삭제된 게시글입니다.");
        }

        long dDay = Math.max(0, LocalDate.now().until(gatheringBoard.getDueDate()).getDays());
        String dDayText = dDay == 0 ? "D-DAY" : "D-" + dDay;

        boolean isClosed = gatheringBoard.getDueDate().isBefore(LocalDate.now()) ||
                (gatheringBoard.isAutomatic() && gatheringBoard.getMaxParticipants() <= gatheringBoard.getCurrentParticipants());

        Post post = gatheringBoard.getPost();
        User author = post.getUser();

        return new GatheringDetailResponseDto(
                gatheringBoard.getPost().getPostId(),
                gatheringBoard.getPost().getTitle(),
                dDayText,
                gatheringBoard.getPost().getContents(),
                gatheringBoard.getPlace(),
                gatheringBoard.getGender().toString(),
                gatheringBoard.getMeetTime(),
                isClosed,
                author.getUserId(),
                author.getNickname()
        );
    }

    @Transactional
    public int createGathering(int userId, PostCreateRequestDto requestDto) {
        // 유저 찾기
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("해당 유저가 존재하지 않습니다."));

        // 게시글 타입이 null인지 확인
        String boardTypeStr = requestDto.getBoardType();
        if (boardTypeStr == null || boardTypeStr.trim().isEmpty()) {
            throw new IllegalArgumentException("게시판 유형(board_type)은 필수입니다.");
        }

        // 게시판 타입 변환
        BoardType boardType;
        try {
            boardType = BoardType.valueOf(boardTypeStr.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("유효하지 않은 게시판 유형입니다. (예: STUDY, MEETUP)");
        }

        //게시글 생성
        Post post = new Post();
        post.setUser(user);
        post.setTitle(requestDto.getTitle());
        post.setContents(requestDto.getContents() != null ? requestDto.getContents() : "");
        post.setBoardType(boardType);
        post.setCampus(user.getCampus());

        //모집 게시글 생성 (Post와 연관 설정)
        GatheringBoard gatheringBoard = new GatheringBoard();
        gatheringBoard.setPlace(requestDto.getPlace());
        gatheringBoard.setMeetTime(requestDto.getTime());
        gatheringBoard.setDueDate(requestDto.getDueDate());
        gatheringBoard.setGender(GenderType.valueOf(requestDto.getGender().toUpperCase()));
        gatheringBoard.setMaxParticipants(requestDto.getMaxParticipants());
        gatheringBoard.setAutomatic(requestDto.isAutomatic());
        gatheringBoard.setCurrentParticipants(0);
        gatheringBoard.setBoardType(boardType);

        //양방향 연관관계 설정
        post.setGatheringBoard(gatheringBoard);
        gatheringBoard.setPost(post);

        postRepository.save(post);

        if (boardType == BoardType.STUDY || boardType == BoardType.MEETUP) {
            notificationService.notifyMatchingUsersByTitleOrContent(
                    post.getTitle(),
                    post.getContents(),
                    boardType,
                    post.getPostId(),
                    user.getUserId()
            );
        }

        // 채팅방 생성 (게시글과 연관 설정)
        Chatroom chatroom = new Chatroom();
        chatroom.setPost(post);
        chatroom.setChatTitle(post.getTitle());
        chatroomRepository.save(chatroom);

        // 작성자 자동 채팅방 참여 (chat_join 테이블)
        ChatJoin chatJoin = new ChatJoin();
        chatJoin.setUserId(user.getUserId());
        chatJoin.setChattingRoomId(chatroom.getChattingRoomId());
        chatJoinRepository.save(chatJoin);

        return post.getPostId();
    }
}