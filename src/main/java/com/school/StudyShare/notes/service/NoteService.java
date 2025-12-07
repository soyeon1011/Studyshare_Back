package com.school.StudyShare.notes.service;

import com.school.StudyShare.notes.dto.NoteCreateRequestDto;
import com.school.StudyShare.notes.dto.NoteResponseDto;
import com.school.StudyShare.notes.dto.NoteUpdateRequestDto;
import com.school.StudyShare.notes.entity.Note;
import com.school.StudyShare.notes.entity.NoteBookmark;
import com.school.StudyShare.notes.entity.NoteLike;
import com.school.StudyShare.notes.repository.NoteBookmarkRepository;
import com.school.StudyShare.notes.repository.NoteLikeRepository;
import com.school.StudyShare.notes.repository.NoteRepository;
import lombok.RequiredArgsConstructor;
import org.jsoup.Jsoup;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class NoteService {

    private final NoteRepository noteRepository;
    private final NoteLikeRepository noteLikeRepository;
    private final NoteBookmarkRepository noteBookmarkRepository;

    @Transactional
    public NoteResponseDto createNote(NoteCreateRequestDto dto, Integer userId) {
        Note note = new Note();
        note.setNoteUserId(userId);
        note.setNoteTitle(dto.getTitle());
        note.setNoteSubjectId(dto.getNoteSubjectId());
        note.setNoteContent(dto.getNoteContent());
        note.setNoteFileUrl(dto.getNoteFileUrl());
        note.setNoteLikesCount(0);
        note.setNoteCommentsCount(0);
        note.setNoteCommentsLikesCount(0);

        String plainText = Jsoup.parse(dto.getNoteContent()).text();
        note.setNotePlainText(plainText);

        note.setNoteFileUrl(dto.getNoteFileUrl());

        Note savedNote = noteRepository.save(note);
        return new NoteResponseDto(savedNote, false, false);
    }

    @Transactional
    public NoteResponseDto updateNote(Long noteId, NoteUpdateRequestDto dto, Integer userId) {
        Note note = noteRepository.findById(noteId)
                .orElseThrow(() -> new IllegalArgumentException("노트 없음 id=" + noteId));

        if (!note.getNoteUserId().equals(userId)) {
            throw new SecurityException("권한 없음");
        }

        note.setNoteTitle(dto.getTitle());
        note.setNoteSubjectId(dto.getNoteSubjectId());
        note.setNoteContent(dto.getNoteContent());
        note.setNoteFileUrl(dto.getNoteFileUrl());

        // 💡 [추가] 수정할 때도 순수 텍스트 업데이트
        String plainText = Jsoup.parse(dto.getNoteContent()).text();
        note.setNotePlainText(plainText);

        note.setNoteFileUrl(dto.getNoteFileUrl());

        Note updatedNote = noteRepository.save(note);

        boolean isLiked = noteLikeRepository.existsByNoteAndUserId(updatedNote, userId);
        boolean isBookmarked = noteBookmarkRepository.existsByNoteAndUserId(updatedNote, userId);

        return new NoteResponseDto(updatedNote, isLiked, isBookmarked);
    }

    // 💡 [추가] 검색 서비스 메서드 (제목 또는 순수 내용에서 검색)
    @Transactional(readOnly = true)
    public List<NoteResponseDto> searchNotes(String keyword, Integer userId) {
        // Repository에 이 메서드를 만들어야 합니다 (다음 단계 참조)
        List<Note> notes = noteRepository.findByNoteTitleContainingOrNotePlainTextContainingOrderByNoteCreateDateDesc(keyword, keyword);

        return notes.stream()
                .map(note -> {
                    boolean isLiked = (userId != null) && noteLikeRepository.existsByNoteAndUserId(note, userId);
                    boolean isBookmarked = (userId != null) && noteBookmarkRepository.existsByNoteAndUserId(note, userId);
                    return new NoteResponseDto(note, isLiked, isBookmarked);
                })
                .collect(Collectors.toList());
    }

    @Transactional
    public void deleteNote(Long noteId, Integer userId) {
        Note note = noteRepository.findById(noteId)
                .orElseThrow(() -> new IllegalArgumentException("노트 없음 id=" + noteId));
        if (!note.getNoteUserId().equals(userId)) {
            throw new SecurityException("권한 없음");
        }
        noteRepository.delete(note);
    }

    // 💡 좋아요 토글
    @Transactional
    public void toggleLike(Long noteId, Integer userId) {
        Note note = noteRepository.findById(noteId)
                .orElseThrow(() -> new IllegalArgumentException("노트 없음"));

        Optional<NoteLike> likeOptional = noteLikeRepository.findByNoteAndUserId(note, userId);

        if (likeOptional.isPresent()) {
            noteLikeRepository.delete(likeOptional.get());
            if (note.getNoteLikesCount() > 0) note.setNoteLikesCount(note.getNoteLikesCount() - 1);
        } else {
            noteLikeRepository.save(new NoteLike(note, userId));
            note.setNoteLikesCount(note.getNoteLikesCount() + 1);
        }
    }

    // 💡 북마크 토글 (이 함수를 통째로 덮어쓰세요)
    @Transactional
    public void toggleBookmark(Long noteId, Integer userId) {
        Note note = noteRepository.findById(noteId)
                .orElseThrow(() -> new IllegalArgumentException("노트 없음"));

        Optional<NoteBookmark> bookmarkOptional = noteBookmarkRepository.findByNoteAndUserId(note, userId);

        if (bookmarkOptional.isPresent()) {
            // 1. 북마크 취소
            noteBookmarkRepository.delete(bookmarkOptional.get());

            // 🚨 [여기가 빠져있었습니다] 숫자가 0보다 클 때만 -1 감소
            if (note.getNoteBookmarksCount() > 0) {
                note.setNoteBookmarksCount(note.getNoteBookmarksCount() - 1);
            }
        } else {
            // 2. 북마크 추가
            noteBookmarkRepository.save(new NoteBookmark(note, userId));

            // 🚨 [여기가 빠져있었습니다] 숫자 +1 증가
            // (만약 null이면 0으로 치고 1을 더함)
            if (note.getNoteBookmarksCount() == null) {
                note.setNoteBookmarksCount(1);
            } else {
                note.setNoteBookmarksCount(note.getNoteBookmarksCount() + 1);
            }
        }
    }

    // 💡 [수정] 내가 북마크한 노트 목록 조회
    @Transactional(readOnly = true)
    public List<NoteResponseDto> getBookmarkedNotes(Integer userId) {
        // 🚨 수정 전: noteBookmarkRepository.findByNoteUserId(userId);
        // ✅ 수정 후: findByUserId 로 변경!
        List<NoteBookmark> bookmarks = noteBookmarkRepository.findByUserId(userId);

        return bookmarks.stream()
                .map(bookmark -> {
                    Note note = bookmark.getNote();
                    boolean isLiked = noteLikeRepository.existsByNoteAndUserId(note, userId);
                    return new NoteResponseDto(note, isLiked, true);
                })
                .collect(Collectors.toList());
    }

    // 💡 [수정] 내가 좋아요한 노트 목록 조회
    @Transactional(readOnly = true)
    public List<NoteResponseDto> getLikedNotes(Integer userId) {
        // 🚨 수정 전: noteLikeRepository.findByNoteUserId(userId);
        // ✅ 수정 후: findByUserId 로 변경!
        List<NoteLike> likes = noteLikeRepository.findByUserId(userId);

        return likes.stream()
                .map(like -> {
                    Note note = like.getNote();
                    boolean isBookmarked = noteBookmarkRepository.existsByNoteAndUserId(note, userId);
                    return new NoteResponseDto(note, true, isBookmarked);
                })
                .collect(Collectors.toList());
    }

    // 💡 모든 노트 조회 (날짜 최신순)
    @Transactional(readOnly = true)
    public List<NoteResponseDto> getAllNotes(Integer userId) {
        return noteRepository.findAllByOrderByNoteCreateDateDesc().stream()
                .map(note -> {
                    boolean isLiked = (userId != null) && noteLikeRepository.existsByNoteAndUserId(note, userId);
                    boolean isBookmarked = (userId != null) && noteBookmarkRepository.existsByNoteAndUserId(note, userId);
                    return new NoteResponseDto(note, isLiked, isBookmarked);
                })
                .collect(Collectors.toList());
    }

    // 오버로딩 (비로그인)
    @Transactional(readOnly = true)
    public List<NoteResponseDto> getAllNotes() {
        return getAllNotes(null);
    }

    @Transactional(readOnly = true)
    public NoteResponseDto getNoteById(Long noteId, Integer userId) {
        Note note = noteRepository.findById(noteId)
                .orElseThrow(() -> new IllegalArgumentException("노트 없음"));
        boolean isLiked = (userId != null) && noteLikeRepository.existsByNoteAndUserId(note, userId);
        boolean isBookmarked = (userId != null) && noteBookmarkRepository.existsByNoteAndUserId(note, userId);
        return new NoteResponseDto(note, isLiked, isBookmarked);
    }

    @Transactional(readOnly = true)
    public NoteResponseDto getNoteById(Long noteId) {
        return getNoteById(noteId, null);
    }

    @Transactional(readOnly = true)
    public List<NoteResponseDto> getNotesByUserId(Integer targetUserId, Integer currentUserId) {
        return noteRepository.findByNoteUserId(targetUserId).stream()
                .map(note -> {
                    boolean isLiked = (currentUserId != null) && noteLikeRepository.existsByNoteAndUserId(note, currentUserId);
                    boolean isBookmarked = (currentUserId != null) && noteBookmarkRepository.existsByNoteAndUserId(note, currentUserId);
                    return new NoteResponseDto(note, isLiked, isBookmarked);
                })
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<NoteResponseDto> getNotesByUserId(Integer userId) {
        return getNotesByUserId(userId, null);
    }
}