package com.harmony.card;


import com.harmony.cardUser.CardUser;
import com.harmony.comment.Comment;
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
    // 담당자 (책임자)
    // private String cardUser;
    private List<CommentResponseDto> commentList = new ArrayList<>();
    private Set<String> cardUsernames = new LinkedHashSet<>();



//    public CardResponseDto(Card card) {
//        this.cardId = card.getId();
//        this.cardName = card.getCardname();
//        this.color = card.getColor();
//        this.desc = card.getDescription();
//        this.createdAt = card.getCreatedAt();
//        this.deadline = card.getDeadline();
//
//        if (card.getComments().size() > 0) {
//            for (Comment comment: card.getComments()) {
//                this.commentList.add(new CommentResponseDto(comment));
//            }
//        }
//    }

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
//        if (card.getCardUsers() != null) {
//            for (CardUser cu : card.getCardUsers()) {
//                this.cardUsernames.add(cu.getUser().getUsername());
//            }
//        }
    }

}
