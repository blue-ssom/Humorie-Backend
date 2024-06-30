package com.example.humorie.consultant.counselor.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CounselingField {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String field;

    @ManyToOne(fetch = FetchType.LAZY)
    private Counselor counselor;

}
