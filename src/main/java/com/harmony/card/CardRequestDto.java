package com.harmony.card;

import java.time.LocalDate;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CardRequestDto {
  private String cardName;
  private String desc;
  private String color;
  private LocalDate deadline;
  private Long cardOrder;

}
