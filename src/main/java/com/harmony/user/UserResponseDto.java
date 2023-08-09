package com.harmony.user;

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
}
