package com.school.StudyShare.community.repository;

import com.school.StudyShare.community.entity.Community;
import com.school.StudyShare.community.entity.CommunityBookmark;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CommunityBookmarkRepository extends JpaRepository<CommunityBookmark, Long> {
    Optional<CommunityBookmark> findByCommunityAndUserId(Community community, Long userId);
    boolean existsByCommunityAndUserId(Community community, Long userId);
}