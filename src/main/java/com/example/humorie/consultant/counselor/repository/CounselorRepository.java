package com.example.humorie.consultant.counselor.repository;

import com.example.humorie.consultant.counselor.entity.Counselor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CounselorRepository extends JpaRepository<Counselor, Long> {


}
