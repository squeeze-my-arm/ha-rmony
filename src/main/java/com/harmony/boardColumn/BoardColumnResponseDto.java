package com.harmony.boardColumn;

import lombok.Getter;

@Getter
public class BoardColumnResponseDto {
    private String boardColumnName;

    public BoardColumnResponseDto(BoardColumn boardColumn) {
        this.boardColumnName = boardColumn.getBoardColumnName();
    }
}
