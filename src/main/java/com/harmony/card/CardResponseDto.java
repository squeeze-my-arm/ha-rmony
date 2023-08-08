package com.harmony.card;

import com.harmony.cardUser.CardUser;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Set;
import lombok.Getter;

@Getter

public class CardResponseDto {
  private Long cardId;
  private String cardName;
  private String desc;
  private String color;
  private LocalDate deadline;
  private Long cardOrder;
  private LocalDateTime createAt;
  private Set<String> cardUsernames;

  public CardResponseDto(Card card){
    this.cardId=card.getId();
    this.cardName=card.getCardname();
    this.desc=card.getDescription();
    this.color=card.getColor();
    this.deadline=card.getDeadline();
    this.cardOrder=card.getCardOrder();
    this.createAt=card.getCreatedAt();
    //Collections.addAll(this.cardUsernames,card.getCardUsers().forEach(cardUser -> getU););
    if(card.getCardUsers()!=null){
      for(CardUser cu:card.getCardUsers()){
        this.cardUsernames.add(cu.getUser().getUsername());
      }
    }else{
      this.cardUsernames=new LinkedHashSet<>();
    }

  }
}
