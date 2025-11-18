package com.shcool.StudyShare.notes.service;

import com.shcool.StudyShare.notes.dto.NoteCreateRequestDto;
import com.shcool.StudyShare.notes.dto.NoteResponseDto;
import com.shcool.StudyShare.notes.dto.NoteUpdateRequestDto;
import com.shcool.StudyShare.notes.entity.Note;
import com.shcool.StudyShare.notes.repository.NoteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor // final 필드에 대한 생성자 자동 생성 (의존성 주입)
public class NoteService {

    private final NoteRepository noteRepository;

    /**
     * 노트 생성
     */
    @Transactional
    public NoteResponseDto createNote(NoteCreateRequestDto dto, Integer userId) {
        Note note = new Note();
        note.setUserId(userId); // 1. (중요) 인증된 사용자의 ID를 받아서 설정
        note.setTitle(dto.getTitle());
        note.setNoteSubjectId(dto.getNoteSubjectId());
        note.setNoteContent(dto.getNoteContent());
        note.setNoteFileUrl(dto.getNoteFileUrl());
        note.setLikesCount(0);     // 2. 생성 시 좋아요/댓글 수는 0으로 초기화
        note.setCommentsCount(0);

        Note savedNote = noteRepository.save(note);

        return new NoteResponseDto(savedNote);
    }

    /**
     * 노트 수정
     */
    @Transactional
    public NoteResponseDto updateNote(Long noteId, NoteUpdateRequestDto dto, Integer userId) {
        // 1. 노트를 ID로 조회
        Note note = noteRepository.findById(noteId)
                .orElseThrow(() -> new IllegalArgumentException("해당 노트를 찾을 수 없습니다. id=" + noteId));

        // 2. (보안) 노트 작성자 ID와 현재 로그인한 사용자 ID가 같은지 확인
        if (!note.getUserId().equals(userId)) {
            throw new SecurityException("노트를 수정할 권한이 없습니다.");
        }

        // 3. DTO의 정보로 엔티티 필드 업데이트
        note.setTitle(dto.getTitle());
        note.setNoteSubjectId(dto.getNoteSubjectId());
        note.setNoteContent(dto.getNoteContent());
        note.setNoteFileUrl(dto.getNoteFileUrl());

        // 4. noteRepository.save(note)는 noteId가 존재하므로 UPDATE 쿼리를 실행
        Note updatedNote = noteRepository.save(note);

        return new NoteResponseDto(updatedNote);
    }

    /**
     * 노트 삭제
     */
    @Transactional
    public void deleteNote(Long noteId, Integer userId) {
        // 1. 노트 조회
        Note note = noteRepository.findById(noteId)
                .orElseThrow(() -> new IllegalArgumentException("해당 노트를 찾을 수 없습니다. id=" + noteId));

        // 2. (보안) 작성자와 로그인 유저가 같은지 확인
        if (!note.getUserId().equals(userId)) {
            throw new SecurityException("노트를 삭제할 권한이 없습니다.");
        }

        // 3. 삭제
        noteRepository.delete(note);
    }

    /**
     * 모든 노트 조회
     */
    @Transactional(readOnly = true)
    public List<NoteResponseDto> getAllNotes() {
        return noteRepository.findAll().stream()
                .map(NoteResponseDto::new) // Note 객체를 NoteResponseDto로 변환
                .collect(Collectors.toList());
    }

    /**
     * 특정 노트 1개 조회 (ID 기준)
     */
    @Transactional(readOnly = true)
    public NoteResponseDto getNoteById(Long noteId) {
        Note note = noteRepository.findById(noteId)
                .orElseThrow(() -> new IllegalArgumentException("해당 노트를 찾을 수 없습니다. id=" + noteId));

        return new NoteResponseDto(note);
    }

    /**
     * 특정 사용자가 작성한 모든 노트 조회
     */
    @Transactional(readOnly = true)
    public List<NoteResponseDto> getNotesByUserId(Integer userId) {
        return noteRepository.findByUserId(userId).stream()
                .map(NoteResponseDto::new)
                .collect(Collectors.toList());
    }
}