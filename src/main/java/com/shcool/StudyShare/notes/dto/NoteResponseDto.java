package com.shcool.StudyShare.notes.dto;

import com.shcool.StudyShare.notes.entity.Note;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class NoteResponseDto {

    private final Long id;
    private final Integer userId;
    private final String title;
    private final Integer noteSubjectId;
    private final String noteContent;
    private final String noteFileUrl;
    private final int likesCount;
    private final int commentsCount;
    private final LocalDateTime createDate;

    // Entity를 DTO로 변환하는 생성자
    public NoteResponseDto(Note note) {
        this.id = note.getId();
        this.userId = note.getUserId();
        this.title = note.getTitle();
        this.noteSubjectId = note.getNoteSubjectId();
        this.noteContent = note.getNoteContent();
        this.noteFileUrl = note.getNoteFileUrl();
        this.likesCount = note.getLikesCount();
        this.commentsCount = note.getCommentsCount();
        this.createDate = note.getCreateDate();
    }
}