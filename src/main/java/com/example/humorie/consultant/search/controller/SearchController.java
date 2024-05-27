package com.example.humorie.consultant.search.controller;

import com.example.humorie.consultant.counselor.entity.Counselor;
import com.example.humorie.consultant.search.dto.CounselorDto;
import com.example.humorie.consultant.search.service.SearchService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/search")
public class SearchController {

    private final SearchService searchService;

    @GetMapping
    public ResponseEntity<List<CounselorDto>> searchCounselors(@RequestParam String keyword) {
        List<CounselorDto> counselors = searchService.searchCounselor(keyword);
        return new ResponseEntity<>(counselors, HttpStatus.OK);
    }

}
