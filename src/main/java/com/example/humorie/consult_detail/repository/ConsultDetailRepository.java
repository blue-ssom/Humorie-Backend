package com.example.humorie.consult_detail.repository;

import com.example.humorie.consult_detail.entity.ConsultDetail;
import com.example.humorie.account.entity.AccountDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ConsultDetailRepository extends JpaRepository<ConsultDetail, Long> {
    // 가장 최근 상담 내역 조회
    @Query("SELECT c FROM ConsultDetail c WHERE c.account = :account ORDER BY c.reservation.counselDate DESC")
    Optional<ConsultDetail> findLatestConsultDetail(@Param("account") AccountDetail account);
}