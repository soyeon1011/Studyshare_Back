package com.shcool.StudyShare.notes.entity; // 수정된 부분

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;      // 1. @Table import 추가
import jakarta.persistence.Column;      // 2. @Column import 추가
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;         // 3. @Setter import 추가
import org.hibernate.annotations.CreationTimestamp; // 4. 날짜 자동 생성을 위한 import 추가
import java.time.LocalDateTime; // 5. datetime 매핑을 위한 import 추가

@Entity
@Getter
@Setter                 // 6. 필드값 수정을 위해 @Setter 추가
@NoArgsConstructor
@Table(name = "notes")  // 7. 데이터베이스의 'notes' 테이블과 연결
public class Note {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // DB의 'id' (PK)

    // DB의 'user_id' (int)와 매핑
    // (기존 String author 필드 대신 user_id를 사용)
    @Column(name = "user_id")
    private Integer userId;

    // DB의 'note_title' (varchar)와 매핑
    @Column(name = "note_title")
    private String title; // (자바 필드명은 title을 사용)

    // DB의 'note_subject_id' (int)와 매핑
    // (기존 String subject 필드 대신 note_subject_id를 사용)
    @Column(name = "note_subject_id")
    private Integer noteSubjectId;

    // DB의 'note_content' (varchar)와 매핑
    // (기존 String preview 필드 대신 note_content를 사용)
    @Column(name = "note_content")
    private String noteContent;

    // DB의 'note_file_url' (varchar)와 매핑
    @Column(name = "note_file_url")
    private String noteFileUrl;

    // DB의 'note_likes_count' (int)와 매핑
    // (필드명을 likesCount로 변경)
    @Column(name = "note_likes_count")
    private int likesCount;

    // DB의 'note_comments_count' (int)와 매핑
    // (필드명을 commentsCount로 변경)
    @Column(name = "note_comments_count")
    private int commentsCount;

    // DB의 'note_create_date' (datetime)와 매핑
    @CreationTimestamp // 11. 엔티티가 저장될 때 현재 시간을 자동으로 저장
    @Column(name = "note_create_date", updatable = false) // 수정 시에는 날짜가 변경되지 않도록 설정
    private LocalDateTime createDate;

    // 12. 기존 생성자는 필드 구성이 달라졌으므로 삭제했습니다.
    // @NoArgsConstructor와 @Setter를 이용해 객체를 생성하고 값을 설정하는 것을 권장합니다.
}