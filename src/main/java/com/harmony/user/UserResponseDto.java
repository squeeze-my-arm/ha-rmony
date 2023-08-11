package com.harmony.user;

import com.harmony.boardUser.BoardUser;
import lombok.Getter;

@Getter
public class UserResponseDto {

  private Long userId;
  private String username;
  private String nickname;
  private String introduction;


  public UserResponseDto(Long userId, String username, String nickname, String introduction) {
    this.userId = userId;
    this.username = username;
    this.nickname = nickname;
    this.introduction = introduction;
  }

  public UserResponseDto(BoardUser boardUser) {
    this.userId = boardUser.getUser().getId();
    this.username = boardUser.getUser().getUsername();
    this.nickname = boardUser.getUser().getNickname();
    this.introduction = boardUser.getUser().getIntroduction();
  }

}
