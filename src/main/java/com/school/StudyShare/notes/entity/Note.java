package com.school.StudyShare.notes.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.hibernate.annotations.CreationTimestamp; // 💡 [필수 임포트] CreationTimestamp
import java.time.LocalDateTime;

@Entity
@Table(name = "notes")
@Getter
@Setter
@NoArgsConstructor
public class Note {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 💡 [필수] 필드명 및 JSON 매핑 유지
    @JsonProperty("user_id")
    @Column(name = "user_id", nullable = false)
    private Integer noteUserId;

    @JsonProperty("note_title")
    @Column(name = "note_title", length = 200, nullable = false)
    private String noteTitle;

    @JsonProperty("note_subject_id")
    @Column(name = "note_subject_id", nullable = false)
    private Integer noteSubjectId;

    @JsonProperty("note_content")
    @Column(name = "note_content", columnDefinition = "TEXT", nullable = false)
    private String noteContent;

    @JsonProperty("note_file_url")
    @Column(name = "note_file_url")
    private String noteFileUrl;

    @JsonProperty("note_likes_count")
    @Column(name = "note_likes_count")
    private Integer noteLikesCount = 0;

    @JsonProperty("note_comments_count")
    @Column(name = "note_comments_count")
    private Integer noteCommentsCount = 0;

    @JsonProperty("note_comments_likes_count")
    @Column(name = "note_comments_likes_count")
    private Integer noteCommentsLikesCount = 0;

    @JsonProperty("note_bookmarks_count")
    @Column(name = "note_bookmarks_count")
    private Integer noteBookmarksCount = 0;

    // 💡 [수정] DB 저장 시 현재 시각 자동 삽입 (PrePersist 로직 대체)
    @CreationTimestamp
    @JsonProperty("note_create_date")
    @Column(name = "note_create_date", nullable = false)
    private LocalDateTime noteCreateDate;

    // ❌ @PrePersist 메서드 제거 또는 주석 처리 (CreationTimestamp와 중복되어 불필요)
    /*
    @PrePersist
    public void prePersist() {
        this.noteCreateDate = LocalDateTime.now();
    }
    */
}