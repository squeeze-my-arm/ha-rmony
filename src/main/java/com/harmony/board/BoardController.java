package com.harmony.board;

import com.harmony.security.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class BoardController {

    private final BoardService boardService;

    // 보드 전체 조회
    @GetMapping("/boards")
    public ResponseEntity<List<BoardResponseDto>> getBoards(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        List<BoardResponseDto> result = boardService.getBoards(userDetails.getUser());
        return ResponseEntity.ok().body(result);
    }

    // 보드 상세 조회


    // 보드 생성
    @PostMapping("/boards/create")
    public ResponseEntity<BoardResponseDto> createBoard(@RequestBody BoardRequestDto boardRequestDto, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        BoardResponseDto result = boardService.createBoard(boardRequestDto, userDetails.getUser());
        return ResponseEntity.status(201).body(result);
    }
    // 보드 수정

    // 보드 삭제

    // 보드 User 초대
}
