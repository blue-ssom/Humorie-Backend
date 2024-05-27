package com.example.humorie.consultant.counselor.repository;

import com.example.humorie.consultant.counselor.entity.Counselor;
import com.example.humorie.consultant.counselor.entity.Symptom;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface CounselorRepository extends JpaRepository<Counselor, Long> {

    @Query("SELECT c FROM Counselor c WHERE c.name LIKE %:keyword% OR :keyword IN (SELECT s FROM c.specialties s)")
    Page<Counselor> findByKeyword(@Param("keyword") String keyword, Pageable pageable);

}
