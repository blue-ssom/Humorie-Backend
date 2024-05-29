package com.example.humorie.recommend.dto;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class RecommendReviewDto {

    private final long id;

    private final String content;

    private final double rating;

    private final int recommendationCount;

    private final LocalDateTime createdAt;

    private final String accountName;

    private final double recommendationRating;

    public RecommendReviewDto(long id, String content, double rating, int recommendationCount, LocalDateTime createdAt, String accountName, double recommendationRating) {
        this.id = id;
        this.content = content;
        this.rating = rating;
        this.recommendationCount = recommendationCount;
        this.createdAt = createdAt;
        this.accountName = accountName;
        this.recommendationRating = recommendationRating;
    }
}
