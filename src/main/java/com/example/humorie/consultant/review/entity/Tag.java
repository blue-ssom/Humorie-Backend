package com.example.humorie.consultant.review.entity;

import com.example.humorie.account.entity.AccountDetail;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Tag {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String tagName;

    private String tagContent;

    @ManyToOne(fetch = FetchType.LAZY)
    private AccountDetail account;

}
