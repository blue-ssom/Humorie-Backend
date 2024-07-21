package com.example.humorie.recommendation.controller;

import com.example.humorie.account.jwt.PrincipalDetails;
import com.example.humorie.global.exception.ErrorResponse;
import com.example.humorie.recommendation.dto.RecommendationCounselorDto;
import com.example.humorie.recommendation.dto.RecommendationReviewDto;
import com.example.humorie.recommendation.service.RecommendationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Tag(name = "Recommendation" , description = "Recommendation 관련 API 모음")
@RequiredArgsConstructor
@RestController
@RequestMapping("/recommendation")
public class RecommendationController {

    private final RecommendationService recommendationService;

    @Operation(summary = "상담사 추천")
    @GetMapping("/counselor")
    public ErrorResponse<List<RecommendationCounselorDto>> recommendCounselor(@AuthenticationPrincipal PrincipalDetails principal) {
        List<RecommendationCounselorDto> recommendationCounselorDtos = recommendationService.recommendCounselor(principal);

        return new ErrorResponse<>(recommendationCounselorDtos);
    }

    @Operation(summary = "리뷰 추천")
    @GetMapping("/review")
    public  ErrorResponse<List<RecommendationReviewDto>>  recommendReview(@AuthenticationPrincipal PrincipalDetails principal) {
        List<RecommendationReviewDto> recommendationReviewDtos = recommendationService.recommendReview(principal);

        return new ErrorResponse<>(recommendationReviewDtos);
    }
}
