package com.shcool.StudyShare.notes.repository;

import com.shcool.StudyShare.notes.entity.Note;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NoteRepository extends JpaRepository<Note, Long> {

    // JpaRepository가 기본 CRUD (save, findById, findAll, deleteById)를 제공합니다.

    // 1. 유저 ID로 모든 노트 찾기 (ex: '내가 작성한 노트' 목록)
    //    메서드 이름을 규칙에 맞게 지으면 JPA가 알아서 쿼리를 만들어줍니다.
    List<Note> findByUserId(Integer userId);

    // 2. 과목 ID로 모든 노트 찾기
    List<Note> findByNoteSubjectId(Integer noteSubjectId);
}