package com.example.humorie.notice.repository;

import com.example.humorie.notice.entity.Notice;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface NoticeRepository extends JpaRepository<Notice, Long> {
    // 공지사항 전체 조회(중요도, 날짜, 시간)
    @Query("SELECT n FROM Notice n ORDER BY n.importance DESC, n.createdDate DESC, n.createdTime DESC")
    Page<Notice> findImportantAndRecentNotices(Pageable pageable);

    // 중요 공지사항 조회
    @Query("SELECT n FROM Notice n WHERE n.importance = true ORDER BY n.createdDate DESC, n.createdTime DESC")
    List<Notice> findImportantNotices(Pageable pageable);

    // 제목이나 내용에 키워드가 포함된 공지사항을 검색하는 메서드
    @Query("SELECT n FROM Notice n WHERE n.title LIKE %:keyword% OR n.content LIKE %:keyword% ORDER BY n.createdDate DESC, n.createdTime DESC")
    Page<Notice> findByTitleContainingOrContentContaining(@Param("keyword") String keyword, Pageable pageable);

    // 공지사항을 날짜와 시간 순으로 내림차순 정렬하여 전체 목록 반환
    List<Notice> findAllByOrderByCreatedDateDescCreatedTimeDesc();
}
