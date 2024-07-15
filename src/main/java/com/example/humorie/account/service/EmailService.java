package com.example.humorie.account.service;

import com.example.humorie.account.config.SecurityConfig;
import com.example.humorie.account.entity.AccountDetail;
import com.example.humorie.account.repository.AccountRepository;
import com.example.humorie.global.exception.ErrorCode;
import com.example.humorie.global.exception.ErrorException;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;


@Service
@Slf4j
@RequiredArgsConstructor
public class EmailService {

    @Value("${spring.mail.username}")
    private String sender;
    private final JavaMailSender javaMailSender;
    private final AccountRepository accountRepository;
    private final SecurityConfig securityConfig;

    public String sendEmail(String sendTo) throws Exception {
        String password = generateRandomPassword();

        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        mimeMessage.setFrom(sender);
        mimeMessage.addRecipient(MimeMessage.RecipientType.TO, new InternetAddress(sendTo));
        mimeMessage.setSubject("<&DAY> 임시 비밀번호 발급");
        mimeMessage.setText("안녕하세요. &DAY 임시 비밀번호 안내 관련 이메일 입니다.\n\n" + " 회원님의 임시 비밀번호는 " + password + " 입니다. " + "로그인 후 비밀번호를 변경해 주세요.");

        javaMailSender.send(mimeMessage);
        updatePassword(sendTo, password);

        return "Email sent successfully";
    }


    private String generateRandomPassword() {
        String str = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789!@#$%^&*()_+-=";
        StringBuilder temporaryPassword = new StringBuilder();

        for (int i = 0; i < 10; i++) {
            int idx = (int)(Math.random() * str.length());
            temporaryPassword.append(str.charAt(idx));
        }

        return temporaryPassword.toString();
    }

    private void updatePassword(String email, String password) {
        AccountDetail accountDetail = accountRepository.findByEmail(email).orElseThrow(
                () -> new ErrorException(ErrorCode.NONE_EXIST_USER));

        String encodedPassword = securityConfig.passwordEncoder().encode(password);
        accountDetail.setPassword(encodedPassword);

        accountRepository.save(accountDetail);
    }

}
