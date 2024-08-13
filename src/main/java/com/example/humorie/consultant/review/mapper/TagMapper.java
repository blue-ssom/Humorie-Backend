package com.example.humorie.consultant.review.mapper;

import com.example.humorie.consultant.review.dto.TagReq;
import com.example.humorie.consultant.review.dto.TagRes;
import com.example.humorie.consultant.review.entity.ReviewTag;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface TagMapper {

    TagMapper INSTANCE = Mappers.getMapper(TagMapper.class);

    @Mapping(source = "id", target = "tagId")
    TagRes toTagRes(ReviewTag tag);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "reviews", ignore = true)
    ReviewTag toReviewTag(TagReq tagReq);

    List<TagRes> toTagResList(List<ReviewTag> tags);

}
