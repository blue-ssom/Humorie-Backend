package com.example.humorie.consultant.search.dto;

import com.example.humorie.consultant.counselor.entity.CounselingField;
import com.example.humorie.consultant.counselor.entity.CounselingMethod;
import com.example.humorie.consultant.counselor.entity.Counselor;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CounselorDto {

    private long counselorId;

    private String name;

    private String gender;

    private String region;

    private double rating;

    private int reviewCount;

    private Set<CounselingMethod> counselingMethods;

    private Set<CounselingField> counselingFields;

    public static CounselorDto createDto(Counselor counselor) {
        return new CounselorDto(
                counselor.getId(),
                counselor.getName(),
                counselor.getGender(),
                counselor.getRegion(),
                counselor.getRating(),
                counselor.getReviewCount(),
                counselor.getCounselingMethods(),
                counselor.getCounselingFields()
        );
    }

}
