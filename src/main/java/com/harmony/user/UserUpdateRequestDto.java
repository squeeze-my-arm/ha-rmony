package com.harmony.user;

import lombok.Getter;

@Getter
public class UserUpdateRequestDto {

  private String username;
  private String password;
  private String nickname;
  private String introduction;
}
