package com.example.humorie.init;

import com.example.humorie.account.config.SecurityConfig;
import com.example.humorie.account.entity.AccountDetail;
import com.example.humorie.account.repository.AccountRepository;
import com.example.humorie.consultant.counselor.entity.CounselingField;
import com.example.humorie.consultant.counselor.entity.Counselor;
import com.example.humorie.consultant.counselor.entity.Symptom;
import com.example.humorie.consultant.counselor.repository.CounselorRepository;
import com.example.humorie.consultant.review.entity.Review;
import com.example.humorie.consultant.review.repository.ReviewRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;

@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final AccountRepository accountRepository;
    private final CounselorRepository counselorRepository;
    private final ReviewRepository reviewRepository;
    private final SecurityConfig securityConfig;


    @Override
    @Transactional
    public void run(String... args) throws Exception {
        AccountDetail accountDetail1 = accountRepository.findByEmail("test1@naver.com")
                .orElseGet(() -> accountRepository.save(AccountDetail.joinAccount(
                        "test1@naver.com",
                        securityConfig.passwordEncoder().encode("test1234!"),
                        "test123",
                        "김봄")));

        AccountDetail accountDetail2 = accountRepository.findByEmail("test2@naver.com")
                .orElseGet(() -> accountRepository.save(AccountDetail.joinAccount(
                        "test2@naver.com",
                        securityConfig.passwordEncoder().encode("test1234!"),
                        "test234",
                        "김여름")));

        Counselor counselor1 = Counselor.builder().
                name("김가을")
                .rating(4.8)
                .counselingCount(17)
                .reviewCount(8)
                .counselingFields(new HashSet<>(Arrays.asList(CounselingField.DIVORCE, CounselingField.FORENSIC_SCIENCE)))
                .specialties(new ArrayList<>(Arrays.asList(Symptom.우울, Symptom.불안, Symptom.화병)))
                .build();

        Counselor counselor2 = Counselor.builder()
                .name("김겨울")
                .rating(4.1)
                .counselingCount(30)
                .reviewCount(19)
                .counselingFields(new HashSet<>(Arrays.asList(CounselingField.FAMILY)))
                .specialties(new ArrayList<>(Arrays.asList(Symptom.스트레스, Symptom.충동)))
                .build();

        Counselor counselor3 = Counselor.builder()
                .name("이우정")
                .rating(4.5)
                .counselingCount(22)
                .counselingFields(new HashSet<>(Arrays.asList(CounselingField.DIVORCE, CounselingField.MARRIAGE, CounselingField.FAMILY)))
                .specialties(new ArrayList<>(Arrays.asList(Symptom.신체화, Symptom.불안, Symptom.스트레스)))
                .build();


        Review review1 = Review.builder().content("좋아요").rating(4.7).recommendationCount(30).createdAt(LocalDateTime.of(2024, 5, 7, 12, 30, 00)).account(accountDetail1).counselor(counselor1).build();
        Review review2 = Review.builder().content("도움돼요").rating(4.5).recommendationCount(7).createdAt(LocalDateTime.of(2024, 5, 15, 13, 00, 00)).account(accountDetail1).counselor(counselor1).build();
        Review review3 = Review.builder().content("별로에요").rating(3.0).recommendationCount(29).createdAt(LocalDateTime.of(2024, 5, 12, 8, 30, 00)).account(accountDetail2).counselor(counselor2).build();
        Review review4 = Review.builder().content("괜찮아요").rating(4.4).recommendationCount(12).createdAt(LocalDateTime.of(2024, 5, 20, 17, 00, 00)).account(accountDetail2).counselor(counselor1).build();


        counselorRepository.saveAll(Arrays.asList(counselor1, counselor2, counselor3));
        reviewRepository.saveAll(Arrays.asList(review1, review2, review3, review4));

    }
}
