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

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "board_column")
public class BoardColumn extends Timestamped {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "columns_id", nullable = false, updatable = false)
  private Long id;

  @Column(name = "column_name", nullable = false)
  private String boardColumnName;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "board_id", nullable = false, updatable = false)
  public Board board;

  @Column(name = "column_order", nullable = false)
  private Integer boardColumnOrder;

  @Builder
  public BoardColumn(Board board, String boardColumnName, Integer boardColumnOrder) {
    this.board = board;
    this.boardColumnName = boardColumnName;
    this.boardColumnOrder = boardColumnOrder;

  }

  public void setBoardColumnOrder(Integer boardColumnOrder) {
    this.boardColumnOrder = boardColumnOrder;
  }

  @OneToMany(mappedBy = "boardColumn")
  private Set<Card> cards = new LinkedHashSet<>();

  public void addCard(Card card) {
    this.cards.add(card);
  }
}
