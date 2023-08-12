package com.harmony.boardColumn;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Arrays;
import java.util.List;

public interface BoardColumnRepository extends JpaRepository<BoardColumn, Long> {

    List<BoardColumn> findByBoardIdOrderByBoardColumnOrder(Long boardId);
    List<BoardColumn> findByBoardId(Long boardId);

    List<BoardColumn> findAllByBoardIdOrderByBoardColumnOrder(Long boardId);

}
