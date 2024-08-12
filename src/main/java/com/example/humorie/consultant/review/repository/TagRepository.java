package com.example.humorie.consultant.review.repository;

import com.example.humorie.account.entity.AccountDetail;
import com.example.humorie.consultant.review.entity.ReviewTag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TagRepository extends JpaRepository<ReviewTag, Long> {

    Optional<ReviewTag> findByTagNameAndAccount(String tagName, AccountDetail accountDetail);

    List<ReviewTag> findByAccount(AccountDetail account);

    Optional<ReviewTag> findByIdAndAccount(long tagId, AccountDetail account);

    boolean existsByTagNameAndAccount(String tagName, AccountDetail account);

}
