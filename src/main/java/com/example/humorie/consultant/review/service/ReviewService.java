package com.example.humorie.consultant.review.service;

import com.example.humorie.account.entity.AccountDetail;
import com.example.humorie.consultant.consult_detail.entity.ConsultDetail;
import com.example.humorie.consultant.consult_detail.repository.ConsultDetailRepository;
import com.example.humorie.consultant.counselor.entity.Counselor;
import com.example.humorie.consultant.counselor.repository.CounselorRepository;
import com.example.humorie.consultant.review.dto.ReviewReq;
import com.example.humorie.consultant.review.dto.ReviewRes;
import com.example.humorie.consultant.review.entity.Review;
import com.example.humorie.consultant.review.mapper.ReviewMapper;
import com.example.humorie.consultant.review.repository.ReviewRepository;
import com.example.humorie.global.exception.ErrorCode;
import com.example.humorie.global.exception.ErrorException;
import com.example.humorie.global.service.CommonService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReviewService {

    private final CounselorRepository counselorRepository;
    private final ReviewRepository reviewRepository;
    private final ConsultDetailRepository consultDetailRepository;
    private final CommonService commonService;
    private final ReviewMapper mapper = ReviewMapper.INSTANCE;

    @Transactional
    public String createReview(String accessToken, long consultId, ReviewReq reviewReq) {
        AccountDetail account = commonService.getAccountFromToken(accessToken);

        ConsultDetail consultDetail = consultDetailRepository.findById(consultId)
                .orElseThrow(() -> new ErrorException(ErrorCode.NONE_EXIST_CONSULT_DETAIL));

        Counselor counselor = consultDetail.getCounselor();

        Review review = mapper.toReview(reviewReq);
        review.setSymptom(consultDetail.getSymptom());
        review.setCreatedAt(LocalDateTime.now());
        review.setAccount(account);
        review.setCounselor(counselor);

        reviewRepository.save(review);

        counselor.setReviewCount(counselor.getReviewCount() + 1);
        updateCounselorRating(counselor);
        counselorRepository.save(counselor);

        return "Creation Success";
    }

    @Transactional
    public String modifyReview(String accessToken, long reviewId, ReviewReq reviewReq) {
        AccountDetail account = commonService.getAccountFromToken(accessToken);

        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new ErrorException(ErrorCode.NONE_EXIST_REVIEW));

        if(!review.getAccount().equals(account)) {
            throw new ErrorException(ErrorCode.REVIEW_PERMISSION_DENIED);
        } else {
            if(reviewReq.getTitle() != null && !reviewReq.getTitle().isEmpty()) {
                review.setTitle(reviewReq.getTitle());
            }
            if(reviewReq.getContent() != null && !reviewReq.getContent().isEmpty()) {
                review.setContent(reviewReq.getContent());
            }
            if(reviewReq.getRating() != null) {
                review.setRating(reviewReq.getRating());
            }

            reviewRepository.save(review);
        }

        return "Modification Success";
    }

    @Transactional
    public String deleteReview(String accessToken, long reviewId) {
        AccountDetail account = commonService.getAccountFromToken(accessToken);

        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new ErrorException(ErrorCode.NONE_EXIST_REVIEW));

        if(!review.getAccount().equals(account)) {
            throw new ErrorException(ErrorCode.REVIEW_PERMISSION_DENIED);
        } else {
            reviewRepository.deleteById(reviewId);
        }

        return "Deletion Success";
    }

    @Transactional(readOnly = true)
    public List<ReviewRes> getReviewListByCounselor(long counselorId) {
        List<Review> reviews = reviewRepository.findByCounselorId(counselorId);

        List<ReviewRes> reviewResList = mapper.toReviewResList(reviews);

        return reviewResList.stream()
                .sorted(Comparator.comparingDouble(ReviewRes::getRating).reversed())
                .collect(Collectors.toList());
    }

    private void updateCounselorRating(Counselor counselor) {
        List<Review> reviews = reviewRepository.findByCounselorId(counselor.getId());
        double averageRating = reviews.stream()
                .mapToDouble(Review::getRating)
                .average()
                .orElse(0.0);
        counselor.setRating(averageRating);
    }

}
