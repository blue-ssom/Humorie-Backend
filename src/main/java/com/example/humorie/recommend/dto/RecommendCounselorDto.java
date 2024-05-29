package com.example.humorie.recommend.dto;

import com.example.humorie.counselor.entity.CounselingField;
import com.example.humorie.counselor.entity.Counselor;
import lombok.Getter;

import java.util.Set;

@Getter
public class RecommendCounselorDto{

    private final long id;

    private final String name;

    private final String qualification;

    private final Set<CounselingField> counselingFields;

    private final double rating;

    private final int reviewCount;

    private final double recommendationRating;

    public RecommendCounselorDto(long id, String name, String qualification, Set<CounselingField> counselingFields,
                                 double rating, int reviewCount, double recommendationRating){
        this.id = id;
        this.name = name;
        this.qualification = qualification;
        this.counselingFields = counselingFields;
        this.rating = rating;
        this.reviewCount = reviewCount;
        this.recommendationRating = recommendationRating;
    }

}
