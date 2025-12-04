package com.school.StudyShare.notes.dto;

import com.school.StudyShare.notes.entity.Note;
import com.fasterxml.jackson.annotation.JsonProperty; // 💡 [필수] 임포트 확인
import lombok.Getter;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor; // @Builder나 @AllArgsConstructor 사용 시 불필요할 수 있지만, 안정성을 위해 유지

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class NoteResponseDto {

    // 💡 [수정] JSON 직렬화 키 명시
    @JsonProperty("id")
    private Long id;

    @JsonProperty("user_id") // Flutter에서 userId를 user_id로 받을 수 있도록 명시
    private Integer userId;

    @JsonProperty("title") // DTO 필드명은 title
    private String title;

    // 💡 [핵심 수정] Flutter가 기대하는 스네이크 케이스 키로 직렬화하도록 명시
    @JsonProperty("note_subject_id")
    private Integer noteSubjectId;

    @JsonProperty("note_content")
    private String noteContent;

    @JsonProperty("note_file_url")
    private String noteFileUrl;

    @JsonProperty("likes_count") // Flutter의 likesCount와 DTO 필드가 다르다면 명시
    private Integer likesCount;

    @JsonProperty("comments_count")
    private Integer commentsCount;

    @JsonProperty("comments_likes_count")
    private Integer commentsLikesCount;

    @JsonProperty("create_date") // Flutter가 기대하는 키에 맞춤
    private LocalDateTime createDate;

    // Entity를 DTO로 변환하는 생성자 (유지)
    public NoteResponseDto(Note note) {
        this.id = note.getId();
        this.userId = note.getNoteUserId();
        this.title = note.getNoteTitle();
        this.noteSubjectId = note.getNoteSubjectId();
        this.noteContent = note.getNoteContent();
        this.noteFileUrl = note.getNoteFileUrl();
        this.likesCount = note.getNoteLikesCount();
        this.commentsCount = note.getNoteCommentsCount();
        this.commentsLikesCount = note.getNoteCommentsLikesCount();
        this.createDate = note.getNoteCreateDate();
    }
}