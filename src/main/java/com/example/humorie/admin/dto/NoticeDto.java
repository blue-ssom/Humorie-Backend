package com.example.humorie.admin.dto;

import com.example.humorie.notice.entity.Notice;
import java.time.LocalDate;
import java.time.LocalTime;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class NoticeDto {
    private String title;         // 제목
    private String content;       // 내용

    // 엔티티 변환 메서드
    public Notice toEntity(String adminName) {
        Notice notice = new Notice();
        notice.setTitle(this.title);
        notice.setContent(this.content);
        notice.setViewCount(0); // 초기 조회수 설정
        notice.setAuthor(adminName);
        notice.setCreatedDate(LocalDate.now()); // 현재 날짜 설정
        notice.setCreatedTime(LocalTime.now()); // 현재 시간 설정
        return notice;
    }
}
