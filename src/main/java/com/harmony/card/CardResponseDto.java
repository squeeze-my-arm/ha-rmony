package com.harmony.card;


import com.harmony.comment.CommentResponseDto;
import com.harmony.common.ApiResponseDto;
import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

@Getter
public class CardResponseDto extends ApiResponseDto {

    private Long cardId;
    private String cardName;
    private String color;
    private String desc;
    private LocalDateTime createdAt;
    private LocalDate deadline;
    private Long cardOrder;
    private String columnName;
    private Long boardId;
    private List<CommentResponseDto> commentList = new ArrayList<>();
    private Set<String> cardUsernames = new LinkedHashSet<>();


    public CardResponseDto(Card card) {
        this.boardId = card.getBoardColumn().getBoard().getId();
        this.columnName = card.getBoardColumn().getBoardColumnName();
        this.cardId = card.getId();
        this.cardName = card.getCardname();
        this.desc = card.getDescription();
        this.color = card.getColor();
        this.deadline = card.getDeadline();
        this.cardOrder = card.getCardOrder();
        this.createdAt = card.getCreatedAt();
    }

}
