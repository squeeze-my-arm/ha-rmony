package com.harmony.boardUser;

import com.harmony.board.Board;
import com.harmony.user.User;
import com.harmony.user.UserResponseDto;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface BoardUserRepository extends JpaRepository<BoardUser, Long> {
    //@Query("select b from BoardUser b join fetch b.user")
    List<BoardUser> findAllByUser(User user);
    //로그인한 유저가 특정 보드에 보드유저로 등록되어있는가?
    Boolean existsByUserAndBoard_Id(User user,Long boardId);
    BoardUser findByBoardAndRole(Board board, BoardUserEnum boardUserEnum);

    Optional<BoardUser> findByUserAndBoard(User user, Board board);

    List<BoardUser> findByBoard_Id(Long boardid);
}
