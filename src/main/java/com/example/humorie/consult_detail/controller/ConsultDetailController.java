package com.example.humorie.consult_detail.controller;

import com.example.humorie.account.dto.response.GetAccountResDto;
import com.example.humorie.account.service.AccountService;
import com.example.humorie.consult_detail.dto.response.LatestConsultDetailResDto;
import com.example.humorie.consult_detail.entity.ConsultDetail;
import com.example.humorie.consult_detail.service.ConsultDetailService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.example.humorie.account.jwt.PrincipalDetails;
import com.example.humorie.global.exception.ErrorResponse;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.http.ResponseEntity;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import com.example.humorie.account.jwt.PrincipalDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/consult-detail")
@RequiredArgsConstructor
@Slf4j
public class ConsultDetailController {
    private final AccountService accountService;
    private final ConsultDetailService consultDetailService;

    // 가장 최근 상담 내역 조회
    @GetMapping("/latest")
    @Operation(summary = "가장 최근에 받은 상담")
    public LatestConsultDetailResDto getLatestConsultDetail(@AuthenticationPrincipal PrincipalDetails principalDetails) {
        return consultDetailService.getLatestConsultDetailResponse(principalDetails);
    }

}
