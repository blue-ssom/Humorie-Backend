package com.example.humorie.consult_detail.dto.response;

import com.example.humorie.consult_detail.entity.ConsultDetail;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;

@Getter
public class LatestConsultDetailResDto {
    private final Long id;
    private final String counselorName;
    private final double rating;
    private final String location;
    private final LocalDate counselDate;
    private final String title;
    private final String content;

    @Builder
    public LatestConsultDetailResDto(Long id, String counselorName, LocalDate counselDate, String status, String title, String symptom, String content, double rating, String location) {
        this.id = id;
        this.counselorName = counselorName;
        this.rating = rating;
        this.location = location;
        this.counselDate = counselDate;
        this.title = title;
        this.content = content;
    }

    public static LatestConsultDetailResDto fromEntity(ConsultDetail consultDetail) {
        return LatestConsultDetailResDto.builder()
                .id(consultDetail.getId())
                .counselorName(consultDetail.getCounselorName())
                .counselDate(consultDetail.getCounselDate())
                .title(consultDetail.getTitle())
                .content(consultDetail.getContent())
                .rating(consultDetail.getRating())
                .location(consultDetail.getLocation())
                .build();
    }
}
