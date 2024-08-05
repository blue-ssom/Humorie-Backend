package com.example.humorie.account.service;

import com.example.humorie.account.repository.AccountRepository;
import com.example.humorie.global.exception.ErrorCode;
import com.example.humorie.global.exception.ErrorException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
public class AccountValidationService {

    private final AccountRepository accountRepository;

    public void validatePassword(String password) {
        if (password == null) throw new ErrorException(ErrorCode.INVALID_PASSWORD);

        String passwordRegex = "^(?=.*[0-9])(?=.*[a-zA-Z])(?=.*[@#$%^&+=!])(?=\\S+$).{8,16}$";
        Pattern pwPattern = Pattern.compile(passwordRegex);
        Matcher pwMatcher = pwPattern.matcher(password);

        if (!pwMatcher.matches()) {
            throw new ErrorException(ErrorCode.INVALID_PASSWORD);
        }
    }

    public void validateEmail(String email) {
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
        Pattern emailPattern = Pattern.compile(emailRegex);
        Matcher emailMatcher = emailPattern.matcher(email);

        if (!emailMatcher.matches()) {
            throw new ErrorException(ErrorCode.INVALID_EMAIL);
        }
    }

    public void validateAccountName(String accountName) {
        String accountNameRegex = "^[a-z0-9]{6,}$";
        Pattern accountNamePatter = Pattern.compile(accountNameRegex);
        Matcher accountNameMatcher = accountNamePatter.matcher(accountName);

        if(!accountNameMatcher.matches()) {
            throw  new ErrorException(ErrorCode.INVALID_ID);
        }
    }

    // 이름 유효성 검사
    public void validateName(String name) {
        if (name == null || name.trim().isEmpty()) {
            throw new ErrorException(ErrorCode.INVALID_NAME);
        }
    }

    public void validatePasswordConfirmation(String password, String confirmPassword) {
        if (!password.equals(confirmPassword)) {
            throw new ErrorException(ErrorCode.PASSWORD_MISMATCH);
        }
    }

}
