package com.school.StudyShare.community.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class CommunityLike {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "community_id")
    private Community community; // 게시글 엔티티

    private Long userId;

    public CommunityLike(Community community, Long userId) {
        this.community = community;
        this.userId = userId;
    }
}