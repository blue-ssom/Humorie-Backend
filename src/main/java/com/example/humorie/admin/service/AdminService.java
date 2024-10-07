package com.example.humorie.admin.service;

import com.example.humorie.account.entity.AccountDetail;
import com.example.humorie.account.jwt.PrincipalDetails;
import com.example.humorie.account.service.EmailService;
import com.example.humorie.admin.dto.ConsultDetailDto;
import com.example.humorie.admin.mapper.AdminMapper;
import com.example.humorie.consultant.consult_detail.entity.ConsultDetail;
import com.example.humorie.consultant.consult_detail.repository.ConsultDetailRepository;
import com.example.humorie.consultant.counselor.entity.Counselor;
import com.example.humorie.consultant.counselor.repository.CounselorRepository;
import com.example.humorie.global.exception.ErrorCode;
import com.example.humorie.global.exception.ErrorException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

@Service
@RequiredArgsConstructor
public class AdminService {

    private final CounselorRepository counselorRepository;
    private final ConsultDetailRepository consultDetailRepository;
    private final AdminMapper adminMapper;
    private final EmailService emailService;

    @Transactional
    public String saveConsultDetail(ConsultDetailDto consultDetailDto, Long counselorId, PrincipalDetails principal) {
        AccountDetail account = principal.getAccountDetail();

        Counselor counselor = counselorRepository.findById(counselorId)
                .orElseThrow(() -> new ErrorException(ErrorCode.NON_EXIST_COUNSELOR));

        LocalDateTime counselDate = parseCounselDateTime(consultDetailDto.getCounselDate());

        ConsultDetail consultDetail = adminMapper.dtoToConsultDetail(consultDetailDto);
            consultDetail.setAccount(account);
            consultDetail.setCounselor(counselor);
            consultDetail.setCounselDate(counselDate.toLocalDate());
            consultDetail.setCounselTime(counselDate.toLocalTime());
            consultDetail.setStatus(true);
            counselor.setCounselingCount(counselor.getCounselingCount() + 1);

        consultDetailRepository.save(consultDetail);
        counselorRepository.save(counselor);

        try {
            emailService.sendConsultationCompletionEmail(account.getEmail(), consultDetailDto.getCounselDate(), counselor.getName());
        } catch (Exception e) {
            throw new ErrorException(ErrorCode.SEND_EMAIL_FAILED);
        }

        return "Consultation details saved successfully";
    }

    private LocalDateTime parseCounselDateTime(String dateTimeString) {
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
            return LocalDateTime.parse(dateTimeString, formatter);
        } catch (DateTimeParseException e) {
            throw new ErrorException(ErrorCode.INVALID_DATE_TIME_FORMAT);
        }
    }

}