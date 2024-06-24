package com.example.humorie.consultant.counselor.entity;

import com.example.humorie.consultant.review.entity.Review;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;
import java.util.Set;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
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

    @ElementCollection(targetClass = Symptom.class)
    @Enumerated(EnumType.STRING)
    private List<Symptom> specialties;


    @OneToMany(mappedBy = "counselor", cascade = CascadeType.ALL)
    private List<Review> reviews;

}
