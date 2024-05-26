package com.example.humorie.account.controller;

import com.example.humorie.account.dto.response.TokenDto;
import com.example.humorie.account.dto.request.JoinReq;
import com.example.humorie.account.dto.request.LoginReq;
import com.example.humorie.account.dto.response.LoginRes;
import com.example.humorie.account.jwt.JwtTokenFilter;
import com.example.humorie.account.jwt.JwtTokenUtil;
import com.example.humorie.account.service.AccountService;
import com.example.humorie.account.service.CookieService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/account")
public class AccountController {

    private final AccountService accountService;
    private final CookieService cookieService;
    private final JwtTokenUtil jwtTokenUtil;

    @PostMapping("/join")
    @Operation(summary = "일반 로그인 회원가입")
    public ResponseEntity<String> join(@RequestBody JoinReq request) throws IOException {

        return accountService.join(request);
    }

    @PostMapping("/login")
    @Operation(summary = "일반 로그인 로그인")
    public ResponseEntity<LoginRes> login(@RequestBody @Valid LoginReq request, HttpServletResponse response) {
        return accountService.login(request, response);
    }

    /*@DeleteMapping("/logout")
    @Operation(summary = "로그아웃")
    public ResponseEntity<String> logout(@RequestHeader(name = JwtTokenUtil.ACCESS_TOKEN) String accessToken,
                                         @RequestHeader(name = JwtTokenUtil.REFRESH_TOKEN) String refreshToken) {
        return accountService.logout(accessToken, refreshToken);
    }*/

    @DeleteMapping("/logout")
    @Operation(summary = "로그아웃")
    public ResponseEntity<String> logout(HttpServletRequest request, @RequestHeader(name = JwtTokenUtil.REFRESH_TOKEN) String refreshToken) {
        String accessToken = jwtTokenUtil.resolveToken(request);

        return accountService.logout(accessToken, refreshToken);
    }

    @PostMapping("/issue/token")
    @Operation(summary = "Access Token 갱신")
    public ResponseEntity<?> refreshAccessToken(HttpServletRequest request, HttpServletResponse response,
                                                @RequestHeader(name = JwtTokenUtil.REFRESH_TOKEN, required = false) String refreshToken) {
        TokenDto newTokenDto = null;

        if (refreshToken == null) {
            String cookie_refreshToken = JwtTokenFilter.getTokenByRequest(request, "refreshToken");
            newTokenDto = accountService.refreshAccessToken(cookie_refreshToken);
        } else {
            newTokenDto = accountService.refreshAccessToken(refreshToken);
        }

        cookieService.setHeader(response, newTokenDto);

        return new ResponseEntity<>(newTokenDto, HttpStatus.OK);
    }

}
