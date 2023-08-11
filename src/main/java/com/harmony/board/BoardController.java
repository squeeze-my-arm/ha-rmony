package com.harmony.board;

import com.harmony.security.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/api")
@RequiredArgsConstructor
public class BoardController {

    private final BoardService boardService;

    // 보드 전체 조회
    @GetMapping("/boards")
    public String getBoardList(@AuthenticationPrincipal UserDetailsImpl userDetails, Model model) {
        List<BoardResponseDto> result = boardService.getBoardList(userDetails.getUser());
        model.addAttribute("user", userDetails.getUser().getNickname());
        model.addAttribute("boardList", result);
        return "index";
    }

    // 보드 상세 조회

//    @GetMapping("/boards/{boardId}")
//    public ResponseEntity<BoardResponseDto> getBoard(@PathVariable Long boardId, @AuthenticationPrincipal UserDetailsImpl userDetails) {
//        Board board = boardService.findBoard(boardId);
//        BoardResponseDto result = boardService.getBoard(board, userDetails.getUser());
//        return ResponseEntity.ok().body(result);
//    }

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
        Board board = boardService.findBoard(boardId);
        boardService.deleteBoard(board, userDetails.getUser());
        return ResponseEntity.ok().body("board 삭제 성공");
    }
}
