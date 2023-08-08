package com.harmony.card;

import com.harmony.aop.BoardUserCheck;
import com.harmony.cardUser.CardUserRepository;
import com.harmony.column.ColumnRepository;
import com.harmony.column.Columns;
import com.harmony.user.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@RequiredArgsConstructor
@Service
public class CardService {

  private final CardRepository cardRepository;
  private final CardUserRepository cardUserRepository;
  private final ColumnRepository columnRepository;

  //카드 생성
  //목록에 추가하고 그 index 에 따른 값 등록
  @BoardUserCheck
  @Transactional
  public CardResponseDto createCard(Long boardId, Long columnId, String cardName, User user) {
    Columns column = columnRepository.findById(columnId)
        .orElseThrow(() -> new IllegalArgumentException("컬럼이 존재하지 않습니다."));
    Card card = Card.builder().cardname(cardName).column(column).color("BLACK").build();

    cardRepository.save(card);

    column.addCard(card);
    log.info("card 생성 성공");
    return new CardResponseDto(card);
  }

  //    CardUser cardUser = new CardUser(card, user);
//
//    card.addCardUser(cardUser);
  //cardUserRepository.save(cardUser);
  //카드 수정
  @BoardUserCheck
  @Transactional
  public CardResponseDto updateCard(Long boardId, Long cardId, CardRequestDto requestDto,
      User user) {
    Card card = findCard(cardId);
    card.updateCard(requestDto);
    return new CardResponseDto(card);

  }

  //카드 삭제
  @BoardUserCheck
  @Transactional
  public void deleteCard(Long boardId, Long cardId, User user) {
    cardRepository.delete(findCard(cardId));
  }

  private Card findCard(Long cardId) {
    return cardRepository.findById(cardId)
        .orElseThrow(() -> new IllegalArgumentException("선택한 카드는 존재하지 않습니다."));
  }
}
