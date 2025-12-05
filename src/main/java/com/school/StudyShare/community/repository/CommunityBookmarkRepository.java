package com.school.StudyShare.community.repository;

import com.school.StudyShare.community.entity.Community;
import com.school.StudyShare.community.entity.CommunityBookmark;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface CommunityBookmarkRepository extends JpaRepository<CommunityBookmark, Long> {
    // 💡 [수정] 매개변수 타입을 Long -> Integer로 변경
    Optional<CommunityBookmark> findByCommunityAndUserId(Community community, Integer userId);
    boolean existsByCommunityAndUserId(Community community, Integer userId);

    // 💡 목록 조회용
    List<CommunityBookmark> findByUserId(Integer userId);
}