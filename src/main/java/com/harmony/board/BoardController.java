package com.harmony.board;

import com.harmony.boardColumn.BoardColumnResponseDto;
import com.harmony.boardUser.BoardUserResponseDto;
import com.harmony.security.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/api")
@RequiredArgsConstructor
public class BoardController {

    private final BoardService boardService;

    // 보드 전체 조회
    @GetMapping("/boards")
    public String getBoardList(@AuthenticationPrincipal UserDetailsImpl userDetails, Model model) {
        List<BoardResponseDto> result = new ArrayList<>();
        String nickname = "";

        if (userDetails != null) {
            result = boardService.getBoardList(userDetails.getUser());
            nickname = userDetails.getUser().getNickname();
        }

        model.addAttribute("user", nickname);
        model.addAttribute("boardList", result);
        return "index";
    }

    // 보드 상세 조회
    @GetMapping("/boards/{boardId}")
    public String getBoard(@PathVariable Long boardId, @AuthenticationPrincipal UserDetailsImpl userDetails, Model model) {
        // 일단 보드에 대한 정보가 필요함
        BoardResponseDto boardResponseDto = boardService.getBoard(boardService.findBoard(boardId), userDetails.getUser());
        model.addAttribute("board", boardResponseDto);

        // 해당 보드에 참여하고 있는 사용자에 대한 정보
        List<BoardUserResponseDto> boardUsers = boardService.getBoardUser(boardResponseDto.getBoardId());
        model.addAttribute("boardUsers", boardUsers);

        // 그리고 컬럼에 대한 정보가 필요함
        List<BoardColumnResponseDto> boardColumnResponseDto = boardService.getBoardColumn(boardResponseDto.getBoardId());
        model.addAttribute("columns", boardColumnResponseDto);
        return "board";
    }

    @ResponseBody
    // 보드 생성
    @PostMapping("/boards")
    public ResponseEntity<BoardResponseDto> createBoard(@RequestBody BoardRequestDto boardRequestDto, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        BoardResponseDto result = boardService.createBoard(boardRequestDto, userDetails.getUser());
        return ResponseEntity.status(201).body(result);
    }

    @ResponseBody
    // 보드 수정
    @PatchMapping("/boards/{boardId}")
    public ResponseEntity<BoardResponseDto> updateBoard(@PathVariable Long boardId, @RequestBody BoardRequestDto boardRequestDto, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        Board board = boardService.findBoard(boardId);
        BoardResponseDto result = boardService.updateBoard(board, boardRequestDto, userDetails.getUser());
        return ResponseEntity.status(201).body(result);
    }

    @ResponseBody
    // 보드 삭제
    @DeleteMapping("/boards/{boardId}")
    public ResponseEntity<String> deleteBoard(@PathVariable Long boardId, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        boardService.deleteBoard(boardService.findBoard(boardId), userDetails.getUser());
        return ResponseEntity.ok().body("board 삭제 성공");
    }
}
