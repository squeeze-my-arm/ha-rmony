package com.harmony.card;

import java.time.LocalDate;
import java.util.LinkedHashSet;
import java.util.Set;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.LinkedHashSet;
import java.util.Set;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CardRequestDto {

  private String cardName;
  private String desc;
  private String color;
  private LocalDate deadline;
  private Long cardOrder;
  private Set<String> cardUserNames = new LinkedHashSet<>();

}
