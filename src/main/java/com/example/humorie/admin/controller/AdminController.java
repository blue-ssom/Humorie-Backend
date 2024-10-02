package com.example.humorie.admin.controller;

import com.example.humorie.admin.dto.ConsultDetailDto;
import com.example.humorie.admin.service.AdminService;
import com.example.humorie.global.exception.ErrorResponse;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin")
public class AdminController {

    private final AdminService adminService;

    @PostMapping("/consult-detail")
    @Operation(summary = "상담 내역 저장")
    public ErrorResponse<String> saveConsultDetail(@Valid @RequestBody ConsultDetailDto consultDetailDto, HttpServletRequest request) {
        return new ErrorResponse<>(adminService.saveConsultDetail(consultDetailDto));
    }

}
