package com.harmony.board;

import com.harmony.boardUser.BoardUser;
import com.harmony.boardUser.BoardUserEnum;
import com.harmony.boardUser.BoardUserRepository;
import com.harmony.user.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class BoardService {

    private final BoardRepository boardRepository;
    private final BoardUserRepository boardUserRepository;


    public List<BoardResponseDto> getBoardList(User user) {
        List<Long> idList = boardUserRepository.findAllByUser(user).stream().map(BoardUser::getBoard).map(Board::getId).toList();
        log.info("id 목록 뽑아오기");
        return boardRepository.findAllById(idList).stream().map(BoardResponseDto::new).toList();

      /*  List<BoardUser> boards = boardUserRepository.findAllByUser(user);
          List<Board> boardList = new ArrayList<>();

          for (BoardUser board : boards) {
            boardList.add(board.getBoard());
          }

          return boardList.stream().map(BoardResponseDto::new).toList(); */
    }

    public BoardResponseDto getBoard(Board board, User user) {
        return new BoardResponseDto(board);
    }

    public BoardResponseDto createBoard(BoardRequestDto boardRequestDto, User user) {
        Board board = boardRepository.save(
                new Board(boardRequestDto.getBoardTitle(), boardRequestDto.getBoardColor(),
                        boardRequestDto.getBoardDesc()));
        BoardUserEnum role = BoardUserEnum.ADMIN;
        BoardUser boardUser = boardUserRepository.save(new BoardUser(board, user, role));
        return new BoardResponseDto(board);
    }

    @Transactional
    public BoardResponseDto updateBoard(Board board, BoardRequestDto boardRequestDto, User user) {
        board.update(boardRequestDto);
        Board savedBoard = boardRepository.save(board);
        return new BoardResponseDto(savedBoard);
    }

    @Transactional
    public void deleteBoard(Board board, User user) {
        boardRepository.delete(board);
    }

    public Board findBoard(Long id) {
        return boardRepository.findById(id).orElseThrow(() ->
                new IllegalArgumentException("선택한 게시글은 존재하지 않습니다.")
        );
    }

}
