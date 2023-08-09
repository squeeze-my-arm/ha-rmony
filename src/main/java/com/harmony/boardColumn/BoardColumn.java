package com.harmony.boardColumn;

import com.harmony.board.Board;
import com.harmony.common.Timestamped;
import jakarta.persistence.*;
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
    @Column(name="columns_id", nullable = false, updatable = false)
    private Long id;

    @Column(name="board_column_name", nullable = false)
    private String boardColumnName;

    @ManyToOne
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

    public void update(BoardColumnRequestDto boardColumnRequestDto) {
        if (boardColumnRequestDto.getBoardColumnName() != null) this.boardColumnName = boardColumnRequestDto.getBoardColumnName();
        if (boardColumnRequestDto.getBoardColumnOrder() != null) this.boardColumnOrder = boardColumnRequestDto.getBoardColumnOrder();
    }

    public void setBoardColumnOrder(Integer boardColumnOrder) {
        this.boardColumnOrder = boardColumnOrder;
    }
}
