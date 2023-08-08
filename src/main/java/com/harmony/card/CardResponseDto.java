package com.harmony.card;


import com.harmony.comment.Comment;
import com.harmony.comment.CommentResponseDto;
import com.harmony.common.ApiResponseDto;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
public class CardResponseDto extends ApiResponseDto {

    private Long cardId;
    private String cardname;
    private String cardColor;
    private String cardDesc;
    private LocalDateTime createdAt;
    private String deadline;
    // 담당자 (책임자)
    // private String cardUser;
    private List<CommentResponseDto> commentList = new ArrayList<>();

    public CardResponseDto(Card card) {
        this.cardId = card.getId();
        this.cardname = card.getCardname();
        this.cardColor = card.getColor();
        this.cardDesc = card.getDescription();
        this.createdAt = card.getCreatedAt();
        this.deadline = card.getDeadline();
        for (Comment comment: card.getComments()) {
            this.commentList.add(new CommentResponseDto(comment));
        }
    }

}
