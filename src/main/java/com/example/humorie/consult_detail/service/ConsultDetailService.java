package com.example.humorie.consult_detail.service;

import com.example.humorie.account.service.AccountService;
import com.example.humorie.consult_detail.dto.response.LatestConsultDetailResDto;
import com.example.humorie.account.entity.AccountDetail;
import com.example.humorie.account.jwt.PrincipalDetails;
import com.example.humorie.consult_detail.entity.ConsultDetail;
import com.example.humorie.consult_detail.repository.ConsultDetailRepository;
import com.example.humorie.global.exception.ErrorCode;
import com.example.humorie.global.exception.ErrorException;
import org.springframework.beans.factory.annotation.Autowired;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
@Service
@RequiredArgsConstructor
@Slf4j
public class ConsultDetailService {
    private final AccountService accountService;
    private final ConsultDetailRepository consultDetailRepository;

    // 가장 최근 상담 내역 조회
    public LatestConsultDetailResDto getLatestConsultDetailResponse(PrincipalDetails principalDetails) {
        AccountDetail accountDetail = principalDetails.getAccountDetail();
        if (accountDetail == null) {
            throw new ErrorException(ErrorCode.NONE_EXIST_USER);
        }
        log.info("Account ID: " + accountDetail.getId());

        ConsultDetail consultDetail = consultDetailRepository.findLatestConsultDetail(accountDetail).orElse(null);

        if (consultDetail == null) {
            log.info("No consult details found for account ID: {}", accountDetail.getId());
            return null;
        }

        return LatestConsultDetailResDto.fromEntity(consultDetail);
    }

}
