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

// 💡 CORS 설정 추가: Flutter 앱(http://localhost:8080)의 접근을 허용합니다.
@CrossOrigin(origins = "http://localhost:8080")
@RestController
@RequiredArgsConstructor
@RequestMapping("/notes") // API의 기본 URL 경로
public class NoteController {

    private final NoteService noteService;

    // 아래 모든 메서드의 'currentUserId'는 Spring Security 같은 인증 기능이
    // 구현된 후, @AuthenticationPrincipal 등을 통해 실제 로그인한 사용자의 ID를
    // 동적으로 받아와야 합니다. 지금은 '1'로 임시 고정합니다.
    private Integer getCurrentUserId() {
        return 1; // 임시 사용자 ID
    }

    /**
     * 노트 생성
     * [POST] /notes
     */
    @PostMapping
    public ResponseEntity<NoteResponseDto> createNote(@RequestBody NoteCreateRequestDto requestDto) {
        Integer userId = getCurrentUserId(); // (임시)
        NoteResponseDto responseDto = noteService.createNote(requestDto, userId);
        return ResponseEntity.status(HttpStatus.CREATED).body(responseDto);
    }

    /**
     * 노트 수정
     * [PUT] /notes/{noteId}
     */
    @PutMapping("/{noteId}")
    public ResponseEntity<NoteResponseDto> updateNote(@PathVariable Long noteId,
                                                      @RequestBody NoteUpdateRequestDto requestDto) {
        Integer userId = getCurrentUserId(); // (임시)
        NoteResponseDto responseDto = noteService.updateNote(noteId, requestDto, userId);
        return ResponseEntity.ok(responseDto);
    }

    /**
     * 노트 삭제
     * [DELETE] /notes/{noteId}
     */
    @DeleteMapping("/{noteId}")
    public ResponseEntity<Void> deleteNote(@PathVariable Long noteId) {
        Integer userId = getCurrentUserId(); // (임시)
        noteService.deleteNote(noteId, userId);
        return ResponseEntity.noContent().build(); // 삭제 성공 시 204 No Content
    }

    /**
     * 모든 노트 조회
     * [GET] /notes
     */
    @GetMapping
    public ResponseEntity<List<NoteResponseDto>> getAllNotes() {
        List<NoteResponseDto> notes = noteService.getAllNotes();
        return ResponseEntity.ok(notes);
    }

    /**
     * 특정 노트 1개 조회
     * [GET] /notes/{noteId}
     */
    @GetMapping("/{noteId}")
    public ResponseEntity<NoteResponseDto> getNoteById(@PathVariable Long noteId) {
        NoteResponseDto note = noteService.getNoteById(noteId);
        return ResponseEntity.ok(note);
    }

    /**
     * 특정 사용자(ID)의 모든 노트 조회
     * [GET] /notes/user/{userId}
     */
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<NoteResponseDto>> getNotesByUserId(@PathVariable Integer userId) {
        List<NoteResponseDto> notes = noteService.getNotesByUserId(userId);
        return ResponseEntity.ok(notes);
    }
}