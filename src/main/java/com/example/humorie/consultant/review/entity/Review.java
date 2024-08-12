package com.example.humorie.consultant.review.entity;

import com.example.humorie.account.entity.AccountDetail;
import com.example.humorie.consultant.counselor.entity.Counselor;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.Set;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Review {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String title;

    private String content;

    private Double rating;

    private String symptom;

    private int recommendationCount;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @ManyToOne(fetch = FetchType.LAZY)
    private AccountDetail account;

    @ManyToOne(fetch = FetchType.LAZY)
    private Counselor counselor;

    @OneToMany(mappedBy = "review", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Set<ReviewTag> reviewTags;

}
