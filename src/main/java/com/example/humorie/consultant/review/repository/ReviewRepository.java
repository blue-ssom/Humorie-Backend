package com.example.humorie.consultant.review.repository;

import com.example.humorie.consultant.review.entity.Review;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {
    List<Review> findByCounselorId(long counselorId);

    @Transactional
    void deleteByAccount_Id(Long accountId);
}