package com.example.humorie.consultant.counselor.service;

import com.example.humorie.consultant.counselor.dto.CounselorProfileDto;
import com.example.humorie.consultant.counselor.entity.*;
import com.example.humorie.consultant.counselor.repository.*;
import com.example.humorie.consultant.review.dto.ReviewDto;
import com.example.humorie.consultant.review.entity.Review;
import com.example.humorie.consultant.review.repository.ReviewRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CounselorService {

    private final CounselorRepository counselorRepository;
    private final ReviewRepository reviewRepository;
    private final CounselingFieldRepository fieldRepository;
    private final EducationRepository educationRepository;
    private final AffiliationRepository affiliationRepository;
    private final CareerRepository careerRepository;

    @Transactional
    public CounselorProfileDto getCounselorProfile(long counselorId) {
        Counselor counselor = counselorRepository.findById(counselorId)
                .orElseThrow(() -> new RuntimeException("Not found counselor"));

        List<Review> reviews = reviewRepository.findByCounselorId(counselorId);

        double totalRating = reviews.stream().mapToDouble(Review::getRating).sum();
        int reviewCount = reviews.size();
        double averageRating = reviewCount > 0 ? totalRating / reviewCount : 0.0;

        counselor.setRating(averageRating);
        counselor.setReviewCount(reviewCount);
        counselorRepository.save(counselor);

        List<ReviewDto> reviewDTOs = reviews.stream()
                .sorted(Comparator.comparingInt(Review::getRecommendationCount).reversed())
                .map(review -> ReviewDto.builder()
                        .content(review.getContent())
                        .rating(review.getRating())
                        .recommendationCount(review.getRecommendationCount())
                        .createdAt(review.getCreatedAt())
                        .build())
                .collect(Collectors.toList());

        Set<String> affiliations = affiliationRepository.findByCounselorId(counselor.getId()).stream()
                .map(Affiliation::getSocietyName)
                .collect(Collectors.toSet());

        List<String> educations = educationRepository.findByCounselorId(counselor.getId()).stream()
                .map(Education::getContent)
                .collect(Collectors.toList());

        List<String> careers = careerRepository.findByCounselorId(counselor.getId()).stream()
                .map(Career::getContent)
                .collect(Collectors.toList());

        Set<String> counselingFields = fieldRepository.findByCounselorId(counselor.getId()).stream()
                .map(CounselingField::getField)
                .collect(Collectors.toSet());

        return CounselorProfileDto.builder()
                .counselorId(counselor.getId())
                .name(counselor.getName())
                .phoneNumber(counselor.getPhoneNumber())
                .email(counselor.getEmail())
                .rating(counselor.getRating())
                .affiliations(affiliations)
                .educations(educations)
                .careers(careers)
                .counselingCount(counselor.getCounselingCount())
                .reviewCount(counselor.getReviewCount())
                .counselingFields(counselingFields)
                .reviews(reviewDTOs)
                .build();
    }

}
