package com.example.humorie.account.controller;

import com.example.humorie.account.dto.request.*;
import com.example.humorie.account.dto.response.TokenDto;
import com.example.humorie.account.dto.response.LoginRes;
import com.example.humorie.account.jwt.JwtTokenFilter;
import com.example.humorie.account.jwt.JwtTokenUtil;
import com.example.humorie.account.jwt.PrincipalDetails;
import com.example.humorie.account.service.AccountService;
import com.example.humorie.account.service.CookieService;
import com.example.humorie.account.service.EmailService;
import com.example.humorie.global.exception.ErrorCode;
import com.example.humorie.global.exception.ErrorResponse;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/account")
public class AccountController {

    private final AccountService accountService;
    private final CookieService cookieService;
    private final EmailService emailService;
    private final JwtTokenUtil jwtTokenUtil;

    @PostMapping("/join")
    @Operation(summary = "일반 로그인 회원가입")
    public ErrorResponse<String> join(@RequestBody JoinReq request) throws IOException {
        return new ErrorResponse<>(accountService.join(request));
    }

    @PostMapping("/login")
    @Operation(summary = "일반 로그인 로그인")
    public ErrorResponse<LoginRes> login(@RequestBody @Valid LoginReq request, HttpServletResponse response) {
        return new ErrorResponse<>(accountService.login(request, response));
    }

    @DeleteMapping("/logout")
    @Operation(summary = "로그아웃")
    public ErrorResponse<String> logout(HttpServletRequest request, @RequestHeader(name = JwtTokenUtil.REFRESH_TOKEN) String refreshToken) {
        String accessToken = jwtTokenUtil.resolveToken(request);

        return new ErrorResponse<>(accountService.logout(accessToken, refreshToken));
    }

    @PostMapping("/issue/token")
    @Operation(summary = "Access Token 갱신")
    public ErrorResponse<?> refreshAccessToken(HttpServletRequest request, HttpServletResponse response,
                                                @RequestHeader(name = JwtTokenUtil.REFRESH_TOKEN, required = false) String refreshToken) {
        TokenDto newTokenDto = null;

        if (refreshToken == null) {
            String cookie_refreshToken = JwtTokenFilter.getTokenByRequest(request, "refreshToken");
            newTokenDto = accountService.refreshAccessToken(cookie_refreshToken);
        } else {
            newTokenDto = accountService.refreshAccessToken(refreshToken);
        }

        cookieService.setHeader(response, newTokenDto);

        return new ErrorResponse<>(newTokenDto);
    }

    @PostMapping("/find-id")
    @Operation(summary = "아이디 찾기")
    public ErrorResponse<String> findAccountNameByEmail(@RequestBody AccountNameFinder finder) {
        return new ErrorResponse<>(accountService.findAccountNameByEmail(finder.getEmail()));
    }

    @PostMapping("/find-password")
    @Operation(summary = "비밀번호 찾기")
    public ErrorResponse<String> sendEmail(@RequestBody EmailDto emailDto) {
        try {
            return new ErrorResponse<>(emailService.sendEmail(emailDto.getEmail()));
        } catch (Exception e) {
            e.printStackTrace();
            return new ErrorResponse<>(ErrorCode.SEND_EMAIL_FAILED);
        }
    }
}