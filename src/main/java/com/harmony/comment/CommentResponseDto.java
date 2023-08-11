package com.harmony.comment;


import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class CommentResponseDto {

    private Long id;
    private String username;
    private LocalDateTime createdAt;
    private String commentContent;

    public CommentResponseDto(Comment savedComment) {
        this.id = savedComment.getId();
        this.username = savedComment.getCommentUsername();
        this.createdAt = savedComment.getCreatedAt();
        this.commentContent = savedComment.getCommentContent();
    }
}
