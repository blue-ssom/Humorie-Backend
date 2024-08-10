package com.example.humorie.notice.repository;

import com.example.humorie.notice.entity.Notice;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface NoticeRepository extends JpaRepository<Notice, Long> {
    @Query("SELECT n FROM Notice n ORDER BY n.importance DESC, n.createdDate DESC, n.createdTime DESC")
    Page<Notice> findImportantAndRecentNotices(Pageable pageable);
}
