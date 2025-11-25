package com.shcool.StudyShare.notes.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "notes") // 1. DB 테이블 이름이 "notes" 임을 명시
@Getter
@Setter
@NoArgsConstructor
public class Note {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 2. DB 컬럼: user_id (작성자 ID)
    @Column(name = "user_id", nullable = false)
    private Integer userId;

    // 3. DB 컬럼: note_title (제목)
    // 작성자님 Service 코드에는 "title"로 되어있어서 매핑이 필요합니다.
    @Column(name = "note_title", length = 200, nullable = false)
    private String title;

    // 4. DB 컬럼: note_subject_id (과목 ID - 핵심!!)
    // 이 부분이 있어야 DB의 note_subject_id 컬럼에 1이 들어갑니다.
    @Column(name = "note_subject_id", nullable = false)
    private Integer noteSubjectId;

    // 5. DB 컬럼: note_content (내용)
    @Column(name = "note_content", columnDefinition = "TEXT", nullable = false)
    private String noteContent;

    // 6. DB 컬럼: note_file_url (파일 주소)
    @Column(name = "note_file_url")
    private String noteFileUrl;

    // 7. DB 컬럼: note_likes_count (좋아요 수)
    @Column(name = "note_likes_count")
    private int likesCount;

    // 8. DB 컬럼: note_comments_count (댓글 수)
    @Column(name = "note_comments_count")
    private int commentsCount;

    @Column(name = "note_comments_likes_count")
    private int commentsLikesCount;

    // 9. 생성일 (자동 저장)
    @Column(name = "note_create_date")
    private LocalDateTime createDate;


    // 엔티티가 저장되기 직전에 실행됨 (날짜 자동 생성)
    @PrePersist
    public void prePersist() {
        this.createDate = LocalDateTime.now();
    }
}