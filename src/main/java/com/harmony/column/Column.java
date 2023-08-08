package com.harmony.column;

import jakarta.persistence.*;
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
}
