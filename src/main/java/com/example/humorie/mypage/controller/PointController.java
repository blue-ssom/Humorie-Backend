package com.example.humorie.mypage.controller;

import com.example.humorie.account.jwt.JwtTokenUtil;
import com.example.humorie.mypage.dto.PointDto;
import com.example.humorie.mypage.dto.TotalPointDto;
import com.example.humorie.mypage.service.PointService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/points")
public class PointController {

    private final PointService pointService;
    private final JwtTokenUtil jwtTokenUtil;

    @GetMapping("/all")
    @Operation(summary = "전체 포인트 내역 조회")
    public ResponseEntity<List<PointDto>> getAllPoints(HttpServletRequest request) {
        String accessToken = jwtTokenUtil.resolveToken(request);
        List<PointDto> pointDto = pointService.getAllPoints(accessToken);

        return ResponseEntity.ok(pointDto);
    }

    @GetMapping("/earned")
    @Operation(summary = "적립 포인트 내역 조회")
    public ResponseEntity<List<PointDto>> getEarnedPoints(HttpServletRequest request) {
        String accessToken = jwtTokenUtil.resolveToken(request);
        List<PointDto> pointDto = pointService.getEarnedPoints(accessToken);

        return ResponseEntity.ok(pointDto);
    }

    @GetMapping("/spent")
    @Operation(summary = "사용 포인트 내역 조회")
    public ResponseEntity<List<PointDto>> getSpentPoints(HttpServletRequest request) {
        String accessToken = jwtTokenUtil.resolveToken(request);
        List<PointDto> pointDto = pointService.getSpentPoints(accessToken);

        return ResponseEntity.ok(pointDto);
    }

    @GetMapping("/total")
    @Operation(summary = "보유 포인트 조회")
    public ResponseEntity<TotalPointDto> getTotalPoints(HttpServletRequest request) {
        String accessToken = jwtTokenUtil.resolveToken(request);
        TotalPointDto totalPoints = pointService.getTotalPoints(accessToken);

        return ResponseEntity.ok(totalPoints);
    }

}
