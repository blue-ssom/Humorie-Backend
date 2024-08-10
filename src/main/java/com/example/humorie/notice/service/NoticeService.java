package com.example.humorie.notice.service;

import com.example.humorie.global.exception.ErrorCode;
import com.example.humorie.global.exception.ErrorException;
import com.example.humorie.notice.dto.GetAllNoticeDto;
import com.example.humorie.notice.dto.NoticePageDto;
import com.example.humorie.notice.entity.Notice;
import com.example.humorie.notice.repository.NoticeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class NoticeService {
    private final NoticeRepository noticeRepository;

    public NoticePageDto getAllNotices(Pageable pageable) {
        Page<Notice> noticePage = noticeRepository.findImportantAndRecentNotices(pageable);

        // 페이지 번호가 전체 페이지 수를 초과하는 경우
        if (pageable.getPageNumber() >= noticePage.getTotalPages()) {
            throw new ErrorException(ErrorCode.INVALID_PAGE_NUMBER);
        }

        // 페이지가 비어 있는지 확인
        if (noticePage.isEmpty()) {
            throw new ErrorException(ErrorCode.NO_CONTENT);
        }

        // 엔티티를 DTO로 변환하여 NoticePageDto로 반환
        Page<GetAllNoticeDto> dtoPage = noticePage.map(GetAllNoticeDto::fromEntity);
        return NoticePageDto.from(dtoPage);
    }
}
