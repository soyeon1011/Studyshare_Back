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

    // 임시 유저 ID (로그인 구현 전까지)
    private Integer getCurrentUserId() {
        return 1;
    }

    // 1. 글 작성 [POST]
    @PostMapping
    public ResponseEntity<CommunityResponseDto> createPost(@RequestBody CommunityUpdateRequestDto requestDto) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(communityService.createPost(requestDto, getCurrentUserId()));
    }

    // 2. 전체 조회 [GET]
    @GetMapping
    public ResponseEntity<List<CommunityResponseDto>> getAllPosts() {
        return ResponseEntity.ok(communityService.getAllPosts());
    }

    // 3. 카테고리별 조회 [GET] /api/v1/communities/category/자유
    @GetMapping("/category/{categoryName}")
    public ResponseEntity<List<CommunityResponseDto>> getPostsByCategory(@PathVariable String categoryName) {
        return ResponseEntity.ok(communityService.getPostsByCategory(categoryName));
    }

    // 4. 상세 조회 [GET]
    @GetMapping("/{id}")
    public ResponseEntity<CommunityResponseDto> getPostById(@PathVariable Long id) {
        return ResponseEntity.ok(communityService.getPostById(id));
    }

    // 5. 삭제 [DELETE]
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePost(@PathVariable Long id) {
        communityService.deletePost(id, getCurrentUserId());
        return ResponseEntity.noContent().build();
    }
}