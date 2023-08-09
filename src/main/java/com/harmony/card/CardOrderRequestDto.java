package com.harmony.card;

import lombok.Getter;

@Getter
public class CardOrderRequestDto {

    // 이동하는 컬럼의 id 값
    private Long columnId;
    // 이동하는 카드의 순서 값
    private Integer cardOrder;
}
