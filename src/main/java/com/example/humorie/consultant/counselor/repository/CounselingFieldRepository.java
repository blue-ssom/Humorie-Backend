package com.example.humorie.consultant.counselor.repository;

import com.example.humorie.consultant.counselor.entity.CounselingField;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CounselingFieldRepository extends JpaRepository<CounselingField, Long> {

    List<CounselingField> findByCounselorId(long counselorId);

}
