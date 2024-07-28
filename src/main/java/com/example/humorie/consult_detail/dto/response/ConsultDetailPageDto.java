package com.example.humorie.consult_detail.dto.response;

import lombok.Getter;
import org.springframework.data.domain.Page;

import java.util.List;

@Getter
public class ConsultDetailPageDto {
    private final List<ConsultDetailListDto> consultDetails;
    private final int pageNumber;
    private final int pageSize;
    private final long totalElements;
    private final int totalPages;

    public ConsultDetailPageDto(Page<ConsultDetailListDto> consultDetailPage) {
        this.consultDetails = consultDetailPage.getContent();
        this.pageNumber = consultDetailPage.getNumber();
        this.pageSize = consultDetailPage.getSize();
        this.totalElements = consultDetailPage.getTotalElements();
        this.totalPages = consultDetailPage.getTotalPages();
    }

    public ConsultDetailPageDto(List<ConsultDetailListDto> consultDetails, int pageNumber, int pageSize, long totalElements, int totalPages) {
        this.consultDetails = consultDetails;
        this.pageNumber = pageNumber;
        this.pageSize = pageSize;
        this.totalElements = totalElements;
        this.totalPages = totalPages;
    }

    public static ConsultDetailPageDto from(Page<ConsultDetailListDto> consultDetailPage) {
        return new ConsultDetailPageDto(
                consultDetailPage.getContent(),
                consultDetailPage.getNumber(),
                consultDetailPage.getSize(),
                consultDetailPage.getTotalElements(),
                consultDetailPage.getTotalPages()
        );
    }
}
