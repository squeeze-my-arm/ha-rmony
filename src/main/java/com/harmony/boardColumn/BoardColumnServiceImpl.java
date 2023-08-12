package com.harmony.boardColumn;

import com.harmony.board.Board;
import com.harmony.board.BoardService;
import com.harmony.user.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class BoardColumnServiceImpl implements BoardColumnService {

    private final BoardColumnRepository boardColumnRepository;

    private final BoardService boardService;

    // 컬럼 생성
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

    // 컬럼 수정
    @Override
    @Transactional
    public BoardColumnResponseDto updateBoardColumn(Long columnId, BoardColumnRequestDto boardColumnRequestDto, User user) {
        BoardColumn boardColumn = findBoardColumn(columnId);
        Board board = boardService.findBoard(boardColumn.getBoard().getId());

        Integer oldOrder = boardColumn.getBoardColumnOrder();
        Integer newOrder = boardColumnRequestDto.getBoardColumnOrder();

        if (newOrder != null /* && ! newOrder.equals(oldOrder)*/) { //변경이 있을 때
            log.info(boardColumnRequestDto.getBoardColumnOrder().toString());
            List<BoardColumn> remainedBoardColumnList = boardColumnRepository.findByBoardIdOrderByBoardColumnOrder(board.getId());
            // 기존 order
            if (newOrder < oldOrder) {
                for (BoardColumn column : remainedBoardColumnList) {

                    if (column.getBoardColumnOrder() >= newOrder && column.getBoardColumnOrder() < oldOrder) {
                        column.setBoardColumnOrder(column.getBoardColumnOrder() + 1);
                    }
                    boardColumnRepository.save(column);
                }
            } else {
                for (BoardColumn column : remainedBoardColumnList) {
                    if (column.getBoardColumnOrder() <= newOrder && column.getBoardColumnOrder() > oldOrder) {
                        column.setBoardColumnOrder(column.getBoardColumnOrder() - 1);
                    }
                    boardColumnRepository.save(column);
                }
            }
            remainedBoardColumnList.sort(Comparator.comparingInt(BoardColumn::getBoardColumnOrder));
            boardColumnRepository.saveAll(remainedBoardColumnList);
        }

        boardColumn.update(boardColumnRequestDto);
        boardColumnRepository.save(boardColumn);

        return new BoardColumnResponseDto(boardColumn);
    }

    // 컬럼 삭제
    @Override
    @Transactional
    public void deleteBoardColumn(Long columnId) {
        BoardColumn boardColumn = findBoardColumn(columnId);
        Board board = boardService.findBoard(boardColumn.getBoard().getId());

        Integer deleteOrder = boardColumn.getBoardColumnOrder();

        boardColumnRepository.delete(boardColumn);

        List<BoardColumn> remainedBoardColumnList = boardColumnRepository.findByBoardIdOrderByBoardColumnOrder(board.getId());
        for (BoardColumn column : remainedBoardColumnList) {
            Integer currentOrder = column.getBoardColumnOrder();
            if (currentOrder > deleteOrder) {
                column.setBoardColumnOrder(currentOrder - 1);
            }
        }
    }

    public BoardColumn findBoardColumn(Long columnId) {
        return boardColumnRepository.findById(columnId).orElseThrow(() ->
                new IllegalArgumentException("존재하지 않는 컬럼입니다."));
    }
}
