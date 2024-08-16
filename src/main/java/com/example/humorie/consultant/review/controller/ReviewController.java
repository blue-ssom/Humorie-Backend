package com.example.humorie.consultant.review.controller;

import com.example.humorie.account.jwt.JwtTokenUtil;
import com.example.humorie.consultant.review.dto.ReviewRes;
import com.example.humorie.consultant.review.dto.ReviewResList;
import com.example.humorie.consultant.review.service.ReviewService;
import com.example.humorie.consultant.review.dto.ReviewReq;
import com.example.humorie.global.exception.ErrorResponse;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/review")
public class ReviewController {

    private final JwtTokenUtil jwtTokenUtil;
    private final ReviewService reviewService;

    @PostMapping("/{consultId}")
    @Operation(summary = "리뷰 작성")
    public ErrorResponse<String> createReview(@RequestBody ReviewReq reviewReq, @RequestParam long consultId, HttpServletRequest request) {
        String accessToken = jwtTokenUtil.resolveToken(request);

        return new ErrorResponse<>(reviewService.createReview(accessToken, consultId, reviewReq));
    }

    @PatchMapping("/{reviewId}")
    @Operation(summary = "리뷰 수정")
    public ErrorResponse<String> modifyReview(@RequestBody ReviewReq reviewReq, @RequestParam long reviewId, HttpServletRequest request) {
        String accessToken = jwtTokenUtil.resolveToken(request);

        return new ErrorResponse<>(reviewService.modifyReview(accessToken, reviewId, reviewReq));
    }

    @DeleteMapping("/{reviewId}")
    @Operation(summary = "리뷰 삭제")
    public ErrorResponse<String> deleteReview(@RequestParam long reviewId, HttpServletRequest request) {
        String accessToken = jwtTokenUtil.resolveToken(request);

        return new ErrorResponse<>(reviewService.deleteReview(accessToken, reviewId));
    }

    @GetMapping("/{counselorId}")
    @Operation(summary = "리뷰 리스트 조회")
    public ErrorResponse<ReviewResList> getReviewList(@RequestParam long counselorId) {
        return new ErrorResponse<>(reviewService.getReviewListByCounselor(counselorId));
    }

}
