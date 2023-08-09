package com.harmony.boardColumn;

import com.harmony.board.Board;
import com.harmony.board.BoardService;
import com.harmony.user.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class BoardColumnServiceImpl implements BoardColumnService {
    private final BoardColumnRepository boardColumnRepository;
    private final BoardService boardService;

    @Override
    @Transactional
    public BoardColumnResponseDto createBoardColumn(BoardColumnRequestDto boardColumnRequestDto,
                                                    User user) {
        Board board = boardService.findBoard(boardColumnRequestDto.getBoardId());
        Integer boardColumnOrder = board.getLastBoardColumnOrder() + 1;

        BoardColumn boardColumn = new BoardColumn(board, boardColumnRequestDto.getBoardColumnName(), boardColumnOrder);
        board.addColumnList(boardColumn);

        return new BoardColumnResponseDto(boardColumnRepository.save(boardColumn));
    }

    @Override
    @Transactional
    public BoardColumnResponseDto updateBoardColumn(BoardColumn boardColumn, BoardColumnRequestDto boardColumnRequestDto, User user) {

        boardColumn.update(boardColumnRequestDto);
//        if (boardColumnRequestDto.getBoardColumnOrder()!=null) {
//
//        }
//        boardColumnRepository.save(boardColumn); // 이게 왜 있어야 하는가?
        return new BoardColumnResponseDto(boardColumn);
    }

    @Override
    @Transactional
    public void deleteBoardColumn(BoardColumn boardColumn, Board board) {
        Integer deleteOrder = boardColumn.getBoardColumnOrder();
//        log.info(boardColumn.getId().toString());
//        BoardColumn boardColumn = boardColumnRepository.findById(id).orElseThrow(
//                ()-> new RuntimeException("되라 ㅜ")
//        );
        boardColumnRepository.delete(boardColumn);

        List<BoardColumn> remainedBoardColumns = boardColumnRepository.findByBoardId(board.getId());
        for(BoardColumn column : remainedBoardColumns) {
            Integer currentOrder = column.getBoardColumnOrder();
            if(currentOrder > deleteOrder) {
                column.setBoardColumnOrder(currentOrder-1);
            }
        }

//        Board loadedBoard = boardService.findBoard(board.getId()); // 영속성 컨텍스트에서 다시 로드
//
//        loadedBoard.getBoardColumns().stream()
//                .filter(column -> column.getBoardColumnOrder() > deleteOrder)
//                .forEach(column -> column.setBoardColumnOrder(column.getBoardColumnOrder() - 1));

//        List<BoardColumn> columnsToUpdate = board.getBoardColumnList().stream()
//                .filter(column -> column.getBoardColumnOrder() > deleteOrder)
//                .toList();
//        for (BoardColumn columnToUpdate : columnsToUpdate) {
//            columnToUpdate.setBoardColumnOrder(columnToUpdate.getBoardColumnOrder() - 1);
//        }
    }

    public BoardColumn findBoardColumn(Long columnId) {
        return boardColumnRepository.findById(columnId).orElseThrow(()->
                new IllegalArgumentException("존재하지 않는 컬럼입니다."));
    }
}
