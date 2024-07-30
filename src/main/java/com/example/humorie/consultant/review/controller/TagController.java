package com.example.humorie.consultant.review.controller;

import com.example.humorie.account.jwt.JwtTokenUtil;
import com.example.humorie.consultant.review.dto.TagReq;
import com.example.humorie.consultant.review.service.TagService;
import com.example.humorie.global.exception.ErrorResponse;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/review/tag")
public class TagController {

    private final JwtTokenUtil jwtTokenUtil;
    private final TagService tagService;

    @PostMapping
    @Operation(summary = "리뷰 태그 등록")
    public ErrorResponse<String> createTag(@RequestBody TagReq tagReq, HttpServletRequest request) {
        String accessToken = jwtTokenUtil.resolveToken(request);

        return new ErrorResponse<>(tagService.createTag(accessToken, tagReq));
    }
}
