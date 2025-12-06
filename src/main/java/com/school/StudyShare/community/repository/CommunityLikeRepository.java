package com.school.StudyShare.community.repository;

import com.school.StudyShare.community.entity.Community;
import com.school.StudyShare.community.entity.CommunityLike;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface CommunityLikeRepository extends JpaRepository<CommunityLike, Long> {

    // 특정 게시글에 대해 특정 유저가 좋아요를 눌렀는지 확인
    Optional<CommunityLike> findByCommunityAndUserId(Community community, Integer userId);

    // 존재 여부 확인 (Boolean 반환)
    boolean existsByCommunityAndUserId(Community community, Integer userId);

    // 💡 [필수 추가] 특정 유저가 좋아요 누른 목록 조회
    // 이 메서드가 없으면 "좋아요한 글" 목록이 0개로 나옵니다.
    List<CommunityLike> findByUserId(Integer userId);
}