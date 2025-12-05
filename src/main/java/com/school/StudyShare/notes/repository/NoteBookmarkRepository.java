package com.school.StudyShare.notes.repository;

import com.school.StudyShare.notes.entity.Note;
import com.school.StudyShare.notes.entity.NoteBookmark;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface NoteBookmarkRepository extends JpaRepository<NoteBookmark, Long> {
    Optional<NoteBookmark> findByNoteAndUserId(Note note, Integer userId);
    boolean existsByNoteAndUserId(Note note, Integer userId);
}