package com.jnu.capstone.controller;

import com.jnu.capstone.dto.*;
import com.jnu.capstone.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import com.jnu.capstone.dto.LoginRequestDto;
import com.jnu.capstone.dto.LoginResponseDto;



@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    // 로그인
    @PostMapping("/login")
    public ResponseEntity<LoginResponseDto> login(@RequestBody LoginRequestDto requestDto) {
        LoginResponseDto responseDto = userService.login(requestDto);
        return ResponseEntity.ok(responseDto);
    }

    // 자신의 프로필 조회 (닉네임, 이메일, 학번, 학과, good_count, bad_count)
    @GetMapping("/{userId}/me")
    public ResponseEntity<UserResponseDto> getMyProfile(@PathVariable int userId) {
        return ResponseEntity.ok(userService.getMyProfile(userId));
    }

    // 특정 사용자 프로필 조회 (타 사용자)
    @GetMapping("/{userId}")
    public ResponseEntity<UserResponseDto> getUserById(@PathVariable int userId) {
        return ResponseEntity.ok(userService.getUserById(userId));
    }

    // 사용자 정보 수정 (학과)
    @PutMapping("/{userId}")
    public ResponseEntity<UserResponseDto> updateUser(@PathVariable int userId, @RequestBody UserUpdateRequestDto requestDto) {
        return ResponseEntity.ok(userService.updateUser(userId, requestDto));
    }

    // 비밀번호 변경
    @PutMapping("/{userId}/password")
    public ResponseEntity<Void> changePassword(@PathVariable int userId, @RequestBody PasswordChangeRequestDto requestDto) {
        userService.changePassword(userId, requestDto);
        return ResponseEntity.noContent().build();
    }

//    // 프로필 사진 변경
//    @PutMapping("/{userId}/profile")
//    public ResponseEntity<String> updateProfilePicture(@PathVariable int userId, @RequestPart("profileImage") MultipartFile file) {
//        String imageUrl = userService.updateProfilePicture(userId, file);
//        return ResponseEntity.ok(imageUrl);
//    }

    // 좋아요 버튼
    @PostMapping("/{userId}/good")
    public ResponseEntity<Void> incrementGoodCount(@PathVariable int userId) {
        userService.incrementGoodCount(userId);
        return ResponseEntity.noContent().build();
    }

    // 나빠요 버튼
    @PostMapping("/{userId}/bad")
    public ResponseEntity<Void> incrementBadCount(@PathVariable int userId) {
        userService.incrementBadCount(userId);
        return ResponseEntity.noContent().build();
    }

    // 사용자 삭제
    @DeleteMapping("/{userId}")
    public ResponseEntity<Void> deleteUser(@PathVariable int userId) {
        userService.deleteUser(userId);
        return ResponseEntity.noContent().build();
    }

    //회원가입기능
    @PostMapping("/signup")
    public ResponseEntity<UserSignupResponseDto> signup(@RequestBody UserSignupRequestDto requestDto) {
        UserSignupResponseDto responseDto = userService.signup(requestDto);
        return ResponseEntity.ok(responseDto);
    }

    // 이메일 인증 코드 발송
    @PostMapping("/verify-email")
    public ResponseEntity<String> sendVerificationEmail(@RequestBody EmailVerificationRequestDto requestDto) {
        userService.sendVerificationEmail(requestDto);
        return ResponseEntity.ok("인증 코드가 발송되었습니다.");
    }

    // 이메일 인증 코드 검증
    @PostMapping("/verify-code")
    public ResponseEntity<String> verifyEmailCode(@RequestBody EmailVerificationCodeRequestDto requestDto) {
        userService.verifyEmailCode(requestDto);
        return ResponseEntity.ok("이메일 인증이 완료되었습니다.");
    }


}
