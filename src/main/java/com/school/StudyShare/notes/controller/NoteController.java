package com.school.StudyShare.notes.controller;

import com.school.StudyShare.notes.dto.NoteCreateRequestDto;
import com.school.StudyShare.notes.dto.NoteResponseDto;
import com.school.StudyShare.notes.dto.NoteUpdateRequestDto;
import com.school.StudyShare.notes.service.NoteService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

// 💡 [핵심] 모든 주소 허용 (*)
// @CrossOrigin(origins = "*")
@RestController
@RequiredArgsConstructor
@RequestMapping("/notes")
public class NoteController {

    private final NoteService noteService;

    // 임시 사용자 ID (추후 JWT 등 적용 시 변경)
    private Integer getCurrentUserId() {
        return 1;
    }

    @PostMapping
    public ResponseEntity<NoteResponseDto> createNote(@RequestBody NoteCreateRequestDto requestDto) {
        Integer userId = getCurrentUserId();
        NoteResponseDto responseDto = noteService.createNote(requestDto, userId);
        return ResponseEntity.status(HttpStatus.CREATED).body(responseDto);
    }

    @PutMapping("/{noteId}")
    public ResponseEntity<NoteResponseDto> updateNote(@PathVariable Long noteId,
                                                      @RequestBody NoteUpdateRequestDto requestDto) {
        Integer userId = getCurrentUserId();
        NoteResponseDto responseDto = noteService.updateNote(noteId, requestDto, userId);
        return ResponseEntity.ok(responseDto);
    }

    @DeleteMapping("/{noteId}")
    public ResponseEntity<Void> deleteNote(@PathVariable Long noteId) {
        Integer userId = getCurrentUserId();
        noteService.deleteNote(noteId, userId);
        return ResponseEntity.noContent().build();
    }

    // 💡 [필수 추가] 좋아요 & 북마크
    @PostMapping("/{id}/like")
    public ResponseEntity<String> toggleLike(@PathVariable Long id, @RequestParam Integer userId) {
        noteService.toggleLike(id, userId);
        return ResponseEntity.ok("좋아요 변경 완료");
    }

    @PostMapping("/{id}/bookmark")
    public ResponseEntity<String> toggleBookmark(@PathVariable Long id, @RequestParam Integer userId) {
        noteService.toggleBookmark(id, userId);
        return ResponseEntity.ok("북마크 변경 완료");
    }

    // 💡 [수정] 조회 시 userId를 받아서 하트 여부 확인
    @GetMapping
    public ResponseEntity<List<NoteResponseDto>> getAllNotes(@RequestParam(required = false) Integer userId) {
        List<NoteResponseDto> notes = noteService.getAllNotes(userId);
        return ResponseEntity.ok(notes);
    }

    @GetMapping("/{noteId}")
    public ResponseEntity<NoteResponseDto> getNoteById(@PathVariable Long noteId,
                                                       @RequestParam(required = false) Integer userId) {
        NoteResponseDto note = noteService.getNoteById(noteId, userId);
        return ResponseEntity.ok(note);
    }

    @GetMapping("/user/{targetUserId}")
    public ResponseEntity<List<NoteResponseDto>> getNotesByUserId(@PathVariable Integer targetUserId,
                                                                  @RequestParam(required = false) Integer currentUserId) {
        List<NoteResponseDto> notes = noteService.getNotesByUserId(targetUserId, currentUserId);
        return ResponseEntity.ok(notes);
    }

    @GetMapping("/user/{userId}/bookmarks")
    public ResponseEntity<List<NoteResponseDto>> getBookmarkedNotes(@PathVariable Integer userId) {
        List<NoteResponseDto> notes = noteService.getBookmarkedNotes(userId);
        return ResponseEntity.ok(notes);
    }

    // 💡 [추가] 내가 좋아요한 노트 조회 API
    @GetMapping("/user/{userId}/likes")
    public ResponseEntity<List<NoteResponseDto>> getLikedNotes(@PathVariable Integer userId) {
        List<NoteResponseDto> notes = noteService.getLikedNotes(userId);
        return ResponseEntity.ok(notes);
    }

    // 💡 [추가] 검색 API
    @GetMapping("/search")
    public ResponseEntity<List<NoteResponseDto>> searchNotes(@RequestParam String keyword,
                                                             @RequestParam(required = false) Integer userId) {
        // 검색어가 없으면 빈 리스트 반환
        if (keyword == null || keyword.trim().isEmpty()) {
            return ResponseEntity.ok(List.of());
        }
        return ResponseEntity.ok(noteService.searchNotes(keyword, userId));
    }
}
