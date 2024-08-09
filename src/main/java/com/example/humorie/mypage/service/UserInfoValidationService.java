package com.example.humorie.mypage.service;

import com.example.humorie.global.exception.ErrorCode;
import com.example.humorie.global.exception.ErrorException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
public class UserInfoValidationService {

    private final PasswordEncoder passwordEncoder;

    // 이름 유효성 검사
    public void validateName(String name) {
        // 이름 빈 값 체크
        if (!StringUtils.hasText(name)) {
            throw new ErrorException(ErrorCode.EMPTY_NAME);
        }
        // 추가 유효성 검사 로직
    }

    // 이메일 유효성 검사
    public void validateEmail(String email) {
        // 이메일 빈 값 체크
        if (!StringUtils.hasText(email)) {
            throw new ErrorException(ErrorCode.EMPTY_EMAIL);
        }

        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
        Pattern emailPattern = Pattern.compile(emailRegex);
        Matcher emailMatcher = emailPattern.matcher(email);

        if (!emailMatcher.matches()) {
            throw new ErrorException(ErrorCode.INVALID_EMAIL);
        }
    }

    // 비밀번호 유효성 검사
    public void validatePassword(String newPassword) {
        // 비밀번호 빈 값 체크
        if (!StringUtils.hasText(newPassword)) {
            throw new ErrorException(ErrorCode.EMPTY_PASSWORD);
        }

        String passwordRegex = "^(?=.*[0-9])(?=.*[a-zA-Z])(?=.*[@#$%^&+=!])(?=\\S+$).{8,16}$";
        Pattern pwPattern = Pattern.compile(passwordRegex);
        Matcher pwMatcher = pwPattern.matcher(newPassword);

        if (!pwMatcher.matches()) {
            throw new ErrorException(ErrorCode.INVALID_PASSWORD);
        }
    }

    // 비밀번호 재확인
    public void validatePasswordConfirmation(String newPassword, String passwordCheck) {
        if (!newPassword.equals(passwordCheck)) {
            throw new ErrorException(ErrorCode.PASSWORD_MISMATCH);
        }
    }

    // 회원 탈퇴 시 원시 비밀번호와 암호화된 비밀번호가 일치하는지 확인
    public void validatePasswordMatch(String rawPassword, String encodedPassword) {
        if (!passwordEncoder.matches(rawPassword, encodedPassword)) {
            throw new  ErrorException(ErrorCode.PASSWORD_MISMATCH);
        }
    }

}
