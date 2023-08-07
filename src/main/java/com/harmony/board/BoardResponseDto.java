package com.harmony.board;

import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class BoardResponseDto {
    private Long boardId;
    private String boardTitle;
    private String boardColor;
    private LocalDateTime createAt;

    public BoardResponseDto(Board board) {
        this.boardId = board.getId();
        this.boardTitle = board.getBoardTitle();
        this.boardColor = board.getBoardColor();
        this.createAt = board.getCreatedAt();
    }
}
