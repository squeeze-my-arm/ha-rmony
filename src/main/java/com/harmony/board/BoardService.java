package com.harmony.board;

import com.harmony.boardColumn.BoardColumnRepository;
import com.harmony.boardColumn.BoardColumnResponseDto;
import com.harmony.boardUser.BoardUser;
import com.harmony.boardUser.BoardUserEnum;
import com.harmony.boardUser.BoardUserRepository;
import com.harmony.boardUser.BoardUserResponseDto;
import com.harmony.card.CardInColumnResponseDto;
import com.harmony.card.CardRepository;
import com.harmony.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BoardService {

    private final BoardRepository boardRepository;
    private final BoardUserRepository boardUserRepository;
    private final BoardColumnRepository boardColumnRepository;
    private final CardRepository cardRepository;

    public List<BoardResponseDto> getBoardList(User user) {
        return boardRepository.findAllById(boardUserRepository.findAllByUser(user)
                        .stream().map(BoardUser::getBoard).map(Board::getId).toList())
                .stream().map(BoardResponseDto::new).toList();
    }

    public BoardResponseDto getBoard(Board board, User user) {
        return new BoardResponseDto(board);
    }

    public List<BoardUserResponseDto> getBoardUser(Long boardId) {
        return boardUserRepository.findByBoard_Id(boardId).stream().map(BoardUserResponseDto::new).toList();
    }

    public List<BoardColumnResponseDto> getBoardColumn(Long boardId) {
        List<BoardColumnResponseDto> boardColumnResponseDto = boardColumnRepository.findAllByBoardIdOrderByBoardColumnOrder(boardId)
                .stream().map(BoardColumnResponseDto::new).toList();
        for (BoardColumnResponseDto b : boardColumnResponseDto) {
            b.setCardsName(cardRepository.findByBoardColumn_IdOrderByCardOrder(b.getColumnId()).stream().map(CardInColumnResponseDto::new).toList());
            b.setBoardId(boardId);
        }
        return boardColumnResponseDto;
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
        return new BoardResponseDto(boardRepository.save(board));
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
