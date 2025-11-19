package com.shcool.StudyShare.community.dto;

import com.shcool.StudyShare.community.entity.Community;
import lombok.Getter;
import java.time.LocalDateTime;

@Getter
public class CommunityResponseDto {
    private final Long id;
    private final Integer userId;
    private final String title;
    private final String category;
    private final String content;
    private final int likesCount;
    private final int commentCount;
    private final LocalDateTime createDate;

    public CommunityResponseDto(Community community) {
        this.id = community.getId();
        this.userId = community.getUserId();
        this.title = community.getTitle();
        this.category = community.getCategory();
        this.content = community.getContent();
        this.likesCount = community.getLikesCount();
        this.commentCount = community.getCommentCount();
        this.createDate = community.getCreateDate();
    }
}