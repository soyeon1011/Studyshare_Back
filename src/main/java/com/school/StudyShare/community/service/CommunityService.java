// StudyShare/src/main/java/com/school/StudyShare/community/service/CommunityService.java

package com.school.StudyShare.community.service;

import com.school.StudyShare.community.dto.CommunityCreateRequestDto;
import com.school.StudyShare.community.dto.CommunityResponseDto;
import com.school.StudyShare.community.dto.CommunityUpdateRequestDto;
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

    /**
     * 게시글 생성
     */
    @Transactional
    public CommunityResponseDto createPost(CommunityCreateRequestDto dto, Integer userId) {
        Community post = new Community();

        post.setUserId(userId);
        post.setTitle(dto.getTitle());
        post.setCategory(dto.getCategory());
        post.setContent(dto.getContent());

        post.setLikesCount(0);
        post.setCommentCount(0);
        post.setCommentLikeCount(0);

        Community savedPost = communityRepository.save(post);

        return new CommunityResponseDto(savedPost);
    }

    /**
     * 게시글 수정
     */
    @Transactional
    public CommunityResponseDto updatePost(Long postId, CommunityUpdateRequestDto dto, Integer userId) {
        // 1. 게시글을 ID로 조회
        Community post = communityRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("해당 게시글을 찾을 수 없습니다. id=" + postId));

        // 2. (보안) 작성자 ID와 현재 로그인한 사용자 ID가 같은지 확인
        if (!post.getUserId().equals(userId)) {
            throw new SecurityException("게시글을 수정할 권한이 없습니다.");
        }

        // 3. DTO의 정보로 엔티티 필드 업데이트
        post.setTitle(dto.getTitle());
        post.setCategory(dto.getCategory());
        post.setContent(dto.getContent());

        Community updatedPost = communityRepository.save(post);

        return new CommunityResponseDto(updatedPost);
    }

    /**
     * 게시글 삭제
     */
    @Transactional
    public void deletePost(Long postId, Integer userId) {
        // 1. 게시글 조회
        Community post = communityRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("해당 게시글을 찾을 수 없습니다. id=" + postId));

        // 2. (보안) 작성자와 로그인 유저가 같은지 확인
        if (!post.getUserId().equals(userId)) {
            throw new SecurityException("게시글을 삭제할 권한이 없습니다.");
        }

        // 3. 삭제
        communityRepository.delete(post);
    }

    /**
     * 모든 게시글 조회 (최신순)
     */
    @Transactional(readOnly = true)
    public List<CommunityResponseDto> getAllPosts() {
        return communityRepository.findAllByOrderByCreateDateDesc().stream()
                .map(CommunityResponseDto::new)
                .collect(Collectors.toList());
    }

    /**
     * 특정 게시글 1개 조회 (ID 기준)
     */
    @Transactional(readOnly = true)
    public CommunityResponseDto getPostById(Long postId) {
        Community post = communityRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("해당 게시글을 찾을 수 없습니다. id=" + postId));

        return new CommunityResponseDto(post);
    }

    /**
     * 특정 사용자가 작성한 모든 게시글 조회
     */
    @Transactional(readOnly = true)
    public List<CommunityResponseDto> getPostsByUserId(Integer userId) {
        return communityRepository.findByUserId(userId).stream()
                .map(CommunityResponseDto::new)
                .collect(Collectors.toList());
    }
}