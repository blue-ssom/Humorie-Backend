package com.example.humorie.consult_detail.service;

import com.example.humorie.account.service.AccountService;
import com.example.humorie.consult_detail.dto.response.ConsultDetailPageDto;
import com.example.humorie.consult_detail.dto.response.LatestConsultDetailResDto;
import com.example.humorie.consult_detail.dto.response.ConsultDetailListDto;
import com.example.humorie.account.entity.AccountDetail;
import com.example.humorie.account.jwt.PrincipalDetails;
import com.example.humorie.consult_detail.dto.response.SpecificConsultDetailDto;
import com.example.humorie.consult_detail.entity.ConsultDetail;
import com.example.humorie.consult_detail.repository.ConsultDetailRepository;
import com.example.humorie.global.exception.ErrorCode;
import com.example.humorie.global.exception.ErrorException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

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

    // 상담 내역 전체 조회
    public ConsultDetailPageDto findAllConsultDetail(PrincipalDetails principalDetails, Pageable pageable) {
        // PrincipalDetails 객체에서 AccountDetail 객체를 가져옴
        AccountDetail accountDetail = principalDetails.getAccountDetail();
        if (accountDetail == null) {
            throw new ErrorException(ErrorCode.NONE_EXIST_USER);
        }
        log.info("Account ID: " + accountDetail.getId());

        // Page 객체를 가져옴
        Page<ConsultDetail> consultDetails = consultDetailRepository.findAllConsultDetail(accountDetail, pageable);

        // Page 객체를 ConsultDetailListDto로 변환
        Page<ConsultDetailListDto> consultDetailListDtos = consultDetails.map(ConsultDetailListDto::fromEntity);
        log.info("ConsultDetailService Requested page number: {}, Total pages available: {}", pageable.getPageNumber(), consultDetailListDtos.getTotalPages());

        // 총 페이지 수보다 요청된 페이지 번호가 클 경우 예외 처리
        if (pageable.getPageNumber() > consultDetailListDtos.getTotalPages()) {
            log.error("Page number {} exceeds total pages {}", pageable.getPageNumber() + 1, consultDetailListDtos.getTotalPages());
            throw new ErrorException(ErrorCode.REQUEST_ERROR);
        }

        // Page 정보를 포함한 ConsultDetailPageDto로 반환
        return new ConsultDetailPageDto(
                consultDetailListDtos.getContent(),
                consultDetailListDtos.getNumber() + 1, // 1 기반 페이지 번호로 변경
                consultDetailListDtos.getSize(),
                consultDetailListDtos.getTotalElements(),
                consultDetailListDtos.getTotalPages()
        );
    }

    // 특정 상담 내역 조회
    public SpecificConsultDetailDto getSpecificConsultDetail(Long id) {
        // Spring Data JPA가 기본적으로 제공하는 메서드
        // 주어진 id에 해당하는 엔티티를 데이터베이스에서 조회
        ConsultDetail consultDetail = consultDetailRepository.findById(id)
                .orElseThrow(() -> new ErrorException(ErrorCode.NONE_EXIST_CONSULT_DETAIL));

        // content가 존재하면 status를 true로 변경
        if (consultDetail.getContent() != null && !consultDetail.getContent().isEmpty()) {
            consultDetail.setStatus(true);
            consultDetailRepository.save(consultDetail); // 상태 업데이트 저장
        } else {
            // content가 없을 경우 예외 발생
            throw new ErrorException(ErrorCode.CONSULT_DETAIL_NOT_COMPLETED);
        }

        return SpecificConsultDetailDto.fromEntity(consultDetail);
    }
}
