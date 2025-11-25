package com.shcool.StudyShare.community.repository;

import com.shcool.StudyShare.community.entity.Community;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommunityRepository extends JpaRepository<Community, Long> {

    // 1. 특정 카테고리 글만 모아보기 (ex: 자유게시판만 보기)
    List<Community> findByCategory(String category);

    // 2. 내가 쓴 글 보기
    List<Community> findByUserId(Integer userId);
}