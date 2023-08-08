package com.harmony.boardColumn;

import com.harmony.user.User;

import java.util.List;

public interface BoardColumnService {

    /*

     */
    BoardColumnResponseDto createBoardColumn(BoardColumnRequestDto boardColumnRequestDto, User user);

    /*
    컬럼 전체 조회
     */
    List<BoardColumnResponseDto> getBoardColumn();

    /*
    컬럼 수정
    기존 게시글 정보
    수정할 게시글 정보
    유저 정보
     */
    BoardColumnResponseDto updateBoardColumn();

    /*
    게시글 삭제
    게시글 정보
    유저 정보
     */
    void deleteBoardColumn();
}
