package com.school.StudyShare.community.controller;

import com.school.StudyShare.community.dto.CommunityUpdateRequestDto;
import com.school.StudyShare.community.dto.CommunityResponseDto;
import com.school.StudyShare.community.service.CommunityService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/communities")
public class CommunityController {

    private final CommunityService communityService;

    // 💡 좋아요 & 북마크 (노트와 동일 방식)
    @PostMapping("/{id}/like")
    public ResponseEntity<String> toggleLike(@PathVariable Long id, @RequestParam Integer userId) {
        communityService.toggleLike(id, userId);
        return ResponseEntity.ok("좋아요 변경 완료");
    }

    @PostMapping("/{id}/bookmark")
    public ResponseEntity<String> toggleBookmark(@PathVariable Long id, @RequestParam Integer userId) {
        communityService.toggleBookmark(id, userId);
        return ResponseEntity.ok("북마크 변경 완료");
    }

    // 💡 내가 좋아요/북마크한 글 목록
    @GetMapping("/user/{userId}/likes")
    public ResponseEntity<List<CommunityResponseDto>> getLikedPosts(@PathVariable Integer userId) {
        return ResponseEntity.ok(communityService.getLikedPosts(userId));
    }

    @GetMapping("/user/{userId}/bookmarks")
    public ResponseEntity<List<CommunityResponseDto>> getBookmarkedPosts(@PathVariable Integer userId) {
        return ResponseEntity.ok(communityService.getBookmarkedPosts(userId));
    }

    // 기존 기능들 (userId 파라미터 추가하여 상태 확인)
    @PostMapping
    public ResponseEntity<CommunityResponseDto> createPost(@RequestBody CommunityUpdateRequestDto requestDto) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(communityService.createPost(requestDto, 1)); // 임시 userId 1
    }

    @GetMapping
    public ResponseEntity<List<CommunityResponseDto>> getAllPosts(@RequestParam(required = false) Integer userId) {
        return ResponseEntity.ok(communityService.getAllPosts(userId));
    }

    @GetMapping("/category/{categoryName}")
    public ResponseEntity<List<CommunityResponseDto>> getPostsByCategory(@PathVariable String categoryName,
                                                                         @RequestParam(required = false) Integer userId) {
        return ResponseEntity.ok(communityService.getPostsByCategory(categoryName, userId));
    }

    @GetMapping("/{id}")
    public ResponseEntity<CommunityResponseDto> getPostById(@PathVariable Long id,
                                                            @RequestParam(required = false) Integer userId) {
        return ResponseEntity.ok(communityService.getPostById(id, userId));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePost(@PathVariable Long id) {
        communityService.deletePost(id, 1); // 임시 userId 1
        return ResponseEntity.noContent().build();
    }
}