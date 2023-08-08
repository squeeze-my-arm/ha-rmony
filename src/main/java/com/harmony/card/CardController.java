package com.harmony.card;

import com.harmony.security.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api")
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

    CardResponseDto result = cardService.updateCard(boardId, cardId, requestDto,
        userDetails.getUser());
    return ResponseEntity.ok(result);
  }

  //카드 삭제
  @DeleteMapping("boards/{boardId}/columns/cards/{cardId}")
  public ResponseEntity<String> deleteCard(@PathVariable Long boardId, @PathVariable Long cardId,
      @AuthenticationPrincipal UserDetailsImpl userDetails) {
    cardService.deleteCard(boardId, cardId, userDetails.getUser());
    return ResponseEntity.ok("card 삭제 성공");
  }

  //카드 유저 등록

  //카드 유저 삭제


}
