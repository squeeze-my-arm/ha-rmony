package com.harmony.boardColumn;

import com.harmony.security.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class BoardColumnController {


    private final BoardColumnService boardColumnService;

    // 컬럼 생성
    // 하이픈 사용 유무 체크
    @PostMapping("/board-columns")
    public ResponseEntity<BoardColumnResponseDto> createPost(@RequestBody BoardColumnRequestDto boardColumnRequestDto,
                                                      @AuthenticationPrincipal UserDetailsImpl userDetails) {
        BoardColumnResponseDto boardColumn = boardColumnService.createBoardColumn(boardColumnRequestDto, userDetails.getUser());
        return ResponseEntity.status(201).body(boardColumn);
    }
}
