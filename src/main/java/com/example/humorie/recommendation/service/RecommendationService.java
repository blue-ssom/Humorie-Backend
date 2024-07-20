package com.example.humorie.recommendation.service;

import com.example.humorie.account.jwt.PrincipalDetails;

import com.example.humorie.consultant.counselor.entity.CounselingField;
import com.example.humorie.consultant.counselor.entity.Counselor;
import com.example.humorie.consultant.counselor.repository.CounselorRepository;
import com.example.humorie.consultant.review.entity.Review;
import com.example.humorie.consultant.review.repository.ReviewRepository;
import com.example.humorie.recommendation.dto.RecommendationCounselorDto;
import com.example.humorie.recommendation.dto.RecommendationReviewDto;
import com.example.humorie.reservation.entity.Reservation;
import com.example.humorie.reservation.repository.ReservationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RecommendationService {

    private final CounselorRepository counselorRepository;
    private final ReservationRepository reservationRepository;
    private final ReviewRepository reviewRepository;
    private final RecommendationCalculator calculator;



    public List<RecommendationCounselorDto> recommendCounselor(PrincipalDetails principal){
        List<Counselor> counselors = counselorRepository.findAll();

        if(principal != null){
            List<Reservation> reservations = reservationRepository.findAllByAccountEmailOrderByCreatedAtDesc(principal.getUsername());
            return getRecommendCounselorsWithLogin(counselors, reservations);

        }

        return getRecommendCounselorsWithoutLogin(counselors);
    }

    public List<RecommendationReviewDto> recommendReview(PrincipalDetails principal){
        List<Review> reviews = reviewRepository.findAll();

        if(principal != null){
            List<Reservation> reservations = reservationRepository.findAllByAccountEmailOrderByCreatedAtDesc(principal.getUsername());
            return getRecommendReviewsWithLogin(reviews, reservations);

        }

        return getRecommendReviewsWithoutLogin(reviews);
    }


    private List<RecommendationCounselorDto> getRecommendCounselorsWithoutLogin(List<Counselor> counselors) {
        return counselors.stream()
                .map(counselor -> new RecommendationCounselorDto(counselor.getId(),
                        counselor.getName(),
                        counselor.getCounselingFields().stream()
                                .map(CounselingField::getField)
                                .collect(Collectors.toSet()),
                        counselor.getRating(),
                        counselor.getReviewCount(),
                        calculator.calculateCounselorRecommendRatingWithoutLogin(counselor)))
                .sorted(Comparator.comparing(RecommendationCounselorDto::getRecommendationRating).reversed())
                .collect(Collectors.toList());
    }

    private List<RecommendationCounselorDto> getRecommendCounselorsWithLogin(List<Counselor> counselors, List<Reservation> reservations) {
        return counselors.stream()
                .map(counselor -> new RecommendationCounselorDto(counselor.getId(),
                        counselor.getName(),
                        counselor.getCounselingFields().stream()
                                .map(CounselingField::getField)
                                .collect(Collectors.toSet()),
                        counselor.getRating(),
                        counselor.getReviewCount(),
                        calculator.calculateCounselorRecommendRatingWithLogin(counselor, reservations)))
                .sorted(Comparator.comparing(RecommendationCounselorDto::getRecommendationRating).reversed())
                .collect(Collectors.toList());
    }

    private List<RecommendationReviewDto> getRecommendReviewsWithoutLogin(List<Review> reviews) {
        return reviews.stream()
                .map(review -> new RecommendationReviewDto(review.getId(),
                        review.getContent(),
                        review.getRating(),
                        review.getRecommendationCount(),
                        review.getCreatedAt(),
                        review.getAccount().getAccountName(),
                        calculator.calculateReviewRecommendRatingWithoutLogin(review)))
                .sorted(Comparator.comparing(RecommendationReviewDto::getRecommendationRating).reversed())
                .collect(Collectors.toList());
    }

    private List<RecommendationReviewDto> getRecommendReviewsWithLogin(List<Review> reviews, List<Reservation> reservations) {
        return reviews.stream()
                .map(review -> new RecommendationReviewDto(review.getId(),
                        review.getContent(),
                        review.getRating(),
                        review.getRecommendationCount(),
                        review.getCreatedAt(),
                        review.getAccount().getAccountName(),
                        calculator.calculateReviewRecommendRatingWithLogin(review, reservations)))
                .sorted(Comparator.comparing(RecommendationReviewDto::getRecommendationRating).reversed())
                .collect(Collectors.toList());
    }


}
