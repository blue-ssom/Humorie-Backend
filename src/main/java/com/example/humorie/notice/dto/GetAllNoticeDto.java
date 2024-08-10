package com.example.humorie.notice.dto;

import com.example.humorie.notice.entity.Notice;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;

@Getter
@Builder
public class GetAllNoticeDto {
    private Long id;
    private boolean importance;
    private String title;
    private LocalDate createdDate;
    private int viewCount;

    // Notice 엔티티를 GetAllNoticeDto로 변환하는 메서드
    public static GetAllNoticeDto fromEntity(Notice notice) {
        return GetAllNoticeDto.builder()
                .id(notice.getId())
                .importance(notice.isImportance())
                .title(notice.getTitle())
                .createdDate(notice.getCreatedDate())
                .viewCount(notice.getViewCount())
                .build();
    }
}
