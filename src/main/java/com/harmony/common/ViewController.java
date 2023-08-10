package com.harmony.common;


import com.harmony.board.BoardRepository;
import com.harmony.board.BoardResponseDto;
import com.harmony.boardColumn.BoardColumnRepository;
import com.harmony.boardColumn.BoardColumnResponseDto;
import com.harmony.card.CardInColumnResponseDto;
import com.harmony.card.CardRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@Controller
@Slf4j
@RequiredArgsConstructor
public class ViewController {
    private final BoardRepository boardRepository;
    private final BoardColumnRepository boardColumnRepository;
    private final CardRepository cardRepository;

    @GetMapping("/")
    public String mainPage() {
        return "index";
    }

    @GetMapping("/api/users/login-page")
    public String loginPage() {
        return "loginsignup";
    }

//    @GetMapping("/view/boards/{boardId}/detail-page")
//    public String boardDetailPage(@PathVariable Long boardId, @AuthenticationPrincipal UserDetailsImpl userDetails) {
//        return "boardDetail";
//    }

    //api->view로 수정 특정 board 조회와 겹쳐서 에러 발생
    @GetMapping("/view/boards/{boardId}")

    public String boardPage(@PathVariable Long boardId, Model model) {
        // 일단 보드에 대한 정보가 필요함
        BoardResponseDto boardResponseDto = boardRepository.findById(boardId).map(BoardResponseDto::new).orElseThrow();
        model.addAttribute("board", boardResponseDto);
        // 그리고 컬럼에 대한 정보가 필요함

        List<BoardColumnResponseDto> boardColumnResponseDto = boardColumnRepository.findByBoardId(boardResponseDto.getBoardId())
                .stream().map(BoardColumnResponseDto::new).toList();

        for (BoardColumnResponseDto b : boardColumnResponseDto) {
            b.setCardsName(cardRepository.findByBoardColumn_IdOrderByCardOrder(b.getColumnId()).stream().map(CardInColumnResponseDto::new).toList());
            b.setBoardId(boardId);
        }

        for (BoardColumnResponseDto b : boardColumnResponseDto) {
            log.info("보드아이디: " + b.getBoardId() + " | 컬럼 아이디: " + b.getColumnId());
            for (CardInColumnResponseDto c : b.getCardsName()) {
                log.info("카드아이디: " + c.getCardId());
            }
        }

        model.addAttribute("columns", boardColumnResponseDto);

        return "boardDetail";
    }

}
