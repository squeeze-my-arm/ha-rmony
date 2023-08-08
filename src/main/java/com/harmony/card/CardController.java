package com.harmony.card;

import com.harmony.security.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api")
public class CardController {
  private final CardService cardService;

  //카드 생성 body 에 ""없이 그냥 cardName 에 들어갈 값 입력하면 된다.
  @PostMapping("/cards")
  public ResponseEntity<CardResponseDto> createCard(@RequestBody String cardName,@AuthenticationPrincipal
      UserDetailsImpl userDetails){
    CardResponseDto result=cardService.createCard(cardName,userDetails.getUser());
        return ResponseEntity.status(HttpStatus.CREATED).body(result);
  }

  //카드 수정 ->
  @PutMapping("cards/{cardId}")
  public ResponseEntity<CardResponseDto> updateCard(@PathVariable Long columnId, @PathVariable Long cardId, @RequestBody CardRequestDto requestDto,@AuthenticationPrincipal UserDetailsImpl userDetails){
    Card card = cardService.findCard(cardId);
    CardResponseDto result= cardService.updateCard(card,CardRequestDto,userDetails.getUser())
    return ResponseEntity.ok(result);
  }

  //카드 삭제
}
