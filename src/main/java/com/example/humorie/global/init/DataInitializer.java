package com.example.humorie.global.init;

import com.example.humorie.account.config.SecurityConfig;
import com.example.humorie.account.entity.AccountDetail;
import com.example.humorie.account.repository.AccountRepository;
import com.example.humorie.consultant.counselor.entity.*;
import com.example.humorie.consultant.counselor.repository.CounselingFieldRepository;
import com.example.humorie.consultant.counselor.repository.CounselingMethodRepository;
import com.example.humorie.consultant.counselor.repository.CounselorRepository;
import com.example.humorie.consultant.counselor.repository.SymptomRepository;
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
    private final CounselingMethodRepository methodRepository;
    private final CounselingFieldRepository fieldRepository;
    private final SymptomRepository symptomRepository;
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

        Counselor counselor1 = Counselor.builder()
                .name("김가을")
                .phoneNumber("01000000000")
                .email("rkdmf@naver.com")
                .gender("여성")
                .region("서울시 강남구")
                .rating(4.8)
                .counselingCount(17)
                .reviewCount(8)
                .build();

        Counselor counselor2 = Counselor.builder()
                .name("김겨울")
                .phoneNumber("01011111111")
                .email("rudnf@naver.com")
                .gender("남성")
                .region("서울시 강남구")
                .rating(4.1)
                .counselingCount(30)
                .reviewCount(19)
                .build();

        Counselor counselor3 = Counselor.builder()
                .name("이우정")
                .phoneNumber("01022222222")
                .email("dnwjd@naver.com")
                .gender("여성")
                .region("서울시 강남구")
                .rating(4.5)
                .counselingCount(22)
                .build();

        CounselingMethod method1 = CounselingMethod.builder().method("online").counselor(counselor1).build();
        CounselingMethod method2 = CounselingMethod.builder().method("offline").counselor(counselor1).build();
        CounselingMethod method3 = CounselingMethod.builder().method("online").counselor(counselor2).build();
        CounselingMethod method4 = CounselingMethod.builder().method("offline").counselor(counselor3).build();

        CounselingField field1 = CounselingField.builder().field("청소년").counselor(counselor1).build();
        CounselingField field2 = CounselingField.builder().field("개인").counselor(counselor1).build();
        CounselingField field3 = CounselingField.builder().field("청소년").counselor(counselor2).build();
        CounselingField field4 = CounselingField.builder().field("집단").counselor(counselor2).build();
        CounselingField field5 = CounselingField.builder().field("중독").counselor(counselor2).build();
        CounselingField field6 = CounselingField.builder().field("청소년").counselor(counselor3).build();

        Symptom symptom1 = Symptom.builder().symptom("우울").counselor(counselor1).build();
        Symptom symptom2 = Symptom.builder().symptom("대인관계").counselor(counselor1).build();
        Symptom symptom3 = Symptom.builder().symptom("우울").counselor(counselor2).build();
        Symptom symptom4 = Symptom.builder().symptom("자살").counselor(counselor2).build();
        Symptom symptom5 = Symptom.builder().symptom("사회부적응").counselor(counselor2).build();
        Symptom symptom6 = Symptom.builder().symptom("우울").counselor(counselor3).build();


        Review review1 = Review.builder().content("좋아요").rating(4.7).recommendationCount(30).createdAt(LocalDateTime.of(2024, 5, 7, 12, 30, 00)).account(accountDetail1).counselor(counselor1).build();
        Review review2 = Review.builder().content("도움돼요").rating(4.5).recommendationCount(7).createdAt(LocalDateTime.of(2024, 5, 15, 13, 00, 00)).account(accountDetail1).counselor(counselor1).build();
        Review review3 = Review.builder().content("별로에요").rating(3.0).recommendationCount(29).createdAt(LocalDateTime.of(2024, 5, 12, 8, 30, 00)).account(accountDetail2).counselor(counselor2).build();
        Review review4 = Review.builder().content("괜찮아요").rating(4.4).recommendationCount(12).createdAt(LocalDateTime.of(2024, 5, 20, 17, 00, 00)).account(accountDetail2).counselor(counselor1).build();


        counselorRepository.saveAll(Arrays.asList(counselor1, counselor2, counselor3));
        methodRepository.saveAll(Arrays.asList(method1, method2, method3, method4));
        fieldRepository.saveAll(Arrays.asList(field1, field2, field3, field4, field5, field6));
        symptomRepository.saveAll(Arrays.asList(symptom1, symptom2, symptom3, symptom4, symptom5, symptom6));
        reviewRepository.saveAll(Arrays.asList(review1, review2, review3, review4));

    }
}
