package com.example.humorie.notice.controller;

import com.example.humorie.global.exception.ErrorCode;
import com.example.humorie.global.exception.ErrorException;
import com.example.humorie.notice.dto.GetAllNoticeDto;
import com.example.humorie.notice.dto.NoticeDetailDto;
import com.example.humorie.notice.dto.NoticeDetailWithNavigationDto;
import com.example.humorie.notice.dto.NoticePageDto;
import com.example.humorie.notice.entity.Notice;
import com.example.humorie.notice.service.NoticeService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;
@RestController
@RequiredArgsConstructor
@RequestMapping("/notice")
public class NoticeController {

    private final NoticeService noticeService;

    @GetMapping("/get")
    @Operation(summary = "공지사항 전체 조회")
    public NoticePageDto getAllNotices(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "9") int size
    ) {
        // 페이지 번호에 대한 유효성 검사
        if (page < 0) {
            throw new ErrorException(ErrorCode.NEGATIVE_PAGE_NUMBER);
        }

        // 페이지 크기에 대한 유효성 검사
        if (size < 1) {
            throw new ErrorException(ErrorCode.NEGATIVE_PAGE_SIZE);
        } else if (size > 9) {
            throw new ErrorException(ErrorCode.INVALID_PAGE_SIZE);
        }

        Pageable pageable = PageRequest.of(page, size);
        return noticeService.getAllNotices(pageable);
    }

    @GetMapping("/search")
    @Operation(summary = "공지사항 검색")
    public NoticePageDto searchNotices(
            @RequestParam(required = false) String keyword,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "9") int size
    ) {
        // 페이지 번호에 대한 유효성 검사
        if (page < 0) {
            throw new ErrorException(ErrorCode.NEGATIVE_PAGE_NUMBER);
        }

        // 페이지 크기에 대한 유효성 검사
        if (size < 1) {
            throw new ErrorException(ErrorCode.NEGATIVE_PAGE_SIZE);
        } else if (size > 9) {
            throw new ErrorException(ErrorCode.INVALID_PAGE_SIZE);
        }

        Pageable pageable = PageRequest.of(page, size);

        // 키워드가 없는 경우 전체 공지사항 반환
        if (keyword == null || keyword.trim().isEmpty()) {
            return noticeService.getAllNotices(pageable);
        }

        return noticeService.searchNotices(keyword, pageable);
    }

    @GetMapping("/{id}")
    @Operation(summary = "특정 공지사항 및 이전/다음 공지사항 조회")
    public NoticeDetailWithNavigationDto getNoticeWithNavigation(@PathVariable Long id) {
        return noticeService.getNoticeWithNavigation(id);
    }
}
