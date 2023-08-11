package com.harmony.cardUser;

import com.harmony.boardUser.BoardUser;
import lombok.Getter;

import java.util.List;

@Getter
public class CardUserResponseDto {

    private String username;
    private String nickname;
    private Boolean selected;

    public CardUserResponseDto (BoardUser boardUser, Boolean selected) {
        this.username = boardUser.getUsername();
        this.nickname = boardUser.getNickname();
        this.selected = selected;

    }

}
