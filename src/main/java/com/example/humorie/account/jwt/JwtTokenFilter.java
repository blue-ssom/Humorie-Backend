package com.example.humorie.account.jwt;

import com.example.humorie.global.dto.GlobalResDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.ObjectUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;

@Slf4j
@RequiredArgsConstructor
public class JwtTokenFilter extends OncePerRequestFilter {

    private final JwtTokenUtil jwtTokenUtil;
    private final RedisTemplate redisTemplate;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String cookie_accessToken = getTokenByRequest(request, "accessToken");
        String cookie_refreshToken = getTokenByRequest(request, "refreshToken");
        String accessToken = jwtTokenUtil.getHeaderToken(request, "Access");
        String refreshToken = jwtTokenUtil.getHeaderToken(request, "Refresh");

        if(cookie_accessToken != null && cookie_refreshToken != null) {
            processSecurity(cookie_accessToken, cookie_refreshToken, response);
        } else {
            processSecurity(accessToken, refreshToken, response);
        }

        filterChain.doFilter(request, response);
    }

    private void processSecurity(String accessToken, String refreshToken, HttpServletResponse response) {
        if (accessToken != null) {
            if (!jwtTokenUtil.tokenValidation(accessToken)) {
                jwtExceptionHandler(response, "AccessToken Expired", HttpStatus.BAD_REQUEST);
                return;
            }
            String isLogout = (String) redisTemplate.opsForValue().get(accessToken);

            if(ObjectUtils.isEmpty(isLogout)) {
                setAuthentication(jwtTokenUtil.getEmailFromToken(accessToken));
            }
        } else if (refreshToken != null) {
            if (!jwtTokenUtil.refreshTokenValidation(refreshToken)) {
                jwtExceptionHandler(response, "RefreshToken Expired", HttpStatus.BAD_REQUEST);

                return;
            }

            setAuthentication(jwtTokenUtil.getEmailFromToken(refreshToken));
        }
    }

    public void setAuthentication(String email) {
        Authentication authentication = jwtTokenUtil.createAuthentication(email);
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    public void jwtExceptionHandler (HttpServletResponse response, String msg, HttpStatus status) {
        response.setStatus(status.value());
        response.setContentType("application/json");
        try {
            String json = new ObjectMapper().writeValueAsString(new GlobalResDto(msg, status.value()));
            // response.getWriter().write(json);
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }

    public static String getTokenByRequest(HttpServletRequest request, String type) {
        Cookie cookies[] = request.getCookies();

        if (cookies != null && cookies.length != 0) {
            return Arrays.stream(cookies)
                    .filter(c -> c.getName().equals(type)).findFirst().map(Cookie::getValue)
                    .orElse(null);
        }

        return null;
    }

}
