package com.example.humorie.consultant.consult_detail.dto.response;

import com.example.humorie.consultant.consult_detail.entity.ConsultDetail;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;

@Getter
public class LatestConsultDetailResDto {
    private final Long id;
    private final Long counselorId;
    private final String counselorName;
    private final double rating;
    private final Boolean isOnline;
    private final LocalDate counselDate;
    private final String title;
    private final String content;

    @Builder
    public LatestConsultDetailResDto(Long id, Long counselorId, String counselorName, LocalDate counselDate, String title, String content, double rating, Boolean isOnline) {
        this.id = id;
        this.counselorId = counselorId;
        this.counselorName = counselorName;
        this.rating = rating;
        this.isOnline = isOnline;
        this.counselDate = counselDate;
        this.title = title;
        this.content = content;
    }

    public static LatestConsultDetailResDto fromEntity(ConsultDetail consultDetail) {
        return LatestConsultDetailResDto.builder()
                .id(consultDetail.getId())
                .counselorId(consultDetail.getCounselorId())
                .counselorName(consultDetail.getCounselorName())
                .rating(consultDetail.getRating())
                .isOnline(consultDetail.getIsOnline())
                .counselDate(consultDetail.getCounselDate())
                .title(consultDetail.getTitle())
                .content(consultDetail.getContent())
                .build();
    }
}
