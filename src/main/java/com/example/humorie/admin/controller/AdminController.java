package com.example.humorie.admin.controller;

import com.example.humorie.account.jwt.PrincipalDetails;
import com.example.humorie.admin.dto.ConsultDetailDto;
import com.example.humorie.admin.service.AdminService;
import com.example.humorie.global.exception.ErrorResponse;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin")
public class AdminController {

    private final AdminService adminService;

    @PostMapping("/consult-detail/{counselorId}")
    @Operation(summary = "상담 내역 저장")
    public ErrorResponse<String> saveConsultDetail(@Valid @RequestBody ConsultDetailDto consultDetailDto,
                                                   @PathVariable Long counselorId,
                                                   @AuthenticationPrincipal PrincipalDetails principal) {
        return new ErrorResponse<>(adminService.saveConsultDetail(consultDetailDto, counselorId, principal));
    }

}