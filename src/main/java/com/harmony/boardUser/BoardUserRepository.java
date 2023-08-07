package com.harmony.boardUser;

import com.harmony.board.Board;
import com.harmony.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BoardUserRepository extends JpaRepository<BoardUser, Long> {
    //@Query("select b from BoardUser b join fetch b.user")
    List<BoardUser> findAllByUser(User user);

    BoardUser findByBoardAndRole(Board board, BoardUserEnum boardUserEnum);
}
