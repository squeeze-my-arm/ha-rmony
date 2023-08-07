package com.harmony.board;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class BoardRequestDto {
    private String boardTitle;
    private String boardColor;
    private String boardDesc;
}
