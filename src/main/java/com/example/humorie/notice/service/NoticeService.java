package com.example.humorie.notice.service;

import com.example.humorie.global.exception.ErrorCode;
import com.example.humorie.global.exception.ErrorException;
import com.example.humorie.notice.dto.*;
import com.example.humorie.notice.entity.Notice;
import com.example.humorie.notice.repository.NoticeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class NoticeService {
    private final NoticeRepository noticeRepository;

    public NoticePageDto getAllNotices(Pageable pageable) {
        Page<Notice> noticePage = noticeRepository.findImportantAndRecentNotices(pageable);



        // 페이지가 비어 있는지 확인
        if (noticePage.isEmpty()) {
            throw new ErrorException(ErrorCode.NO_CONTENT);
        }

        // 엔티티를 DTO로 변환하여 NoticePageDto로 반환
        Page<GetAllNoticeDto> dtoPage = noticePage.map(GetAllNoticeDto::fromEntity);
        return NoticePageDto.from(dtoPage);
    }

    public NoticePageDto searchNotices(String keyword, Pageable pageable) {
        // 중요 공지사항 먼저 가져오기
        List<Notice> importantNotices = noticeRepository.findImportantNotices(pageable);

        // 검색 결과 가져오기
        Page<Notice> searchResults = noticeRepository.findByTitleContainingOrContentContaining(keyword, keyword, pageable);

        // 검색 결과가 비어 있을 경우 예외 처리
        if (importantNotices.isEmpty() && searchResults.isEmpty()) {
            throw new ErrorException(ErrorCode.NO_SEARCH_RESULTS);
        }

        // 중요 공지사항과 검색 결과를 병합하고 중복 제거
        Set<Notice> distinctResults = new LinkedHashSet<>();
        distinctResults.addAll(importantNotices);
        distinctResults.addAll(searchResults.getContent());

        // 병합된 결과를 날짜 및 시간 순으로 정렬
        List<Notice> sortedResults = new ArrayList<>(distinctResults);
        sortedResults.sort((n1, n2) -> {
            // 두 공지사항의 createdDate(생성일자)가 동일한 경우, createdTime(생성시간)을 비교
            if (n1.getCreatedDate().isEqual(n2.getCreatedDate())) {
                return n2.getCreatedTime().compareTo(n1.getCreatedTime());
            }
            // 날짜가 최신일수록 리스트의 앞쪽에 위치
            return n2.getCreatedDate().compareTo(n1.getCreatedDate());
        });

        // 병합된 결과를 DTO로 변환
        List<GetAllNoticeDto> dtoList = new ArrayList<>();
        for (Notice notice : sortedResults) {
            dtoList.add(GetAllNoticeDto.fromEntity(notice));
        }
        // 병합된 결과를 페이지네이션
        // 페이지네이션을 위해 시작점(start)과 끝점(end) 인덱스를 계산
        int start = Math.min((int) pageable.getOffset(), dtoList.size());
        int end = Math.min((start + pageable.getPageSize()), dtoList.size());

        // 페이지 번호가 전체 페이지 수를 초과하는 경우 예외 처리
        if (pageable.getPageNumber() >= Math.ceil((double) dtoList.size() / pageable.getPageSize())) {
            throw new ErrorException(ErrorCode.INVALID_PAGE_NUMBER);
        }

        Page<GetAllNoticeDto> dtoPage = new PageImpl<>(dtoList.subList(start, end), pageable, dtoList.size());

        return NoticePageDto.from(dtoPage);
    }

    public  NoticeDetailWithNavigationDto getNoticeWithNavigation(Long id) {
        Notice notice = noticeRepository.findById(id)
                .orElseThrow(() -> new ErrorException(ErrorCode.NONE_EXIST_NOTICE));

        // 조회수 1 증가
        notice.setViewCount(notice.getViewCount() + 1);
        noticeRepository.save(notice); // 변경된 조회수를 데이터베이스에 저장

        // 공지사항 목록을 날짜와 시간 순으로 정렬하여 가져옴
        List<Notice> notices = noticeRepository.findAllByOrderByCreatedDateDescCreatedTimeDesc();

        // 현재 공지사항의 위치 파악
        int currentIndex = notices.indexOf(notice);

        // 이전 공지사항을 위한 변수
        NoticeSummaryDto previousNotice = null;

        // 다음 공지사항을 위한 변수
        NoticeSummaryDto nextNotice = null;

        // 현재 공지사항이 첫 번째가 아닌 경우에만 이전 공지사항을 설정
        if (currentIndex > 0) {
            Notice previousNoticeEntity = notices.get(currentIndex - 1); // 이전 공지사항 엔티티를 가져옴
            previousNotice = new NoticeSummaryDto(previousNoticeEntity.getId(), previousNoticeEntity.getTitle());
        }


        // 현재 공지사항이 마지막이 아닌 경우에만 다음 공지사항을 설정
        if (currentIndex < notices.size() - 1) {
            Notice nextNoticeEntity = notices.get(currentIndex + 1); // 다음 공지사항 엔티티를 가져옴
            nextNotice = new NoticeSummaryDto(nextNoticeEntity.getId(), nextNoticeEntity.getTitle());
        }

        // 현재 공지사항 DTO 생성
        NoticeDetailDto currentNoticeDto = NoticeDetailDto.fromEntity(notice);

        // 이전 공지사항, 현재 공지사항, 다음 공지사항을 포함한 DTO를 반환
        return new NoticeDetailWithNavigationDto(currentNoticeDto, previousNotice, nextNotice);
    }
}
