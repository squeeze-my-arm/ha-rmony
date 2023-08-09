package com.harmony.boardColumn;

import lombok.Getter;

@Getter
public class BoardColumnResponseDto {

    private final String boardColumnName;

    private final Integer boardColumnOrder;

    public BoardColumnResponseDto(BoardColumn boardColumn) {
        this.boardColumnName = boardColumn.getBoardColumnName();
        this.boardColumnOrder = boardColumn.getBoardColumnOrder();
    }
}
