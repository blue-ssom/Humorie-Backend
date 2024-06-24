package com.example.humorie.consultant.counselor.controller;

import com.example.humorie.account.jwt.JwtTokenUtil;
import com.example.humorie.consultant.counselor.dto.BookmarkDto;
import com.example.humorie.consultant.counselor.dto.CounselorProfileDto;
import com.example.humorie.consultant.counselor.entity.Bookmark;
import com.example.humorie.consultant.counselor.service.BookmarkService;
import com.example.humorie.consultant.counselor.service.CounselorService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/counselor")
public class CounselorController {

    private final CounselorService counselorService;
    private final BookmarkService bookmarkService;
    private final JwtTokenUtil jwtTokenUtil;

    @GetMapping("/{counselorId}")
    @Operation(summary = "상담사 프로필 조회")
    public ResponseEntity<CounselorProfileDto> getCounselorProfile(@PathVariable long counselorId) {
        CounselorProfileDto counselorDTO = counselorService.getCounselorProfile(counselorId);
        return ResponseEntity.ok(counselorDTO);
    }

    @PostMapping("/bookmark/add")
    @Operation(summary = "상담사 북마크 추가")
    public ResponseEntity<String> addBookmark(@RequestParam long counselorId, HttpServletRequest request) {
        String accessToken = jwtTokenUtil.resolveToken(request);
        bookmarkService.addBookmark(accessToken, counselorId);

        return ResponseEntity.ok("Bookmark added successfully");
    }

    @DeleteMapping("/bookmark/remove")
    @Operation(summary = "상담사 북마크 삭제")
    public ResponseEntity<String> removeBookmark(@RequestParam long counselorId, HttpServletRequest request) {
        String accessToken = jwtTokenUtil.resolveToken(request);
        bookmarkService.removeBookmark(accessToken, counselorId);

        return ResponseEntity.ok("Bookmark removed successfully");
    }

    @GetMapping("/bookmark")
    @Operation(summary = "북마크 리스트 조회")
    public ResponseEntity<List<BookmarkDto>> getUserBookmarks(HttpServletRequest request) {
        String accessToken = jwtTokenUtil.resolveToken(request);

        return ResponseEntity.ok(bookmarkService.getAllBookmarks(accessToken));
    }

}
