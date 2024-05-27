package com.example.humorie.consultant.review.entity;

import com.example.humorie.account.entity.AccountDetail;
import com.example.humorie.consultant.counselor.entity.Counselor;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Review {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String content;

    private double rating;

    private int recommendationCount;

    private LocalDateTime createdAt;

    @ManyToOne(fetch = FetchType.LAZY)
    private AccountDetail account;

    @ManyToOne(fetch = FetchType.LAZY)
    private Counselor counselor;

}
