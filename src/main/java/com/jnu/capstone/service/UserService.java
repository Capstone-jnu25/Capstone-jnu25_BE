package com.jnu.capstone.service;

import com.jnu.capstone.dto.*;
import org.springframework.web.multipart.MultipartFile;
import com.jnu.capstone.dto.LoginRequestDto;
import com.jnu.capstone.dto.LoginResponseDto;



public interface UserService {
    // 유저 정보 조회 기능
    UserResponseDto getMyProfile(int userId);
    UserResponseDto getUserById(int userId);
    UserResponseDto updateUser(int userId, UserUpdateRequestDto requestDto);
    void changePassword(int userId, PasswordChangeRequestDto requestDto);
    String updateProfilePicture(int userId, MultipartFile file);
    void incrementGoodCount(int userId);
    void incrementBadCount(int userId);
    void deleteUser(int userId);
    LoginResponseDto login(LoginRequestDto requestDto);
    // 회원가입 기능
    UserSignupResponseDto signup(UserSignupRequestDto requestDto);

    // 이메일 인증 기능
    void sendVerificationEmail(EmailVerificationRequestDto requestDto);
    void verifyEmailCode(EmailVerificationCodeRequestDto requestDto);

    // 이메일 인증 상태 확인
    boolean isEmailVerified(String email);
}
