package com.harmony.card;


import com.google.protobuf.Api;
import com.harmony.common.ApiResponseDto;
import com.harmony.security.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/api")
@RestController
@RequiredArgsConstructor
@Slf4j
public class CardController {

    private final CardService cardService;

    // 카드 상세 조회
    @GetMapping("/cards/{cardid}")
    public ResponseEntity<ApiResponseDto> getOneCard(@PathVariable Long cardid) {
        try {
            CardResponseDto cardResponseDto = cardService.getOneCard(cardid);
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
        } catch (IllegalArgumentException e) {

        }
    }

}
