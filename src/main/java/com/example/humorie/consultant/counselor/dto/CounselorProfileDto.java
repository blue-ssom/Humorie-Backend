package com.example.humorie.consultant.counselor.dto;

import com.example.humorie.consultant.counselor.entity.CounselingField;
import com.example.humorie.consultant.review.dto.ReviewDto;
import lombok.*;

import java.util.List;
import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CounselorProfileDto {

    private String name;

    private double rating;

    private int counselingCount;

    private int reviewCount;

    private Set<CounselingField> counselingField;

    private List<ReviewDto> reviews;

}
