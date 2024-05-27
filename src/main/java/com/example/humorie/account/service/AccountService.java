package com.example.humorie.account.service;

import com.example.humorie.account.config.SecurityConfig;
import com.example.humorie.account.dto.response.TokenDto;
import com.example.humorie.account.dto.request.JoinReq;
import com.example.humorie.account.dto.request.LoginReq;
import com.example.humorie.account.dto.response.LoginRes;
import com.example.humorie.account.entity.AccountDetail;
import com.example.humorie.account.entity.LoginType;
import com.example.humorie.account.jwt.JwtTokenUtil;
import com.example.humorie.account.repository.AccountRepository;
import com.example.humorie.account.repository.RefreshTokenRepository;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.concurrent.TimeUnit;

import static com.example.humorie.account.jwt.JwtTokenUtil.ACCESS_TIME;

@Service
@Slf4j
@RequiredArgsConstructor
public class AccountService {

    private final AccountRepository accountRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final AccountValidationService validationService;
    private final CookieService cookieService;
    private final JwtTokenUtil jwtTokenUtil;
    private final SecurityConfig jwtSecurityConfig;
    private final RedisTemplate redisTemplate;

    @Transactional
    public ResponseEntity<String> join(JoinReq request) {
        validationService.validateEmail(request.getEmail());
        validationService.validateAccountName(request.getAccountName());
        validationService.validatePassword(request.getPassword());

        if (!request.getPassword().equals(request.getPasswordCheck())) {
            throw new RuntimeException("Passwords do not match");
        }

        if (accountRepository.existsByAccountName(request.getAccountName())) {
            throw new RuntimeException("The username already exists");
        }

        AccountDetail accountDetail = AccountDetail.joinAccount(request.getEmail(), jwtSecurityConfig.passwordEncoder().encode(request.getPassword()), request.getAccountName(), request.getName());
        accountRepository.save(accountDetail);

        return ResponseEntity.ok("Success Join");
    }

    @Transactional
    public ResponseEntity<LoginRes> login(LoginReq request, HttpServletResponse response) {
        AccountDetail accountDetail = accountRepository.findByAccountName(request.getAccountName()).orElseThrow(() ->
                new RuntimeException("Not found user"));

        if (!jwtSecurityConfig.passwordEncoder().matches(request.getPassword(), accountDetail.getPassword())) {
            throw new RuntimeException("The passwords do not match");
        } else {
            TokenDto tokenDto = jwtTokenUtil.createToken(accountDetail.getEmail());

            redisTemplate.opsForValue().set("RT:" + accountDetail.getEmail(), tokenDto.getRefreshToken(), tokenDto.getRefreshTokenExpirationTime(), TimeUnit.MILLISECONDS);

            cookieService.setHeader(response, tokenDto);

            String accessToken = tokenDto.getAccessToken();
            String refreshToken = tokenDto.getRefreshToken();

            return ResponseEntity.ok(new LoginRes(accessToken, refreshToken));
        }
    }

    @Transactional
    public ResponseEntity<String> logout(String accessToken, String refreshToken) {
        if(!jwtTokenUtil.tokenValidation(accessToken)) {
            throw new IllegalArgumentException("Invalid Access Token");
        }

        String email = jwtTokenUtil.getEmailFromToken(refreshToken);

        if (redisTemplate.opsForValue().get("RT:" + email) != null) {
            redisTemplate.delete("RT:" + email);
        }

        Long expiration = jwtTokenUtil.getExpiration(accessToken);
        refreshTokenRepository.deleteByRefreshToken(refreshToken);

        redisTemplate.opsForValue().set(accessToken, "logout", expiration, TimeUnit.MILLISECONDS);

        return ResponseEntity.ok("Success Logout");
    }

    public TokenDto refreshAccessToken(String refreshToken) {
        if (!jwtTokenUtil.refreshTokenValidation(refreshToken)) {
            throw new RuntimeException("Invalid Refresh Token");
        }

        String email = jwtTokenUtil.getEmailFromToken(refreshToken);
        if(!refreshToken.equals(refreshToken)) {
            throw new IllegalArgumentException("The refresh token information does not match");
        }

        String newAccessToken = jwtTokenUtil.recreateAccessToken(refreshToken);
        long refreshTokenExpirationTime = jwtTokenUtil.getExpiration(refreshToken);

        redisTemplate.opsForValue().set("RT:" + email, newAccessToken, ACCESS_TIME , TimeUnit.MILLISECONDS);

        return TokenDto.builder()
                .accessToken(newAccessToken)
                .refreshToken(refreshToken)
                .refreshTokenExpirationTime(refreshTokenExpirationTime)
                .build();
    }

    public String findAccountNameByEmail(String email) {
        Optional<AccountDetail> accountDetail = accountRepository.findByEmail(email);
        if (accountDetail.isPresent()) {
            return accountDetail.get().getAccountName();
        } else {
            throw new RuntimeException("Account not found with email: " + email);
        }
    }

}

