package com.harmony.card;

import java.time.LocalDate;
import java.util.*;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Builder;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CardRequestDto {

  private String cardName;
  private String desc;
  private String color;
  private String deadline;
  private Long cardOrder;

  @Builder
  public CardRequestDto(String cardName) {
    this.cardName = cardName;
  }

}
