package com.school.StudyShare.community.dto;

import com.school.StudyShare.community.entity.Community;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class CommunityResponseDto {

    private Long id;
    private Integer userId;
    private String title;
    private String category;
    private String content;
    private Integer likesCount;
    private Integer commentCount;
    private Integer commentLikeCount;
    private String createDate;

    // 💡 프론트엔드 UI 상태값
    private boolean isLiked;
    private boolean isBookmarked;

    // 1. 단순 변환용
    public CommunityResponseDto(Community community) {
        this(community, false, false);
    }

    // 2. 상태값 포함 생성자
    public CommunityResponseDto(Community community, boolean isLiked, boolean isBookmarked) {
        this.id = community.getId();

        // ⚠️ [확인 필요] Community Entity의 실제 Getter 이름과 일치시켜야 합니다.
        // 예: community.getUserId() 인지 community.getCommunityUserId() 인지 확인!
        this.userId = community.getUserId();
        this.title = community.getTitle();
        this.category = community.getCategory();
        this.content = community.getContent();
        this.likesCount = community.getLikesCount();
        this.commentCount = community.getCommentCount();
        this.commentLikeCount = community.getCommentLikeCount();

        if (community.getCreateDate() != null) {
            this.createDate = community.getCreateDate().toString();
        } else {
            this.createDate = "";
        }

        this.isLiked = isLiked;
        this.isBookmarked = isBookmarked;
    }
}