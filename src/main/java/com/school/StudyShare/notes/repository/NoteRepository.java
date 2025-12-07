package com.school.StudyShare.notes.repository;

import com.school.StudyShare.notes.entity.Note;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NoteRepository extends JpaRepository<Note, Long> {

    // JpaRepository가 기본 CRUD (save, findById, findAll, deleteById)를 제공합니다.

    // 💡 [추가] 최신순으로 모든 노트 조회 (ORDER BY createDate DESC)
    List<Note> findAllByOrderByNoteCreateDateDesc(); // 💡 엔티티 필드명 'noteCreateDate' 사용

    // [유지] 유저 ID로 모든 노트 찾기
    List<Note> findByNoteUserId(Integer userId);

    // [유지] 과목 ID로 모든 노트 찾기
    List<Note> findByNoteSubjectId(Integer noteSubjectId);

    // 💡 [추가] 제목 또는 '순수 텍스트' 내용에서 검색 (최신순 정렬)
    // 기존에 findByNoteTitleContainingOrNoteContentContaining... 를 썼다면 이걸로 교체하세요!
    List<Note> findByNoteTitleContainingOrNotePlainTextContainingOrderByNoteCreateDateDesc(String title, String plainText);
}