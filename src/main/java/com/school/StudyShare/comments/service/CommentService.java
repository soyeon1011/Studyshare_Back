package com.school.StudyShare.comments.service;

import com.school.StudyShare.comments.dto.CommentRequestDto;
import com.school.StudyShare.comments.dto.CommentResponseDto;
import com.school.StudyShare.comments.entity.Comment;
import com.school.StudyShare.comments.repository.CommentRepository;
import com.school.StudyShare.community.entity.Community;
import com.school.StudyShare.community.repository.CommunityRepository;
import com.school.StudyShare.notes.entity.Note;
import com.school.StudyShare.notes.repository.NoteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final NoteRepository noteRepository;
    private final CommunityRepository communityRepository;

    // 댓글 작성
    @Transactional
    public CommentResponseDto createComment(CommentRequestDto dto, Integer userId) {
        Comment comment = new Comment();
        comment.setUserId(userId);
        comment.setContent(dto.getContent());
        comment.setParentCommentId(dto.getParentCommentId());

        // 1. 노트 댓글인 경우
        if (dto.getNoteId() != null) {
            Note note = noteRepository.findById(dto.getNoteId())
                    .orElseThrow(() -> new IllegalArgumentException("해당 노트가 없습니다."));

            comment.setNote(note);

            // (옵션) 노트의 댓글 수 증가시키기 (Note 엔티티에 필드가 있다면)
            // note.setCommentsCount(note.getCommentsCount() + 1);
        }
        // 2. 커뮤니티 댓글인 경우
        else if (dto.getCommunityId() != null) {
            Community community = communityRepository.findById(dto.getCommunityId())
                    .orElseThrow(() -> new IllegalArgumentException("해당 게시글이 없습니다."));

            comment.setCommunity(community);

            // (옵션) 커뮤니티의 댓글 수 증가시키기
            // community.setCommentCount(community.getCommentCount() + 1);
        } else {
            throw new IllegalArgumentException("noteId 또는 communityId 중 하나는 필수입니다.");
        }

        Comment savedComment = commentRepository.save(comment);
        return new CommentResponseDto(savedComment);
    }

    // 노트별 댓글 조회
    @Transactional(readOnly = true)
    public List<CommentResponseDto> getCommentsByNoteId(Long noteId) {
        return commentRepository.findByNoteId(noteId).stream()
                .map(CommentResponseDto::new)
                .collect(Collectors.toList());
    }

    // 커뮤니티별 댓글 조회
    @Transactional(readOnly = true)
    public List<CommentResponseDto> getCommentsByCommunityId(Long communityId) {
        return commentRepository.findByCommunityId(communityId).stream()
                .map(CommentResponseDto::new)
                .collect(Collectors.toList());
    }
}