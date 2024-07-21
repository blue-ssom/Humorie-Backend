package com.example.humorie.account.service;

import com.example.humorie.account.config.SecurityConfig;
import com.example.humorie.account.dto.request.AccountDetailUpdate;
import com.example.humorie.account.dto.response.GetAccountResDto;
import com.example.humorie.account.dto.response.TokenDto;
import com.example.humorie.account.dto.request.JoinReq;
import com.example.humorie.account.dto.request.LoginReq;
import com.example.humorie.account.dto.response.LoginRes;
import com.example.humorie.account.entity.AccountDetail;
import com.example.humorie.account.entity.LoginType;
import com.example.humorie.account.jwt.JwtTokenUtil;
import com.example.humorie.account.jwt.PrincipalDetails;
import com.example.humorie.account.repository.AccountRepository;
import com.example.humorie.account.repository.RefreshTokenRepository;
import com.example.humorie.global.exception.ErrorCode;
import com.example.humorie.global.exception.ErrorException;
import com.example.humorie.global.exception.ErrorResponse;
import com.example.humorie.mypage.entity.Point;
import com.example.humorie.mypage.repository.PointRepository;
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
    private final PointRepository pointRepository;
    private final AccountValidationService validationService;
    private final CookieService cookieService;
    private final JwtTokenUtil jwtTokenUtil;
    private final SecurityConfig jwtSecurityConfig;
    private final RedisTemplate redisTemplate;

    @Transactional
    public String join(JoinReq request) {
        validationService.validateEmail(request.getEmail());
        validationService.validateAccountName(request.getAccountName());
        validationService.validatePassword(request.getPassword());

        if (!request.getPassword().equals(request.getPasswordCheck())) {
            throw new ErrorException(ErrorCode.PASSWORD_MISMATCH);
        }

        if (accountRepository.existsByAccountName(request.getAccountName())) {
            throw new ErrorException(ErrorCode.ID_EXISTS);
        }

        AccountDetail accountDetail = AccountDetail.joinAccount(request.getEmail(), jwtSecurityConfig.passwordEncoder().encode(request.getPassword()), request.getAccountName(), request.getName());
        accountRepository.save(accountDetail);

        Point earnedPoints = new Point(accountDetail, 100000, "earn");
        pointRepository.save(earnedPoints);

        return "Success Join";
    }

    @Transactional
    public LoginRes login(LoginReq request, HttpServletResponse response) {
        AccountDetail accountDetail = accountRepository.findByAccountName(request.getAccountName()).orElseThrow(() ->
                new ErrorException(ErrorCode.NONE_EXIST_USER));

        if (!jwtSecurityConfig.passwordEncoder().matches(request.getPassword(), accountDetail.getPassword())) {
            throw new ErrorException(ErrorCode.PASSWORD_MISMATCH);
        } else {
            TokenDto tokenDto = jwtTokenUtil.createToken(accountDetail.getEmail());

            redisTemplate.opsForValue().set("RT:" + accountDetail.getEmail(), tokenDto.getRefreshToken(), tokenDto.getRefreshTokenExpirationTime(), TimeUnit.MILLISECONDS);

            cookieService.setHeader(response, tokenDto);

            String accessToken = tokenDto.getAccessToken();
            String refreshToken = tokenDto.getRefreshToken();

            return new LoginRes(accessToken, refreshToken);
        }
    }

    @Transactional
    public String logout(String accessToken, String refreshToken) {
        if(!jwtTokenUtil.tokenValidation(accessToken)) {
            throw new ErrorException(ErrorCode.INVALID_JWT);
        }

        String email = jwtTokenUtil.getEmailFromToken(refreshToken);

        if (redisTemplate.opsForValue().get("RT:" + email) != null) {
            redisTemplate.delete("RT:" + email);
        }

        Long expiration = jwtTokenUtil.getExpiration(accessToken);
        refreshTokenRepository.deleteByRefreshToken(refreshToken);

        redisTemplate.opsForValue().set(accessToken, "logout", expiration, TimeUnit.MILLISECONDS);

        return "Success Logout";
    }

    public TokenDto refreshAccessToken(String refreshToken) {
        if (!jwtTokenUtil.refreshTokenValidation(refreshToken)) {
            throw new ErrorException(ErrorCode.INVALID_JWT);
        }

        String email = jwtTokenUtil.getEmailFromToken(refreshToken);
        if(!refreshToken.equals(refreshToken)) {
            throw new ErrorException(ErrorCode.INVALID_JWT);
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

    // 사용자 정보 조회
    public GetAccountResDto getMyAccount(PrincipalDetails principalDetails) {
        AccountDetail account = principalDetails.getAccountDetail();

        if(account == null){
            throw new ErrorException(ErrorCode.NONE_EXIST_USER);
        }

        return GetAccountResDto.builder()
                .accountName(account.getAccountName())
                .email(account.getEmail())
                .id(account.getId())
                .emailSubscription(false)
                .build();
    }

    // 사용자 정보 업데이트
    public String updateAccount(PrincipalDetails principalDetails, AccountDetailUpdate updateDto) {
        AccountDetail account = principalDetails.getAccountDetail();

        // 유효성 검사 및 필드 업데이트
        // 이름
        validationService.validateName(updateDto.getName());
        account.setName(updateDto.getName());

        //이메일
        validationService.validateEmail(updateDto.getEmail());
        account.setEmail(updateDto.getEmail());

        // 비밀번호
        validationService.validatePassword(updateDto.getPassword());
        validationService.validatePasswordConfirmation(updateDto.getPassword(), updateDto.getPasswordCheck());
        account.setPassword(jwtSecurityConfig.passwordEncoder().encode(updateDto.getPassword()));

        // 이메일 수신 여부 체크
//        if (updateDto.getEmailSubscription() != null) {
//            account.setEmailSubscription(updateDto.getEmailSubscription());
//        }

        accountRepository.save(account);

        // 변경된 사용자 정보를 저장
        return "Success Update";
    }

    public String deleteAccount(PrincipalDetails principalDetails) {
       AccountDetail account = principalDetails.getAccountDetail();

        accountRepository.deleteById(account.getId());

        return "Success Delete";
    }
}

