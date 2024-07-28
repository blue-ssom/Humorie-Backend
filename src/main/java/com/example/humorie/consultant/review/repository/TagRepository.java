package com.example.humorie.consultant.review.repository;

import com.example.humorie.consultant.review.entity.ReviewTag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TagRepository extends JpaRepository<ReviewTag, Long> {
}
