package com.harmony.boardColumn;

import com.harmony.board.Board;
import com.harmony.user.User;

import java.util.List;

public interface BoardColumnService {

    /*

     */
    BoardColumnResponseDto createBoardColumn(BoardColumnRequestDto boardColumnRequestDto, User user);


    /*
    컬럼 수정
    기존 게시글 정보
    수정할 게시글 정보
    유저 정보
     */
    BoardColumnResponseDto updateBoardColumn(BoardColumn boardColumn, BoardColumnRequestDto boardColumnRequestDto, User user);

    /*
    컬럼 삭제
    컬럼 정보
    보드 정보
     */
    void deleteBoardColumn(BoardColumn boardColumn, Board board);

//    void deleteBoardColumn(Long id);

    BoardColumn findBoardColumn(Long columnId);
}
