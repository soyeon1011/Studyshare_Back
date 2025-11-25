package com.shcool.StudyShare.community.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "community") // ERD 테이블명
@Getter
@Setter
@NoArgsConstructor
public class Community {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 작성자 ID (user_id)
    @Column(name = "user_id", nullable = false)
    private Integer userId;

    // 게시글 제목 (community_title)
    @Column(name = "community_title", length = 30, nullable = false)
    private String title;

    // 카테고리 (community_category) -> ex: "자유", "질문", "홍보"
    @Column(name = "community_category", length = 50, nullable = false)
    private String category;

    // 본문 (community_content)
    @Column(name = "community_content", columnDefinition = "TEXT", nullable = false)
    private String content;

    // 좋아요 수
    @Column(name = "community_likes_count")
    private int likesCount;

    // 댓글 수
    @Column(name = "community_comment_count")
    private int commentCount;

    // 댓글 좋아요 수 (ERD에 있어서 추가함)
    @Column(name = "community_comment_like_count")
    private int commentLikeCount;

    // 작성일
    @Column(name = "community_create_date")
    private LocalDateTime createDate;

    // 저장 전 날짜 자동 생성
    @PrePersist
    public void prePersist() {
        this.createDate = LocalDateTime.now();
    }
}