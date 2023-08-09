package com.harmony.boardColumn;

import com.harmony.board.Board;
import com.harmony.card.Card;
import com.harmony.common.Timestamped;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.util.LinkedHashSet;
import java.util.Set;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "board_column")
public class BoardColumn extends Timestamped {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "columns_id", nullable = false, updatable = false)
  private Long id;




    @Column(name="board_column_name", nullable = false)
    private String boardColumnName;

    @ManyToOne
    @JoinColumn(name = "board_id", nullable = false, updatable = false)
    public Board board;


    @Column(name = "column_order", nullable = false)
    private Integer boardColumnOrder;


    @OneToMany(mappedBy = "boardColumn", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<Card> cards = new LinkedList<>();

    @Builder
    public BoardColumn(Board board, String boardColumnName, Integer boardColumnOrder) {
        this.board = board;
        this.boardColumnName = boardColumnName;
        this.boardColumnOrder = boardColumnOrder;
    }


    public void update(BoardColumnRequestDto boardColumnRequestDto) {
        if (boardColumnRequestDto.getBoardColumnName() != null) this.boardColumnName = boardColumnRequestDto.getBoardColumnName();
        if (boardColumnRequestDto.getBoardColumnOrder() != null) this.boardColumnOrder = boardColumnRequestDto.getBoardColumnOrder();
  }


  public void addCard(Card card) {
    this.cards.add(card);
  }
    public void changeCardOrder(Card card, Integer cardOrder) {
        this.cards.remove(card);
        this.cards.add(cardOrder, card);
    }

    public void removeCard(Card card) {
        this.cards.remove(card);
    }

    public void setBoardColumnOrder(Integer boardColumnOrder) {
        this.boardColumnOrder = boardColumnOrder;
    }
}
