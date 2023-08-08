package com.harmony.user;


import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLIntegrityConstraintViolationException;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    // 회원가입
    @PostMapping("/users/signup")
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
    @PostMapping("/users/login")
    public ResponseEntity<String> login(@RequestBody LoginRequestDto loginRequestDto, HttpServletResponse res) {
        userService.login(loginRequestDto, res);
        try {
            return ResponseEntity.ok().body("로그인 성공");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // 로그아웃
}
