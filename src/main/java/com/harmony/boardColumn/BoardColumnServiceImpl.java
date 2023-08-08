package com.harmony.boardColumn;

import com.harmony.board.Board;
import com.harmony.board.BoardService;
import com.harmony.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BoardColumnServiceImpl implements BoardColumnService {
    private final BoardColumnRepository boardColumnRepository;
    private final BoardService boardService;

    @Override
    @Transactional
    public BoardColumnResponseDto createBoardColumn(BoardColumnRequestDto boardColumnRequestDto,
                                                    User user) {
        Board board = boardService.findBoard(boardColumnRequestDto.getBoardId());
        Integer boardColumnOrder = board.getLastBoardColumnOrder() + 1;

        BoardColumn boardColumn = new BoardColumn(board, boardColumnRequestDto.getColumnName(), boardColumnOrder);
        board.addColumnList(boardColumn);

        return new BoardColumnResponseDto(boardColumnRepository.save(boardColumn));
    }

    @Override
    public List<BoardColumnResponseDto> getBoardColumn() {
        return null;
    }

    @Override
    public BoardColumnResponseDto updateBoardColumn() {
        return null;
    }

    @Override
    public void deleteBoardColumn() {

    }
}
