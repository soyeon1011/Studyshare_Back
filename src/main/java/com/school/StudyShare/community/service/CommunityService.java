package com.school.StudyShare.community.service;

import com.school.StudyShare.community.dto.CommunityUpdateRequestDto;
import com.school.StudyShare.community.dto.CommunityResponseDto;
import com.school.StudyShare.community.entity.Community;
import com.school.StudyShare.community.entity.CommunityLike;
import com.school.StudyShare.community.entity.CommunityBookmark;
import com.school.StudyShare.community.repository.CommunityRepository;
import com.school.StudyShare.community.repository.CommunityLikeRepository;
import com.school.StudyShare.community.repository.CommunityBookmarkRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CommunityService {

    private final CommunityRepository communityRepository;
    private final CommunityLikeRepository communityLikeRepository;
    private final CommunityBookmarkRepository communityBookmarkRepository;

    // 글 작성
    @Transactional
    public CommunityResponseDto createPost(CommunityUpdateRequestDto dto, Integer userId) {
        Community community = new Community();
        community.setUserId(userId);
        community.setTitle(dto.getTitle());
        community.setCategory(dto.getCategory());
        community.setContent(dto.getContent());
        community.setLikesCount(0);
        community.setBookmarksCount(0); // 💡 초기화
        community.setCommentCount(0);
        community.setCommentLikeCount(0);

        Community savedPost = communityRepository.save(community);
        return new CommunityResponseDto(savedPost, false, false);
    }

    // 💡 좋아요 토글
    @Transactional
    public void toggleLike(Long communityId, Integer userId) {
        Community community = communityRepository.findById(communityId)
                .orElseThrow(() -> new IllegalArgumentException("게시글 없음"));

        Optional<CommunityLike> likeOptional = communityLikeRepository.findByCommunityAndUserId(community, userId);

        if (likeOptional.isPresent()) {
            communityLikeRepository.delete(likeOptional.get());
            if (community.getLikesCount() > 0) community.setLikesCount(community.getLikesCount() - 1);
        } else {
            communityLikeRepository.save(new CommunityLike(community, userId)); // ⚠️ Entity의 userId 타입도 Integer여야 함
            community.setLikesCount(community.getLikesCount() + 1);
        }
    }

    // 💡 북마크 토글
    @Transactional
    public void toggleBookmark(Long communityId, Integer userId) {
        Community community = communityRepository.findById(communityId)
                .orElseThrow(() -> new IllegalArgumentException("게시글 없음"));

        Optional<CommunityBookmark> bookmarkOptional = communityBookmarkRepository.findByCommunityAndUserId(community, userId);

        if (bookmarkOptional.isPresent()) {
            communityBookmarkRepository.delete(bookmarkOptional.get());
            if (community.getBookmarksCount() > 0) community.setBookmarksCount(community.getBookmarksCount() - 1);
        } else {
            communityBookmarkRepository.save(new CommunityBookmark(community, userId));
            if (community.getBookmarksCount() == null) community.setBookmarksCount(0);
            community.setBookmarksCount(community.getBookmarksCount() + 1);
        }
    }

    // 💡 전체 조회 (로그인한 유저 기준 좋아요 여부 포함)
    @Transactional(readOnly = true)
    public List<CommunityResponseDto> getAllPosts(Integer userId) {
        return communityRepository.findAll().stream()
                .map(post -> {
                    boolean isLiked = (userId != null) && communityLikeRepository.existsByCommunityAndUserId(post, userId);
                    boolean isBookmarked = (userId != null) && communityBookmarkRepository.existsByCommunityAndUserId(post, userId);
                    return new CommunityResponseDto(post, isLiked, isBookmarked);
                })
                .collect(Collectors.toList());
    }

    // 오버로딩 (비로그인용)
    public List<CommunityResponseDto> getAllPosts() {
        return getAllPosts(null);
    }

    // 💡 카테고리별 조회
    @Transactional(readOnly = true)
    public List<CommunityResponseDto> getPostsByCategory(String category, Integer userId) {
        return communityRepository.findByCategory(category).stream()
                .map(post -> {
                    boolean isLiked = (userId != null) && communityLikeRepository.existsByCommunityAndUserId(post, userId);
                    boolean isBookmarked = (userId != null) && communityBookmarkRepository.existsByCommunityAndUserId(post, userId);
                    return new CommunityResponseDto(post, isLiked, isBookmarked);
                })
                .collect(Collectors.toList());
    }

    // 💡 상세 조회
    @Transactional(readOnly = true)
    public CommunityResponseDto getPostById(Long id, Integer userId) {
        Community community = communityRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("게시글 없음"));
        boolean isLiked = (userId != null) && communityLikeRepository.existsByCommunityAndUserId(community, userId);
        boolean isBookmarked = (userId != null) && communityBookmarkRepository.existsByCommunityAndUserId(community, userId);
        return new CommunityResponseDto(community, isLiked, isBookmarked);
    }

    // 💡 내가 좋아요한 글 조회
    @Transactional(readOnly = true)
    public List<CommunityResponseDto> getLikedPosts(Integer userId) {
        return communityLikeRepository.findByUserId(userId).stream()
                .map(like -> {
                    Community post = like.getCommunity();
                    boolean isBookmarked = communityBookmarkRepository.existsByCommunityAndUserId(post, userId);
                    return new CommunityResponseDto(post, true, isBookmarked);
                })
                .collect(Collectors.toList());
    }

    // 💡 내가 북마크한 글 조회
    @Transactional(readOnly = true)
    public List<CommunityResponseDto> getBookmarkedPosts(Integer userId) {
        return communityBookmarkRepository.findByUserId(userId).stream()
                .map(bookmark -> {
                    Community post = bookmark.getCommunity();
                    boolean isLiked = communityLikeRepository.existsByCommunityAndUserId(post, userId);
                    return new CommunityResponseDto(post, isLiked, true);
                })
                .collect(Collectors.toList());
    }

    @Transactional
    public void deletePost(Long id, Integer userId) {
        Community community = communityRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("게시글 없음"));
        if (!community.getUserId().equals(userId)) throw new SecurityException("권한 없음");
        communityRepository.delete(community);
    }
}