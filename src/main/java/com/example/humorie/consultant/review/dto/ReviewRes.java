package com.example.humorie.consultant.review.dto;

import lombok.*;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReviewRes {

    private long reviewId;

    private String title;

    private String content;

    private Double rating;

    private LocalDateTime createdAt;

}
