package com.harmony.user;


import com.harmony.security.UserDetailsImpl;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.sql.SQLIntegrityConstraintViolationException;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

  private final UserService userService;

    // 회원가입
    @PostMapping("/signup")
    public ResponseEntity<String> signUp(@RequestBody SignupRequestDto signupRequestDto) {
        try {
            userService.signUp(signupRequestDto);
            return ResponseEntity.ok().body("회원가입 성공");
        } catch (SQLIntegrityConstraintViolationException ex) {
            return ResponseEntity.badRequest().body("{\"message\": \"" + ex.getMessage() + "\"}");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body("{\"message\": \"" + e.getMessage() + "\"}");
        }

    }

    // 로그인
    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody LoginRequestDto loginRequestDto, HttpServletResponse res) {
        userService.login(loginRequestDto, res);
        try {
            return ResponseEntity.ok().body("로그인 성공");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

  // 로그아웃

  //유저 수정
  @PutMapping("/{userId}")
  public ResponseEntity<UserResponseDto> updateUser(@PathVariable Long userId,
      @RequestBody UserUpdateRequestDto requestDto,
      @AuthenticationPrincipal UserDetailsImpl userDetails) {
    return ResponseEntity.ok()
        .body(userService.updateUser(userId, requestDto, userDetails.getUser()));
  }

  //유저 삭제(탈퇴)
  @DeleteMapping("/{userId}")
  public ResponseEntity<String> deleteUser(@PathVariable Long userId,
      @AuthenticationPrincipal UserDetailsImpl userDetails) {
    userService.deleteUser(userId, userDetails.getUser());
    return ResponseEntity.ok().body("유저 삭제 성공");
  }
}
