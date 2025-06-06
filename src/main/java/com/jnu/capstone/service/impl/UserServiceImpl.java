package com.jnu.capstone.service.impl;

import java.util.List;
import java.util.Map;
import com.jnu.capstone.dto.LoginRequestDto;
import com.jnu.capstone.dto.LoginResponseDto;
import com.jnu.capstone.dto.*;
import com.jnu.capstone.entity.Post;
import com.jnu.capstone.entity.School;
import com.jnu.capstone.entity.User;
import com.jnu.capstone.repository.*;
import com.jnu.capstone.service.UserService;
import com.jnu.capstone.util.JwtTokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.client.RestTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Autowired
    private SchoolRepository schoolRepository;


    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private EmailVerificationService emailVerificationService;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private SecondhandBoardRepository secondhandBoardRepository;

    @Autowired
    private LostBoardRepository lostBoardRepository;

    @Autowired
    private GatheringBoardRepository gatheringBoardRepository;

//    @Autowired
//    private UserService userService;

    @Value("${univcert.api.key}")
    private String univCertApiKey;

    // ✅ 이메일 인증 상태를 임시 저장 (서버 재시작 시 사라짐)
    private final Map<String, Boolean> verifiedEmailStore = new ConcurrentHashMap<>();

    @Override
    public LoginResponseDto login(LoginRequestDto requestDto) {
        // 사용자 조회
        User user = userRepository.findByEmail(requestDto.getEmail())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 이메일입니다."));

        // 비밀번호 검증
        if (!passwordEncoder.matches(requestDto.getPassword(), user.getPassword())) {
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
        }

        // 토큰 생성
        String token = jwtTokenProvider.generateToken(user.getUserId(), user.getEmail());

        // ✅ 사용자 학교의 위도/경도 가져오기
        School school = user.getCampus();
        double latitude = school.getLatitude();
        double longitude = school.getLongitude();

        // 로그인 응답 생성
        return new LoginResponseDto(user.getUserId(), user.getEmail(), user.getNickname(), token, latitude, longitude);
    }
    // UserServiceImpl.java
    @Override
    public void updateFcmToken(int userId, String fcmToken) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("사용자 없음"));
        user.setFcmToken(fcmToken);
        userRepository.save(user);
    }



//    @Override
//    public LoginResponseDto login(LoginRequestDto requestDto) {
//        // 사용자 조회
//        User user = userRepository.findByEmail(requestDto.getEmail())
//                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 이메일입니다."));
//
//        // 비밀번호 검증
//        if (!passwordEncoder.matches(requestDto.getPassword(), user.getPassword())) {
//            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
//        }
//
//        // 토큰 생성
//        String token = jwtTokenProvider.generateToken(user.getUserId(), user.getEmail());
//
//        // 로그인 응답 생성
//        return new LoginResponseDto(user.getUserId(), user.getEmail(), user.getNickname(), token);
//    }


    @Override
    @Transactional
    public UserSignupResponseDto signup(UserSignupRequestDto requestDto) {
        String univName = requestDto.getUnivName();

        Optional<School> optionalSchool = schoolRepository.findByCampusName(univName);

        School school = optionalSchool.orElseGet(() -> {
            School newSchool = new School();
            newSchool.setCampusName(univName);
            newSchool.setLatitude(requestDto.getLatitude());
            newSchool.setLongitude(requestDto.getLongitude());
            return schoolRepository.save(newSchool);
        });

        User user = new User();
        user.setEmail(requestDto.getEmail());
        user.setPassword(passwordEncoder.encode(requestDto.getPassword()));
        user.setNickname(requestDto.getNickname());
        user.setStudentNum(requestDto.getStudentNum());
        user.setCampus(school);
        user.setGoodCount(0);
        user.setBadCount(0);
        user.setEmailVerified(true);

        User savedUser = userRepository.save(user);

        return new UserSignupResponseDto(savedUser.getUserId(), savedUser.getEmail(), savedUser.getNickname());
    }



    @Override
    public void sendVerificationEmail(EmailVerificationRequestDto requestDto) {
        String url = "https://univcert.com/api/v1/certify";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        String requestBody = String.format(
                "{\"key\":\"%s\",\"email\":\"%s\",\"univName\":\"%s\",\"univ_check\":true}",
                univCertApiKey, requestDto.getEmail(), requestDto.getUnivName()
        );

        HttpEntity<String> entity = new HttpEntity<>(requestBody, headers);
        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, entity, String.class);

        if (!response.getBody().contains("\"success\":true")) {
            throw new IllegalArgumentException("이메일 인증 코드 발송에 실패했습니다.");
        }
    }

    @Override
    public void verifyEmailCode(EmailVerificationCodeRequestDto requestDto) {
        String url = "https://univcert.com/api/v1/certifycode";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        String requestBody = String.format(
                "{\"key\":\"%s\",\"email\":\"%s\",\"univName\":\"%s\",\"code\":%d}",
                univCertApiKey, requestDto.getEmail(), requestDto.getUnivName(), requestDto.getCode()
        );

        HttpEntity<String> entity = new HttpEntity<>(requestBody, headers);
        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, entity, String.class);

        //✅✅✅✅✅확인용
        System.out.println("📨 UnivCert 응답: " + response.getBody());

        if (!response.getBody().contains("\"success\":true")) {
            throw new IllegalArgumentException("인증 코드가 일치하지 않습니다.");
        }

        // ✅ 임시로 인증된 이메일 저장 (예: Memory, Redis 등)
        verifiedEmailStore.put(requestDto.getEmail(), true);
    }


    @Override
    public boolean isEmailVerified(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));
        return user.isEmailVerified();
    }

    @Override
    public UserResponseDto getMyProfile(int userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));
        return UserResponseDto.fromEntity(user);
    }

    @Override
    public UserResponseDto getUserById(int userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));
        return UserResponseDto.fromEntity(user);
    }

    @Override
    @Transactional
    public UserResponseDto updateUser(int userId, UserUpdateRequestDto requestDto) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));
//
//        // 닉네임 수정
//        if (requestDto.getNickname() != null) {
//            user.setNickname(requestDto.getNickname());
//        }

        // 학과 수정
        if ((user.getDepartment() == null || user.getDepartment().isBlank())
                && requestDto.getDepartment() != null && !requestDto.getDepartment().isBlank()) {
            user.setDepartment(requestDto.getDepartment());
        }


//
//        // 학번 수정
//        if (requestDto.getStudentNum() > 0) {
//            user.setStudentNum(requestDto.getStudentNum());
//        }

        // 수정된 유저 저장
        userRepository.save(user);
        return UserResponseDto.fromEntity(user);
    }

    @Override
    @Transactional
    public void changePassword(int userId, PasswordChangeRequestDto requestDto) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));

        String encryptedPassword = passwordEncoder.encode(requestDto.getNewPassword());
        user.setPassword(encryptedPassword); // password는 User 엔티티에 있는 필드명과 일치해야 함
        userRepository.save(user);
    }
//    @Override
//    @Transactional
//    public void changePassword(int userId, PasswordChangeRequestDto requestDto) {
//        // 사용자 조회
//        User user = userRepository.findById(userId)
//                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));
//
////        // 현재 비밀번호 검증
////        if (!passwordEncoder.matches(requestDto.getCurrentPassword(), user.getPassword())) {
////            throw new IllegalArgumentException("현재 비밀번호가 일치하지 않습니다.");
////        }
//
////        // 현재 비밀번호와 새 비밀번호가 같은지 검사
////        if (requestDto.getCurrentPassword().equals(requestDto.getNewPassword())) {
////            throw new IllegalArgumentException("새 비밀번호는 현재 비밀번호와 달라야 합니다.");
////        }
//
//        // 새 비밀번호 암호화
//        String encryptedNewPassword = passwordEncoder.encode(requestDto.getNewPassword());
//
//        // 비밀번호 변경
//        user.setPassword(encryptedNewPassword);
//        userRepository.save(user);
//    }


    @Override
    public String updateProfilePicture(int userId, MultipartFile file) {
        return "";
    }

    @Override
    @Transactional
    public void deleteUser(int userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));

        List<Post> posts = postRepository.findByUser_UserId(userId);

        for (Post post : posts) {
            // 게시판 테이블에서 먼저 삭제
            secondhandBoardRepository.deleteByPost(post);
            lostBoardRepository.deleteByPost(post);
            gatheringBoardRepository.deleteByPost(post);

            // 게시글 삭제
            postRepository.delete(post);
        }

        userRepository.delete(user);
    }


    @Override
    @Transactional
    public void incrementBadCount(int userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));

        // badCount 증가
        user.setBadCount(user.getBadCount() + 1);
        userRepository.save(user);
    }

    @Override
    @Transactional
    public void incrementGoodCount(int userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));

        // goodCount 증가
        user.setGoodCount(user.getGoodCount() + 1);
        userRepository.save(user);
    }
    // 기존 유저 관리 메소드 (수정 없이 유지)
}
