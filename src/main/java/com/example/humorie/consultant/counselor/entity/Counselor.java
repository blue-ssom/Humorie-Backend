package com.example.humorie.consultant.counselor.entity;

import com.example.humorie.consultant.review.entity.Review;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Set;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Counselor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String name;

    private double rating;

    private int counselingCount;

    private int reviewCount;

    @ElementCollection(targetClass = CounselingField.class)
    @CollectionTable(name = "counselor_counseling_fields", joinColumns = @JoinColumn(name = "counselor_id"))
    @Column(name = "counseling_field")
    @Enumerated(EnumType.STRING)
    private Set<CounselingField> counselingFields;


    @OneToMany(mappedBy = "counselor", cascade = CascadeType.ALL)
    private List<Review> reviews;

}
