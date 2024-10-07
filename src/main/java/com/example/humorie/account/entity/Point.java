package com.example.humorie.mypage.entity;

import com.example.humorie.account.entity.AccountDetail;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Point {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private int points;

    private String title;

    //earn or spend
    private String type;

    private LocalDateTime transactionDate;

    @ManyToOne(fetch = FetchType.LAZY)
    private AccountDetail account;

    public Point(AccountDetail accountDetail, int points, String title, String type) {
        this.account = accountDetail;
        this.points = points;
        this.title = title;
        this.type = type;
        this.transactionDate = LocalDateTime.now();
    }

}
