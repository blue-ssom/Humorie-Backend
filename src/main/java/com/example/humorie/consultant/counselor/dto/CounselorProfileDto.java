package com.example.humorie.consultant.counselor.dto;

import com.example.humorie.consultant.counselor.entity.Affiliation;
import com.example.humorie.consultant.counselor.entity.Career;
import com.example.humorie.consultant.counselor.entity.CounselingField;
import com.example.humorie.consultant.counselor.entity.Education;
import com.example.humorie.consultant.review.dto.ReviewDto;
import lombok.*;

import java.util.List;
import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CounselorProfileDto {

    private long counselorId;

    private String name;

    private String phoneNumber;

    private String email;

    private String qualification;

    private double rating;

    private int counselingCount;

    private int reviewCount;

    private Set<Affiliation> affiliations;

    private List<Education> educations;

    private List<Career> careers;

    private Set<CounselingField> counselingField;

    private List<ReviewDto> reviews;

}
