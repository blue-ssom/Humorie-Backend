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

    private String phoneNumber;

    private String email;

    private String gender;

    private String region;

    private String qualification;

    private double rating;

    private int counselingCount;

    private int reviewCount;

    private String introduction;

    @OneToMany(mappedBy = "counselor", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Affiliation> affiliations;

    @OneToMany(mappedBy = "counselor", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Education> educations;

    @OneToMany(mappedBy = "counselor", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Career> careers;

    @OneToMany(mappedBy = "counselor", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<CounselingMethod> counselingMethods;

    @OneToMany(mappedBy = "counselor", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Symptom> symptoms;

    @OneToMany(mappedBy = "counselor", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Review> reviews;

}
