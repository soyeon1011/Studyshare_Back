// package com.school.StudyShare.community.entity;

package com.school.StudyShare.community.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.hibernate.annotations.CreationTimestamp;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "community")
@Getter
@Setter
@NoArgsConstructor
public class Community {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 💡 [수정] 작성자 ID (DB: user_id)
    @Column(name = "user_id", nullable = false)
    private Integer userId;

    // 💡 [수정] 제목
    @Column(name = "community_title", length = 200, nullable = false)
    private String title;

    // 💡 [수정] 카테고리 (DB: community_category)
    @Column(name = "community_category", length = 50, nullable = false)
    private String category;

    // 💡 [수정] 내용
    @Column(name = "community_content", columnDefinition = "TEXT", nullable = false)
    private String content;

    // 💡 [수정] 좋아요 수
    @Column(name = "community_likes_count")
    private Integer likesCount = 0;

    // 💡 [수정] 댓글 수
    @Column(name = "community_comment_count")
    private Integer commentCount = 0;

    // 💡 [수정] 댓글 좋아요 수
    @Column(name = "community_comment_like_count")
    private Integer commentLikeCount = 0;

    // 💡 [수정] 작성일
    @CreationTimestamp
    @Column(name = "community_create_date", nullable = false)
    private LocalDateTime createDate;
}