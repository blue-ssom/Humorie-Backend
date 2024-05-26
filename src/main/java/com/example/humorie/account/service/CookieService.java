package com.example.humorie.account.service;

import com.example.humorie.account.dto.response.TokenDto;
import com.example.humorie.account.jwt.JwtTokenUtil;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CookieService {

    public static ResponseCookie createAccessToken(String access) {
        return ResponseCookie.from("accessToken" , access)
                .path("/")
                .maxAge(30 * 60 * 1000)
                //.secure(true)
                //.domain()
                .httpOnly(true)
                //.sameSite("none")
                .build();
    }

    public static ResponseCookie createRefreshToken(String refresh) {
        return ResponseCookie.from("refreshToken" , refresh)
                .path("/")
                .maxAge(14 * 24 * 60 * 60 * 1000)
                //.secure(true)
                //.domain()
                .httpOnly(true)
                //.sameSite("none")
                .build();
    }

    public void setHeader(HttpServletResponse response, TokenDto tokenDto) {
        if (tokenDto.getRefreshToken() != null) {
            response.addHeader(JwtTokenUtil.REFRESH_TOKEN, tokenDto.getRefreshToken());
            response.addHeader("Set-Cookie", createRefreshToken(tokenDto.getRefreshToken()).toString());
        }

        if (tokenDto.getAccessToken() != null) {
            response.addHeader(JwtTokenUtil.ACCESS_TOKEN, tokenDto.getAccessToken());
            response.addHeader("Set-Cookie", createAccessToken(tokenDto.getAccessToken()).toString());
        }
    }

}
