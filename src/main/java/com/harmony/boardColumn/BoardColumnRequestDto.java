package com.harmony.boardColumn;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class BoardColumnRequestDto {

    private Long boardId;

    private String boardColumnName;

    private Integer boardColumnOrder;
}
