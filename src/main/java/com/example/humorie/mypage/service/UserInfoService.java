package com.example.humorie.mypage.service;

import com.example.humorie.consultant.consult_detail.repository.ConsultDetailRepository;
import com.example.humorie.global.config.SecurityConfig;
import com.example.humorie.account.entity.AccountDetail;
import com.example.humorie.account.jwt.PrincipalDetails;
import com.example.humorie.account.repository.AccountRepository;
import com.example.humorie.consultant.review.repository.ReviewRepository;
import com.example.humorie.global.exception.ErrorCode;
import com.example.humorie.global.exception.ErrorException;
import com.example.humorie.mypage.dto.request.UserInfoDelete;
import com.example.humorie.mypage.dto.request.UserInfoUpdate;
import com.example.humorie.mypage.dto.response.GetUserInfoResDto;
import com.example.humorie.reservation.repository.ReservationRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserInfoService {
    private final ConsultDetailRepository consultDetailRepository;
    private final AccountRepository accountRepository;
    private final ReviewRepository reviewRepository;
    private final ReservationRepository reservationRepository;
    private final UserInfoValidationService userInfoValidationService;
    private final SecurityConfig jwtSecurityConfig;

    // 사용자 정보 조회
    public GetUserInfoResDto getMyAccount(PrincipalDetails principalDetails) {
        AccountDetail account = principalDetails.getAccountDetail();

        if(account == null){
            throw new ErrorException(ErrorCode.NONE_EXIST_USER);
        }

        return GetUserInfoResDto.builder()
                .id(account.getId()) // 식별자
                .email(account.getEmail()) // 이메일
                .accountName(account.getAccountName()) // 아이디
                .name(account.getName()) // 이름
                .emailSubscription(account.getEmailSubscription()) // 이메일 확인
                .build();
    }

    // 사용자 정보 업데이트
    public String updateUserInfo(PrincipalDetails principalDetails, UserInfoUpdate updateDto) {
        AccountDetail account = principalDetails.getAccountDetail();

        // 이름 (필수)
        userInfoValidationService.validateName(updateDto.getName());
        account.setName(updateDto.getName());

        // 비밀번호 (선택 사항)
        // 비밀번호 형식 검사
        userInfoValidationService.validatePassword(updateDto.getNewPassword());
        // 비밀번호 확인 필드 검사
        userInfoValidationService.validatePasswordConfirmation(updateDto.getNewPassword(), updateDto.getPasswordCheck());
        // 비밀번호가 유효하다면 암호화 후 저장
        account.setPassword(jwtSecurityConfig.passwordEncoder().encode(updateDto.getNewPassword()));

        // 이메일 수신 여부 체크 (선택 사항)
        if (updateDto.getEmailSubscription() != null) {
            account.setEmailSubscription(updateDto.getEmailSubscription());
        }

        accountRepository.save(account);

        // 변경된 사용자 정보를 저장
        return "Success Update";
    }

    // 회원 탈퇴
    @Transactional
    public String deleteUserInfo(PrincipalDetails principalDetails, UserInfoDelete deleteDto) {
        AccountDetail account = principalDetails.getAccountDetail();

        // 비밀번호 유효성 검사
        userInfoValidationService.validatePassword(deleteDto.getPassword());
        userInfoValidationService.validatePasswordMatch(deleteDto.getPassword(), account.getPassword());

        // 상담 내역 삭제
         consultDetailRepository.deleteByAccount_Id(account.getId());

        // 예약 삭제
        reservationRepository.deleteByAccount_Id(account.getId());

        // 리뷰 삭제
        reviewRepository.deleteByAccount_Id(account.getId());

        // 사용자 삭제
        accountRepository.deleteById(account.getId());

        return "Success Delete";
    }
}
