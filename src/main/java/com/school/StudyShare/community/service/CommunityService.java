package com.school.StudyShare.community.service;

import com.school.StudyShare.community.dto.CommunityUpdateRequestDto;
import com.school.StudyShare.community.dto.CommunityResponseDto;
import com.school.StudyShare.community.entity.Community;
import com.school.StudyShare.community.repository.CommunityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CommunityService {

    private final CommunityRepository communityRepository;

    // 글 작성
    @Transactional
    public CommunityResponseDto createPost(CommunityUpdateRequestDto dto, Integer userId) {
        Community community = new Community();
        community.setUserId(userId);
        community.setTitle(dto.getTitle());
        community.setCategory(dto.getCategory());
        community.setContent(dto.getContent());

        // 초기화
        community.setLikesCount(0);
        community.setCommentCount(0);
        community.setCommentLikeCount(0);

        Community savedPost = communityRepository.save(community);
        return new CommunityResponseDto(savedPost);
    }

    // 전체 조회
    @Transactional(readOnly = true)
    public List<CommunityResponseDto> getAllPosts() {
        return communityRepository.findAll().stream()
                .map(CommunityResponseDto::new)
                .collect(Collectors.toList());
    }

    // 카테고리별 조회 (ex: "자유"만 보기)
    @Transactional(readOnly = true)
    public List<CommunityResponseDto> getPostsByCategory(String category) {
        return communityRepository.findByCategory(category).stream()
                .map(CommunityResponseDto::new)
                .collect(Collectors.toList());
    }

    // 상세 조회
    @Transactional(readOnly = true)
    public CommunityResponseDto getPostById(Long id) {
        Community community = communityRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("게시글이 없습니다. id=" + id));
        return new CommunityResponseDto(community);
    }

    // 삭제
    @Transactional
    public void deletePost(Long id, Integer userId) {
        Community community = communityRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("게시글이 없습니다."));

        if (!community.getUserId().equals(userId)) {
            throw new SecurityException("삭제 권한이 없습니다.");
        }
        communityRepository.delete(community);
    }
}