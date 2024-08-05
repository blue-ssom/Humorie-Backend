package com.example.humorie.consult_detail.dto.response;

import com.example.humorie.consult_detail.entity.ConsultDetail;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;

@Getter
public class ConsultDetailListDto {
    private final Long id;
    private final String counselorName;
    private final String counselContent;
    private final String location;
    private final LocalDate counselDate;

    @Builder
    public ConsultDetailListDto(Long id, String counselorName, String counselContent, String location, LocalDate counselDate) {
        this.id = id;
        this.counselorName = counselorName;
        this.counselContent = counselContent;
        this.location = location;
        this.counselDate = counselDate;
    }

    public static ConsultDetailListDto fromEntity(ConsultDetail consultDetail) {
        return ConsultDetailListDto.builder()
                .id(consultDetail.getId())
                .counselorName(consultDetail.getCounselorName())
                .counselContent(consultDetail.getCounselContent())
                .location(consultDetail.getLocation())
                .counselDate(consultDetail.getCounselDate())
                .build();
    }
}
