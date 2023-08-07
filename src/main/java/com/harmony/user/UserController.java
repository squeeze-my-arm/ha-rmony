package com.harmony.user;


import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    // 회원가입
    @PostMapping("/users/signup")
    public ResponseEntity<String> signUp(@RequestBody SignupRequestDto signupRequestDto) {
        userService.signUp(signupRequestDto);
        return ResponseEntity.ok().body("회원가입 성공");
    }

    // 로그인
    @PostMapping("/users/login")
    public ResponseEntity<String> login(@RequestBody LoginRequestDto loginRequestDto, HttpServletResponse res) {
        userService.login(loginRequestDto, res);
        return ResponseEntity.ok().body("로그인 성공");
    }

    // 로그아웃
}
