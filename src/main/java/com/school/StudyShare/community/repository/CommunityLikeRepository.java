package com.school.StudyShare.community.repository;

import com.school.StudyShare.community.entity.Community;
import com.school.StudyShare.community.entity.CommunityLike;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CommunityLikeRepository extends JpaRepository<CommunityLike, Long> {
    Optional<CommunityLike> findByCommunityAndUserId(Community community, Long userId);
    boolean existsByCommunityAndUserId(Community community, Long userId);
}