package com.harmony.card;

import com.harmony.comment.CommentResponseDto;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CardRepository extends JpaRepository<Card, Long> {

    List<Card> findByBoardColumn_Id(Long boardColumnId);
    List<Card> findByBoardColumn_IdOrderByCardOrder(Long boardColumnId);
}
