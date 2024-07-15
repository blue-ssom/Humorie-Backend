package com.example.humorie.consultant.counselor.repository;

import com.example.humorie.consultant.counselor.entity.Affiliation;
import com.example.humorie.consultant.counselor.entity.Education;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EducationRepository extends JpaRepository<Education, Long> {

    List<Education> findByCounselorId(long counselorId);

}