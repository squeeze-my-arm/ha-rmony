package com.harmony.boardUser;

import com.harmony.board.Board;
import com.harmony.board.BoardService;
import com.harmony.user.User;
import com.harmony.user.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class BoardUserService {

    private final UserService userService;
    private final BoardService boardService;
    private final BoardUserRepository boardUserRepository;

    public void joinUserBoard(Long boardId, String username) {
        User user = userService.findUser(username);
        Board board = boardService.findBoard(boardId);
        Optional<BoardUser> boardUser = boardUserRepository.findByUserAndBoard(user, board);
        if (boardUser.isEmpty()) {
            boardUserRepository.save(new BoardUser(board, user, BoardUserEnum.USER));
        }
    }

    public void deleteBoardUser(Long boardId, User user) {
        Board board = boardService.findBoard(boardId);
        BoardUser boardUser = boardUserRepository.findByUserAndBoard(user, board).orElseThrow();
        if (boardUser.getRole() == BoardUserEnum.ADMIN) {
            throw new IllegalArgumentException("관리자는 삭제 불가능함");
        }
        boardUserRepository.deleteById(boardUser.getId());
    }
}
