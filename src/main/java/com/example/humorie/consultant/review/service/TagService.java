package com.example.humorie.consultant.review.service;

import com.example.humorie.account.entity.AccountDetail;
import com.example.humorie.account.jwt.JwtTokenUtil;
import com.example.humorie.account.repository.AccountRepository;
import com.example.humorie.consultant.review.dto.TagReq;
import com.example.humorie.consultant.review.entity.ReviewTag;
import com.example.humorie.consultant.review.repository.TagRepository;
import com.example.humorie.global.exception.ErrorCode;
import com.example.humorie.global.exception.ErrorException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TagService {

    private final JwtTokenUtil jwtTokenUtil;
    private final TagRepository tagRepository;
    private final AccountRepository accountRepository;

    @Transactional
    public String createTag(String accessToken, TagReq tagReq) {
        String email = jwtTokenUtil.getEmailFromToken(accessToken);

        AccountDetail account = accountRepository.findByEmail(email)
                .orElseThrow(() -> new ErrorException(ErrorCode.NONE_EXIST_USER));

        ReviewTag tag = ReviewTag.builder()
                .tagName(tagReq.getTagName())
                .tagContent(tagReq.getTagContent())
                .account(account)
                .build();

        tagRepository.save(tag);

        return "Creation Success";
    }

}
