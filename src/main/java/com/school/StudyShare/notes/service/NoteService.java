package com.school.StudyShare.notes.service;

import com.school.StudyShare.notes.dto.NoteCreateRequestDto;
import com.school.StudyShare.notes.dto.NoteResponseDto;
import com.school.StudyShare.notes.dto.NoteUpdateRequestDto;
import com.school.StudyShare.notes.entity.Note;
import com.school.StudyShare.notes.repository.NoteRepository;
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

        // 💡 [수정 반영] setUserId -> setNoteUserId
        note.setNoteUserId(userId);

        // 💡 [수정 반영] setTitle -> setNoteTitle
        note.setNoteTitle(dto.getTitle());

        note.setNoteSubjectId(dto.getNoteSubjectId());
        note.setNoteContent(dto.getNoteContent());
        note.setNoteFileUrl(dto.getNoteFileUrl());

        // 💡 [수정 반영] setLikesCount -> setNoteLikesCount 등
        note.setNoteLikesCount(0);
        note.setNoteCommentsCount(0);
        note.setNoteCommentsLikesCount(0); // 추가

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
        // 💡 [수정 반영] getUserId -> getNoteUserId
        if (!note.getNoteUserId().equals(userId)) {
            throw new SecurityException("노트를 수정할 권한이 없습니다.");
        }

        // 3. DTO의 정보로 엔티티 필드 업데이트
        // 💡 [수정 반영] setTitle -> setNoteTitle
        note.setNoteTitle(dto.getTitle());

        note.setNoteSubjectId(dto.getNoteSubjectId());
        note.setNoteContent(dto.getNoteContent());
        note.setNoteFileUrl(dto.getNoteFileUrl());

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
        // 💡 [수정 반영] getUserId -> getNoteUserId
        if (!note.getNoteUserId().equals(userId)) {
            throw new SecurityException("노트를 삭제할 권한이 없습니다.");
        }

        // 3. 삭제
        noteRepository.delete(note);
    }

    // =======================================================
    // 💡 [최신순 정렬 적용] getAllNotes 메서드 수정
    // =======================================================

    /**
     * 모든 노트 조회 (최신순)
     * [GET] /api/v1/notes
     */
    @Transactional(readOnly = true)
    public List<NoteResponseDto> getAllNotes() {
        // 🚨 [핵심 수정] Repository의 최신순 정렬 메서드를 호출합니다.
        // noteRepository.findAll() 대신 최신순 메서드를 사용합니다.
        // Entitry 필드명 'noteCreateDate'에 맞춘 Repository 메서드를 호출합니다.
        return noteRepository.findAllByOrderByNoteCreateDateDesc().stream()
                .map(NoteResponseDto::new) // Note 객체를 NoteResponseDto로 변환
                .collect(Collectors.toList());
    }

    /**
     * 특정 노트 1개 조회 (ID 기준)
     * [GET] /api/v1/notes/{noteId}
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
        // 💡 [수정 반영] findByUserId -> findByNoteUserId
        return noteRepository.findByNoteUserId(userId).stream()
                .map(NoteResponseDto::new)
                .collect(Collectors.toList());
    }
}