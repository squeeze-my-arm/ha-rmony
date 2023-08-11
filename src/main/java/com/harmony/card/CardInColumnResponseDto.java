package com.harmony.card;

import lombok.Getter;

@Getter
public class CardInColumnResponseDto {

    private String cardName;
    private Long cardId;

    public CardInColumnResponseDto(Card card) {
        this.cardId = card.getId();
        this.cardName = card.getCardname();
    }
}
