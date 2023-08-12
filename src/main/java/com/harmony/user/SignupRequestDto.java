package com.harmony.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

// lombok
@Getter
public class SignupRequestDto {

    @Email(message = "이메일 형식에 어긋났습니다.")
    @NotBlank(message = "username(email)을 필수입력사항입니다.")
    private String username;
    @NotBlank(message = "Password 는 필수입력사항입니다.")
    private String password;
    @NotBlank(message = "nickname 은 필수입력사항입니다.")
    private String nickname;
}
