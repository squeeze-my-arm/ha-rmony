package com.harmony.board;

import com.harmony.boardColumn.BoardColumn;
import com.harmony.boardUser.BoardUser;
import com.harmony.common.Timestamped;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

// lombok
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
// jpa
@Entity
@Table(name = "boards")
public class Board extends Timestamped {
    /**
     * 컬럼 - 연관관계 컬럼을 제외한 컬럼을 정의합니다.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "board_id", nullable = false, updatable = false)
    private Long id;

    @Column(name = "board_title", nullable = false)
    private String boardTitle;

    @Column(name = "board_color", nullable = false)
    private String boardColor;

    @Column(name = "board_desc")
    private String boardDesc;
    /**
     * 연관관계 - Foreign Key 값을 따로 컬럼으로 정의하지 않고 연관 관계로 정의합니다.
     */
    @OneToMany(mappedBy = "board", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<BoardUser> boardUsers = new LinkedHashSet<>();

    @OneToMany(mappedBy = "board", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("boardColumnOrder ASC")
    private List<BoardColumn> boardColumns = new ArrayList<>();
    /**
     * 생성자 - 약속된 형태로만 생성가능하도록 합니다.
     */
    @Builder
    public Board(String boardTitle, String boardColor, String boardDesc) {
        this.boardTitle = boardTitle;
        this.boardColor = boardColor;
        this.boardDesc = boardDesc;
    }

    /**
     * 연관관계 편의 메소드 - 반대쪽에는 연관관계 편의 메소드가 없도록 주의합니다.
     */


    /**
     * 서비스 메소드 - 외부에서 엔티티를 수정할 메소드를 정의합니다. (단일 책임을 가지도록 주의합니다.)
     */
    public void update(BoardRequestDto boardRequestDto) {
        if (boardRequestDto.getBoardTitle() != null) this.boardTitle = boardRequestDto.getBoardTitle();
        if (boardRequestDto.getBoardColor() != null) this.boardColor = boardRequestDto.getBoardColor();
        if (boardRequestDto.getBoardDesc() != null) this.boardDesc = boardRequestDto.getBoardDesc();
    }

    public Integer getLastBoardColumnOrder() {
        if (boardColumns.isEmpty()) {
            return 0;
        } else {
            return boardColumns.get(boardColumns.size() - 1).getBoardColumnOrder();
        }
    }

    public void addColumnList(BoardColumn boardColumn) {
        this.boardColumns.add(boardColumn);
    }
}
