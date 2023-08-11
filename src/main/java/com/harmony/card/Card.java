package com.harmony.card;

import com.harmony.boardColumn.BoardColumn;
import com.harmony.cardUser.CardUser;
import com.harmony.comment.Comment;
import com.harmony.common.Timestamped;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
// jpa
@Entity
@Table(name = "cards")
public class Card extends Timestamped {
    /**
     * 컬럼 - 연관관계 컬럼을 제외한 컬럼을 정의합니다.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "card_id", nullable = false, updatable = false)
    private Long id;

    @Column(name = "card_name", nullable = false)
    private String cardname;

    @Column(name = "card_desc")
    private String description;

    @Column(name = "card_color", nullable = false)
    private String color;

    @Column(name = "deadline")
    private LocalDate deadline;

    @Column(name = "card_order")
    private Long cardOrder;

    /**
     * 생성자 - 약속된 형태로만 생성가능하도록 합니다.
     */
  
    /**
     * 연관관계 - Foreign Key 값을 따로 컬럼으로 정의하지 않고 연관 관계로 정의합니다.
     */
    @ManyToOne
    @JoinColumn(name = "column_id")
    private BoardColumn boardColumn;

    // 카드가 삭제되면 해당 카드에 존재하는 댓글도 함께 삭제
    @OneToMany(mappedBy = "card", fetch = FetchType.EAGER, cascade = CascadeType.REMOVE)
    @Column(name = "comments")
    @Builder.Default
    List<Comment> comments = new ArrayList<>();

    @OneToMany(mappedBy = "card", orphanRemoval = true)
    @Builder.Default
    private Set<CardUser> cardUsers = new LinkedHashSet<>();

    /**
     * 연관관계 편의 메소드 - 반대쪽에는 연관관계 편의 메소드가 없도록 주의합니다.
     */

    /**
     * 서비스 메소드 - 외부에서 엔티티를 수정할 메소드를 정의합니다. (단일 책임을 가지도록 주의합니다.)
     */
    public void updateCard(CardRequestDto requestDto) {
        if (requestDto.getCardName() != null) {
            this.cardname = requestDto.getCardName();
        }
        if (requestDto.getColor() != null) {
            this.color = requestDto.getColor();
        }
        if (requestDto.getDeadline() != null) {

            this.deadline = LocalDate.parse(requestDto.getDeadline(), DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        }
        if (requestDto.getDesc() != null) {
            this.description = requestDto.getDesc();
        }
        if (requestDto.getCardOrder() != null) {
            this.cardOrder = requestDto.getCardOrder();
        }
    }

    public void addCardUser(CardUser cardUser) {
        this.cardUsers.add(cardUser);
    }

    public void addComment(Comment comment) {
        this.comments.add(comment);
        comment.setCard(this);
    }

    public void removeCardUser(CardUser cardUser) {
        this.cardUsers.remove(cardUser);
    }


    public void clearCardUsers() {
        this.cardUsers.clear();
    }

    public void setBoardColumn(BoardColumn boardColumn) {
        this.boardColumn = boardColumn;
    }

    public void setCardOrder(Integer order) {
        this.cardOrder = Long.valueOf(order);
    }
}
