package com.harmony.boardUser;

import com.harmony.board.Board;
import com.harmony.board.BoardService;
import com.harmony.user.User;
import com.harmony.user.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class BoardUserService {

    private final UserService userService;
    private final BoardService boardService;
    private final BoardUserRepository boardUserRepository;

    public String joinUserBoard(Long boardId, String username) {
        User user = userService.findUser(username);
        Board board = boardService.findBoard(boardId);
        Optional<BoardUser> boardUser = boardUserRepository.findByUserAndBoard(user, board);
        if (boardUser.isEmpty()) {
            boardUserRepository.save(new BoardUser(board, user, BoardUserEnum.USER));
        }
        
        if (SecurityContextHolder.getContext().getAuthentication().getPrincipal() instanceof UserDetails) {
            UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            if (userDetails.getUsername().equals(username)) {
                // 해당 보드 페이지로 넘어가도록 할 예정
                return "redirect:/api/boards/" + boardId;
            }
        }
        return "loginsignup";
    }

    public ResponseEntity<String> deleteBoardUser(Long boardId, User user) {
        Board board = boardService.findBoard(boardId);
        BoardUser boardUser = boardUserRepository.findByUserAndBoard(user, board).orElseThrow();
        if (boardUser.getRole() == BoardUserEnum.ADMIN) {
            return ResponseEntity.badRequest().body("관리자는 떠나기가 불가능합니다:)");
        }
        boardUserRepository.deleteById(boardUser.getId());
        return ResponseEntity.ok().body("떠나기 성공하였습니다.");
    }
}
