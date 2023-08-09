package com.harmony.boardColumn;

import com.harmony.board.Board;
import com.harmony.board.BoardService;
import com.harmony.security.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@Slf4j
public class BoardColumnController {

    private final BoardColumnServiceImpl boardColumnService;
    private final BoardService boardService;

    // 컬럼 생성
    // 하이픈 사용 유무 체크
    @PostMapping("/board-columns")
    public ResponseEntity<BoardColumnResponseDto> createBoardColumn(@RequestBody BoardColumnRequestDto boardColumnRequestDto,
                                                  @AuthenticationPrincipal UserDetailsImpl userDetails) {
        BoardColumnResponseDto boardColumn = boardColumnService.createBoardColumn(boardColumnRequestDto, userDetails.getUser());
        return ResponseEntity.status(201).body(boardColumn);
    }

    // 컬럼 수정
    @PutMapping("/board-columns/{columnId}")
    public ResponseEntity<BoardColumnResponseDto> updateBoardColumn(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                                        @RequestBody BoardColumnRequestDto boardColumnRequestDto,
                                                        @PathVariable Long columnId) {

            BoardColumn boardColumn = boardColumnService.findBoardColumn(columnId);
            BoardColumnResponseDto responseDto = boardColumnService.updateBoardColumn(boardColumn, boardColumnRequestDto, userDetails.getUser());
            return ResponseEntity.ok().body(responseDto);

    }

    // 컬럼 삭제
    @DeleteMapping("/board-columns/{columnId}")
    public ResponseEntity<String> deleteBoardColumn(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                                    @PathVariable Long columnId) {
            BoardColumn boardColumn = boardColumnService.findBoardColumn(columnId);
//            log.info(boardColumn.getBoardColumnName());
            Board board = boardService.findBoard(boardColumn.getBoard().getId());
//            log.info(board.getBoardTitle());
            boardColumnService.deleteBoardColumn(boardColumn, board);
//            boardColumnRepository.deleteById(boardColumn.getId());
            return ResponseEntity.ok().body("컬럼이 삭제 되었습니다.");
    }

}

