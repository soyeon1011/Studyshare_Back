// StudyShare/src/main/java/com.school.StudyShare/community/controller/CommunityController.java

package com.school.StudyShare.community.controller;

import com.school.StudyShare.community.dto.CommunityCreateRequestDto;
import com.school.StudyShare.community.dto.CommunityResponseDto;
import com.school.StudyShare.community.dto.CommunityUpdateRequestDto;
import com.school.StudyShare.community.service.CommunityService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

// 💡 CORS 설정 (Port 8081로 변경되었으므로 모든 출처를 허용하는 것이 안전합니다.)
@CrossOrigin(origins = "*")
@RestController
@RequiredArgsConstructor
@RequestMapping("/communities") // 💡 [수정] 엔드포인트를 /communities로 변경합니다.
public class CommunityController {

    private final CommunityService communityService; // 💡 [수정] CommunityService 주입

    private Integer getCurrentUserId() {
        return 1; // 임시 사용자 ID
    }

    /**
     * 게시글 생성 (노트 -> 게시글)
     * [POST] /communities
     */
    @PostMapping
    public ResponseEntity<CommunityResponseDto> createPost(@RequestBody CommunityCreateRequestDto requestDto) { // 💡 [수정] DTO 및 메서드명 변경
        Integer userId = getCurrentUserId();
        CommunityResponseDto responseDto = communityService.createPost(requestDto, userId); // 💡 [수정] Service 메서드명 변경
        return ResponseEntity.status(HttpStatus.CREATED).body(responseDto);
    }

    /**
     * 게시글 수정 (노트 -> 게시글)
     * [PUT] /communities/{postId}
     */
    @PutMapping("/{postId}")
    public ResponseEntity<CommunityResponseDto> updatePost(@PathVariable Long postId, // 💡 [수정] PathVariable 이름 변경
                                                           @RequestBody CommunityUpdateRequestDto requestDto) { // 💡 [수정] DTO 변경
        Integer userId = getCurrentUserId();
        CommunityResponseDto responseDto = communityService.updatePost(postId, requestDto, userId); // 💡 [수정] Service 메서드명 변경
        return ResponseEntity.ok(responseDto);
    }

    /**
     * 게시글 삭제 (노트 -> 게시글)
     * [DELETE] /communities/{postId}
     */
    @DeleteMapping("/{postId}")
    public ResponseEntity<Void> deletePost(@PathVariable Long postId) { // 💡 [수정] PathVariable 이름 변경
        Integer userId = getCurrentUserId();
        communityService.deletePost(postId, userId); // 💡 [수정] Service 메서드명 변경
        return ResponseEntity.noContent().build();
    }

    /**
     * 모든 게시글 조회 (노트 -> 게시글)
     * [GET] /communities
     */
    @GetMapping
    public ResponseEntity<List<CommunityResponseDto>> getAllPosts() { // 💡 [수정] DTO 및 메서드명 변경
        List<CommunityResponseDto> posts = communityService.getAllPosts(); // 💡 [수정] Service 메서드명 변경
        return ResponseEntity.ok(posts);
    }

    /**
     * 특정 게시글 1개 조회 (노트 -> 게시글)
     * [GET] /communities/{postId}
     */
    @GetMapping("/{postId}")
    public ResponseEntity<CommunityResponseDto> getPostById(@PathVariable Long postId) { // 💡 [수정] DTO 및 PathVariable 이름 변경
        CommunityResponseDto post = communityService.getPostById(postId); // 💡 [수정] Service 메서드명 변경
        return ResponseEntity.ok(post);
    }

    /**
     * 특정 사용자(ID)의 모든 게시글 조회 (노트 -> 게시글)
     * [GET] /communities/user/{userId}
     */
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<CommunityResponseDto>> getPostsByUserId(@PathVariable Integer userId) { // 💡 [수정] DTO 및 메서드명 변경
        List<CommunityResponseDto> posts = communityService.getPostsByUserId(userId); // 💡 [수정] Service 메서드명 변경
        return ResponseEntity.ok(posts);
    }
}