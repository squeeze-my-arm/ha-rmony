package com.harmony.card;


import com.harmony.boardcolumn.BoardColumnRepository;
import com.harmony.common.ApiResponseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/api")
@RestController
@RequiredArgsConstructor
@Slf4j
public class CardController {

    private final CardService cardService;
    private final BoardColumnRepository boardColumnRepository;

    // 카드 상세 조회
    @GetMapping("/cards/{cardid}")
    public ResponseEntity<ApiResponseDto> getOneCard(@PathVariable Long cardid) {
        try {
            log.info("조회 시도");
            CardResponseDto cardResponseDto = cardService.getOneCard(cardid);
            log.info("조회 완료");
            return ResponseEntity.ok().body(cardResponseDto);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(new ApiResponseDto("카드가 존재하지 않습니다", HttpStatus.BAD_REQUEST.value()));
        }
    }

    // 카드 이동
    @PutMapping("/cards/{cardid}/orders")
    public ResponseEntity<ApiResponseDto> changeCardOrder(@PathVariable Long cardid, @RequestBody CardOrderRequestDto cardOrderRequestDto) {
        try {
            cardService.changeCardOrder(cardid, cardOrderRequestDto);
            return ResponseEntity.ok().body(new ApiResponseDto("이동 성공", HttpStatus.OK.value()));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(new ApiResponseDto("이동 실패", HttpStatus.BAD_REQUEST.value()));
        }
    }

}
