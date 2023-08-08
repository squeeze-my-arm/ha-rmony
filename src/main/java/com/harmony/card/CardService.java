package com.harmony.card;

import com.harmony.cardUser.CardUser;
import com.harmony.cardUser.CardUserRepository;
import com.harmony.user.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class CardService {
  private final CardRepository cardRepository;
  private final CardUserRepository cardUserRepository;

  //카드 생성
  public CardResponseDto createCard(String cardName,User user) {
    Card card=new Card(cardName);
    cardRepository.save(card);
    CardUser cardUser=new CardUser(card,user);
    cardUserRepository.save(cardUser);
    return new CardResponseDto(card);
  }

  public Card findCard(Long cardId) {
    return cardRepository.findById(cardId).orElseThrow(()-> new IllegalArgumentException("선택한 카드는 존재하지 않습니다."));
  }

  //카드 수정
  
  //카드 삭제
}
