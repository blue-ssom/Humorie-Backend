package com.example.humorie.consultant.counselor.service;

import com.example.humorie.consultant.counselor.dto.CounselorProfileDto;
import com.example.humorie.consultant.counselor.entity.Counselor;
import com.example.humorie.consultant.counselor.repository.CounselorRepository;
import com.example.humorie.consultant.review.dto.ReviewDto;
import com.example.humorie.consultant.review.entity.Review;
import com.example.humorie.consultant.review.repository.ReviewRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CounselorService {

    private final CounselorRepository counselorRepository;
    private final ReviewRepository reviewRepository;

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

        return CounselorProfileDto.builder()
                .counselorId(counselor.getId())
                .name(counselor.getName())
                .phoneNumber(counselor.getPhoneNumber())
                .email(counselor.getEmail())
                .rating(counselor.getRating())
                .affiliations(counselor.getAffiliations())
                .educations(counselor.getEducations())
                .careers(counselor.getCareers())
                .counselingCount(counselor.getCounselingCount())
                .reviewCount(counselor.getReviewCount())
                .counselingField(counselor.getCounselingFields())
                .reviews(reviewDTOs)
                .build();
    }

}
