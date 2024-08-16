package com.example.humorie.consultant.review.service;

import com.example.humorie.account.entity.AccountDetail;
import com.example.humorie.account.jwt.PrincipalDetails;
import com.example.humorie.consultant.review.dto.TagReq;
import com.example.humorie.consultant.review.dto.TagRes;
import com.example.humorie.consultant.review.entity.Tag;
import com.example.humorie.consultant.review.mapper.TagMapper;
import com.example.humorie.consultant.review.repository.TagRepository;
import com.example.humorie.global.exception.ErrorCode;
import com.example.humorie.global.exception.ErrorException;

import com.example.humorie.global.service.CommonService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TagService {

    private final TagRepository tagRepository;
    private final CommonService commonService;
    private final TagMapper mapper = TagMapper.INSTANCE;

    @Transactional
    public String createTag(String accessToken, TagReq tagReq) {
        AccountDetail account = commonService.getAccountFromToken(accessToken);

        boolean tagExists = tagRepository.existsByTagNameAndAccount(tagReq.getTagName(), account);
        if (tagExists) {
            throw new ErrorException(ErrorCode.DUPLICATE_TAG_NAME);
        }

        int tagCount = tagRepository.countByAccount(account);
        if(tagCount >= 5) {
            throw new ErrorException(ErrorCode.MAX_TAG_LIMIT_EXCEEDED);
        }

        Tag tag = mapper.toReviewTag(tagReq);
        tag.setAccount(account);
        tagRepository.save(tag);

        return "Creation Success";
    }

    @Transactional(readOnly = true)
    public TagRes getTagByName(String accessToken, String tagName) {
        AccountDetail account = commonService.getAccountFromToken(accessToken);

        Tag tag = tagRepository.findByTagNameAndAccount(tagName, account)
                .orElseThrow(() -> new ErrorException(ErrorCode.NONE_EXIST_TAG));

        return mapper.toTagRes(tag);
    }

    @Transactional(readOnly = true)
    public List<TagRes> getAllTags(String accessToken) {
        AccountDetail account = commonService.getAccountFromToken(accessToken);

        List<Tag> tags = tagRepository.findByAccount(account);

        return mapper.toTagResList(tags);
    }

    @Transactional
    public String deleteTag(String accessToken, long tagId) {
        AccountDetail account = commonService.getAccountFromToken(accessToken);

        tagRepository.findByIdAndAccount(tagId, account)
                .ifPresentOrElse(
                        tagRepository::delete,
                        () -> { throw new ErrorException(ErrorCode.NONE_EXIST_TAG); }
                );

        return "Deletion Success";
    }

}
