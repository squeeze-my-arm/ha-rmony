package com.harmony.column;

import com.harmony.board.Board;
import com.harmony.card.Card;
import com.harmony.cardUser.CardUser;
import jakarta.persistence.*;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "columns")
public class Columns {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="columns_id", nullable = false, updatable = false)
    private Long id;

    @Column(name="column_name", nullable = false)
    private String columnName;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "board_id", nullable = false, updatable = false)
    public Board board;

    @Column(name = "column_order", nullable = false)
    private Integer columnOrder;

    @OneToMany(mappedBy = "column")
    private Set<Card> cards=new LinkedHashSet<>();

    public void addCard(Card card) {
        this.cards.add(card);
    }
}
