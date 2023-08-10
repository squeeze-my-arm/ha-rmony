package com.harmony.boardColumn;

import com.harmony.card.CardInColumnResponseDto;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class BoardColumnResponseDto {

    private Long boardId;

    private Long columnId;

    private final String boardColumnName;

    private final Integer boardColumnOrder;

    private List<CardInColumnResponseDto> cardsName = new ArrayList<>();

    public BoardColumnResponseDto(BoardColumn boardColumn) {
        this.columnId = boardColumn.getId();
        this.boardColumnName = boardColumn.getBoardColumnName();
        this.boardColumnOrder = boardColumn.getBoardColumnOrder();
    }

    public void setBoardId(Long boardId) {
        this.boardId = boardId;
    }

    public void setCardsName(List<CardInColumnResponseDto> cardsName) {
        this.cardsName = cardsName;
    }
}