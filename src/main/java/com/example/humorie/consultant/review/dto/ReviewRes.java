package com.example.humorie.consultant.review.dto;

import com.example.humorie.consultant.review.entity.Review;
import lombok.*;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReviewDto {

    private String content;

    private double rating;

    private int recommendationCount;

    private LocalDateTime createdAt;

}
