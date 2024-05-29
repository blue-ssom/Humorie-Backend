package com.example.humorie.recommend.controller;

import com.example.humorie.account.jwt.PrincipalDetails;
import com.example.humorie.recommend.dto.RecommendCounselorDto;
import com.example.humorie.recommend.dto.RecommendReviewDto;
import com.example.humorie.recommend.service.RecommendService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Tag(name = "Recommend" , description = "Recommend 관련 API 모음")
@RequiredArgsConstructor
@RestController
@RequestMapping("/recommend")
public class RecommendController {

    private final RecommendService recommendService;

    @GetMapping("/counselor")
    public ResponseEntity<List<RecommendCounselorDto>> recommendCounselor(@AuthenticationPrincipal PrincipalDetails principal) {

        return ResponseEntity.status(HttpStatus.OK).body(recommendService.recommendCounselor(principal));
    }

}
