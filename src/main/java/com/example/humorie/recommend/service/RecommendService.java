package com.example.humorie.recommend.service;

import com.example.humorie.account.jwt.PrincipalDetails;
import com.example.humorie.counselor.entity.CounselingField;
import com.example.humorie.counselor.entity.Counselor;
import com.example.humorie.counselor.repository.CounselorRepository;
import com.example.humorie.recommend.dto.RecommendCounselorDto;
import com.example.humorie.recommend.dto.RecommendReviewDto;
import com.example.humorie.reservation.entity.Reservation;
import com.example.humorie.reservation.repository.ReservationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RecommendService {

    private final CounselorRepository counselorRepository;
    private final ReservationRepository reservationRepository;

    private final Double RECOMMENDATION_RATING_BY_REVIEW_COUNT = 0.02; // 리뷰 수에 따른 추천도
    private final Double RECOMMENDATION_RATING_BY_COUNSELING_COUNT = 0.01; // 상담 수에 따른 추천도
    private final Double RECOMMENDATION_RATING_BY_SAME_COUNSELOR = 0.05; // 같은 상담원에 따른 추천도
    private final Double RECOMMENDATION_RATING_BY_SAME_FIELD = 0.05; // 같은 상담 분야에 따른 추천도

    public List<RecommendCounselorDto> recommendCounselor(PrincipalDetails principal){
        List<Counselor> counselors = counselorRepository.findAll();

        if(principal == null){
            return getRecommendCounselorsWithoutLogin(counselors);
        }
        List<Reservation> reservations = reservationRepository.findAllByAccountEmailOrderByCreatedAtDesc(principal.getUsername());

        return getRecommendCounselorsWithLogin(counselors, reservations);
    }


    private List<RecommendCounselorDto> getRecommendCounselorsWithoutLogin(List<Counselor> counselors) {
        return counselors.stream()
                .map(counselor -> new RecommendCounselorDto(counselor.getId(), counselor.getName(), counselor.getQualification(),
                        counselor.getCounselingFields(),counselor.getRating(),counselor.getReviewCount(), calculateRatingWithNotLogin(counselor)))
                .sorted(Comparator.comparing(RecommendCounselorDto::getRecommendationRating).reversed())
                .collect(Collectors.toList());
    }

    private List<RecommendCounselorDto> getRecommendCounselorsWithLogin(List<Counselor> counselors, List<Reservation> reservations) {
        return counselors.stream()
                .map(counselor -> new RecommendCounselorDto(counselor.getId(), counselor.getName(), counselor.getQualification(),
                        counselor.getCounselingFields(),counselor.getRating(),counselor.getReviewCount(), calculateRatingWithLogin(counselor, reservations)))
                .sorted(Comparator.comparing(RecommendCounselorDto::getRecommendationRating).reversed())
                .collect(Collectors.toList());
    }

    private Double calculateRatingWithNotLogin(Counselor counselor) {
        return baseRating(counselor);
    }

    private Double calculateRatingWithLogin(Counselor counselor, List<Reservation> reservations) {
        double baseRating = baseRating(counselor);
        double additionalRating = additionalRating(counselor, reservations);
        return baseRating + additionalRating;
    }

    private double baseRating(Counselor counselor) {
        return counselor.getRating()
                + counselor.getReviewCount() * RECOMMENDATION_RATING_BY_REVIEW_COUNT
                + counselor.getCounselingCount() * RECOMMENDATION_RATING_BY_COUNSELING_COUNT;
    }

    private double additionalRating(Counselor counselor, List<Reservation> reservations) {
        Set<CounselingField> combinedFields = reservations.stream()
                .map(Reservation::getCounselor)
                .flatMap(reservedCounselor -> reservedCounselor.getCounselingFields().stream())
                .collect(Collectors.toSet());

        return reservations.stream()
                .mapToDouble(reservation -> {
                    double rating = 0;
                    if (reservation.getCounselor().getId() == counselor.getId()) {
                        rating += RECOMMENDATION_RATING_BY_SAME_COUNSELOR;
                    }
                    rating += combinedFields.stream()
                            .filter(counselor.getCounselingFields()::contains)
                            .count() * RECOMMENDATION_RATING_BY_SAME_FIELD;
                    return rating;
                })
                .sum();
    }
}
