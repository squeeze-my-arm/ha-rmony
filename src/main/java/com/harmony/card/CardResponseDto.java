package com.harmony.card;


import com.harmony.common.ApiResponseDto;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class CardResponseDto extends ApiResponseDto {

    private Long cardId;
    private String cardname;
    private String cardColor;
    private String cardDesc;
    private LocalDateTime createdAt;
    private String deadline;
    // 담당자 (책임자)
    // private Comment

    public CardResponseDto(Card card) {
        this.cardId = card.getId();
        this.cardname = card.getCardname();
        this.cardColor = card.getColor();
        this.cardDesc = card.getDescription();
        this.createdAt = card.getCreatedAt();
        this.deadline = card.getDeadline();
    }

}
