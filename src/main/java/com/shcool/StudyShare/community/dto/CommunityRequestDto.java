package com.shcool.StudyShare.community.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class CommunityRequestDto {
    private String title;
    private String category; // "자유", "질문", "정보" 등 직접 입력
    private String content;
}