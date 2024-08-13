package com.example.humorie.consultant.review.controller;

import com.example.humorie.account.jwt.JwtTokenUtil;
import com.example.humorie.consultant.review.dto.TagReq;
import com.example.humorie.consultant.review.dto.TagRes;
import com.example.humorie.consultant.review.service.TagService;
import com.example.humorie.global.exception.ErrorResponse;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/review")
public class TagController {

    private final JwtTokenUtil jwtTokenUtil;
    private final TagService tagService;

    @PostMapping("/tag")
    @Operation(summary = "태그 등록")
    public ErrorResponse<String> createTag(@RequestBody TagReq tagReq, HttpServletRequest request) {
        String accessToken = jwtTokenUtil.resolveToken(request);

        return new ErrorResponse<>(tagService.createTag(accessToken, tagReq));
    }

    @GetMapping("/tag/{tagName}")
    @Operation(summary = "태그 단건 조회")
    public ErrorResponse<TagRes> getTagByName(@PathVariable String tagName, HttpServletRequest request) {
        String accessToken = jwtTokenUtil.resolveToken(request);

        return new ErrorResponse<>(tagService.getTagByName(accessToken, tagName));
    }

    @GetMapping("/tags")
    @Operation(summary = "태그 목록 조회")
    public ErrorResponse<List<TagRes>> getAllTags(HttpServletRequest request) {
        String accessToken = jwtTokenUtil.resolveToken(request);

        return new ErrorResponse<>(tagService.getAllTags(accessToken));
    }

    @DeleteMapping("/tag/{tagId}")
    @Operation(summary = "태그 삭제")
    public ErrorResponse<String> deleteTag(@PathVariable long tagId, HttpServletRequest request) {
        String accessToken = jwtTokenUtil.resolveToken(request);

        return new ErrorResponse<>(tagService.deleteTag(accessToken, tagId));
    }

}
