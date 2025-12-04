// package com.school.StudyShare.community.dto;

package com.school.StudyShare.community.dto;

import com.school.StudyShare.community.entity.Community;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class CommunityResponseDto {

    @JsonProperty("id")
    private Long id;

    @JsonProperty("user_id") // 💡 [수정] DTO 필드명은 userId
    private Integer userId;

    @JsonProperty("title")
    private String title;

    @JsonProperty("category") // 💡 [수정] category 필드
    private String category;

    @JsonProperty("content")
    private String content;

    @JsonProperty("likes_count")
    private Integer likesCount;

    @JsonProperty("comments_count")
    private Integer commentsCount;

    @JsonProperty("comment_like_count") // 💡 [수정] DTO 필드명은 commentsLikeCount
    private Integer commentLikeCount;

    @JsonProperty("create_date")
    private LocalDateTime createDate;

    // Entity를 DTO로 변환하는 생성자
    public CommunityResponseDto(Community post) {
        this.id = post.getId();
        this.userId = post.getUserId(); // Entity 필드명에 맞게 호출해야 함
        this.title = post.getTitle();
        this.category = post.getCategory();
        this.content = post.getContent();
        this.likesCount = post.getLikesCount();
        this.commentsCount = post.getCommentCount();
        this.commentLikeCount = post.getCommentLikeCount();
        this.createDate = post.getCreateDate();
    }
}