package com.harmony.boardColumn;

import com.harmony.user.User;

public interface BoardColumnService {

    /*
    컬럼 생성
    생성할 컬럼 정보
    유저 정보
     */
    BoardColumnResponseDto createBoardColumn(BoardColumnRequestDto boardColumnRequestDto, User user);


    /*
    컬럼 수정
    수정할 컬럼 ID
    수정할 컬럼 정보
    유저 정보
     */
    BoardColumnResponseDto updateBoardColumn(Long columnId, BoardColumnRequestDto boardColumnRequestDto, User user);

    /*
    컬럼 삭제
    컬럼 ID
     */
    void deleteBoardColumn(Long columnId);

    /*
    컬럼 찾기
    컬럼 ID
     */
    BoardColumn findBoardColumn(Long columnId);
}
