package com.harmony.card;


import com.harmony.common.ApiResponseDto;
import com.harmony.security.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;


@RequestMapping("/api")
@RestController
@RequiredArgsConstructor
@Slf4j
public class CardController {
    private final CardService cardService;

    //카드 생성 body 에 ""없이 그냥 cardName 에 들어갈 값 입력하면 된다.
    @PostMapping("/boards/{boardId}/columns/{columnId}/cards")
    public ResponseEntity<CardResponseDto> createCard(@PathVariable Long boardId,
                                                      @PathVariable Long columnId, @RequestBody String cardName,
                                                      @AuthenticationPrincipal UserDetailsImpl userDetails) {
        CardResponseDto result = cardService.createCard(boardId, columnId, cardName,
                userDetails.getUser());
        return ResponseEntity.status(HttpStatus.CREATED).body(result);
    }

    //카드 수정
    @PatchMapping("/boards/{boardId}/columns/cards/{cardId}")
    public ResponseEntity<CardResponseDto> updateCard(@PathVariable Long boardId,
                                                      @PathVariable Long cardId,
                                                      @RequestBody CardRequestDto requestDto,
                                                      @AuthenticationPrincipal UserDetailsImpl userDetails) {
        log.info(requestDto.getDeadline());
        CardResponseDto result = cardService.updateCard(boardId, cardId, requestDto,
                userDetails.getUser());
        return ResponseEntity.ok(result);
    }

    //카드 유저 수정
    @PatchMapping("/boards/{boardId}/cards/{cardId}")
    public ResponseEntity<CardResponseDto> updateCard(@PathVariable Long boardId,
                                                      @PathVariable Long cardId,
                                                      @RequestBody CardRequestUserDto requestDto,
                                                      @AuthenticationPrincipal UserDetailsImpl userDetails) {
        CardResponseDto result = cardService.updateCardUser(boardId, cardId, requestDto,
                userDetails.getUser());
        return ResponseEntity.ok(result);
    }

    //카드 삭제
    @DeleteMapping("/boards/{boardId}/columns/cards/{cardId}")
    public ResponseEntity<String> deleteCard(@PathVariable Long boardId, @PathVariable Long cardId,
                                             @AuthenticationPrincipal UserDetailsImpl userDetails) {
        cardService.deleteCard(boardId, cardId, userDetails.getUser());
        return ResponseEntity.ok("card 삭제 성공");
    }

    // 카드 상세 조회
//    @GetMapping("/cards/{cardid}")
//    public ResponseEntity<ApiResponseDto> getOneCard(@PathVariable Long cardid) {
//        try {
//            log.info("조회 시도");
//            CardResponseDto cardResponseDto = cardService.getOneCard(cardid);
//            log.info("조회 완료");
//            return ResponseEntity.ok().body(cardResponseDto);
//        } catch (IllegalArgumentException e) {
//            return ResponseEntity.badRequest().body(new ApiResponseDto("카드가 존재하지 않습니다", HttpStatus.BAD_REQUEST.value()));
//        }
//    }

    // 카드 이동
    @PutMapping("/cards/{cardid}/orders")
    public ResponseEntity<ApiResponseDto> changeCardOrder(@PathVariable Long cardid, @RequestBody CardOrderRequestDto cardOrderRequestDto) {
        log.info("이동 시도");
        try {
            cardService.changeCardOrder(cardid, cardOrderRequestDto);
            return ResponseEntity.ok().body(new ApiResponseDto("이동 성공", HttpStatus.OK.value()));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(new ApiResponseDto("이동 실패", HttpStatus.BAD_REQUEST.value()));
        }
    }



}
