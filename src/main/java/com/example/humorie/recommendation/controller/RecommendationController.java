package com.example.humorie.recommendation.controller;

import com.example.humorie.account.jwt.PrincipalDetails;
import com.example.humorie.recommendation.dto.RecommendationCounselorDto;
import com.example.humorie.recommendation.dto.RecommendationReviewDto;
import com.example.humorie.recommendation.service.RecommendationService;
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
public class RecommendationController {

    private final RecommendationService recommendationService;

    @GetMapping("/counselor")
    public ResponseEntity<List<RecommendationCounselorDto>> recommendCounselor(@AuthenticationPrincipal PrincipalDetails principal) {

        return ResponseEntity.status(HttpStatus.OK).body(recommendationService.recommendCounselor(principal));
    }

    @GetMapping("/review")
    public ResponseEntity<List<RecommendationReviewDto>> recommendReview(@AuthenticationPrincipal PrincipalDetails principal) {

        return ResponseEntity.status(HttpStatus.OK).body(recommendationService.recommendReview(principal));
    }
}
