package com.harmony.boardUser;

import lombok.Getter;

@Getter
public class BoardUserResponseDto {

    private String username;

    public BoardUserResponseDto(BoardUser boardUser) {
        this.username = boardUser.getUsername();
    }
}