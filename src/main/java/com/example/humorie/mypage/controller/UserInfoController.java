package com.example.humorie.mypage.controller;

import com.example.humorie.account.service.AccountService;
import com.example.humorie.account.jwt.PrincipalDetails;
import com.example.humorie.global.exception.ErrorResponse;
import com.example.humorie.mypage.dto.response.GetUserInfoResDto;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/mypage")
public class UserInfoController {

    private final AccountService accountService;
    @GetMapping("/get")
    @Operation(summary = "내 정보 조회(마이페이지)")
    public ErrorResponse<GetUserInfoResDto> getAccountById(@AuthenticationPrincipal PrincipalDetails principalDetails) {
        GetUserInfoResDto account = accountService.getMyAccount(principalDetails);
        return new ErrorResponse<>(account);
    }
}
