package com.harmony.user;


import com.harmony.card.CardResponseDto;
import com.harmony.card.CardService;
import com.harmony.cardUser.CardUser;
import com.harmony.cardUser.CardUserService;
import com.harmony.comment.CommentResponseDto;
import com.harmony.comment.CommentService;
import com.harmony.security.JwtUtil;
import com.harmony.security.UserDetailsImpl;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLIntegrityConstraintViolationException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final CardUserService cardUserService;
    private final CommentService commentService;

    // 회원가입
    @ResponseBody
    @PostMapping("/signup")
    public ResponseEntity<String> signUp(@RequestBody @Validated SignupRequestDto signupRequestDto) {
        try {
            userService.signUp(signupRequestDto);
            return ResponseEntity.ok().body("회원가입 성공");
        } catch (SQLIntegrityConstraintViolationException ex) {
            return ResponseEntity.badRequest().body("{\"message\": \"" + ex.getMessage() + "\"}");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body("{\"message\": \"" + e.getMessage() + "\"}");
        }

    }

    // 로그인 페이지
    @GetMapping("/login-page")
    public String loginPage(HttpServletRequest request, HttpServletResponse response) {
        // 해당 쿠키 이름으로 생성된 쿠키를 찾아 제거
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (JwtUtil.BEARER_PREFIX.equals(cookie.getName())) {
                    cookie.setMaxAge(0); // 만료 시간을 0으로 설정하여 쿠키 제거
                    cookie.setPath("/"); // 도메인 전체에 걸쳐 쿠키를 삭제하도록 설정
                    response.addCookie(cookie);
                }
            }
        }
        return "loginsignup";
    }

    @ResponseBody
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
    @ResponseBody
    @PutMapping("/{userId}")
    public ResponseEntity<UserResponseDto> updateUser(@PathVariable Long userId,
                                                      @RequestBody UserUpdateRequestDto requestDto,
                                                      @AuthenticationPrincipal UserDetailsImpl userDetails) {
        User user = userService.findUserById(userId);
        return ResponseEntity.ok()
                .body(userService.updateUser(user, requestDto, userDetails.getUser()));
    }

    //유저 삭제(탈퇴)
    @ResponseBody
    @DeleteMapping("/{userId}")
    public ResponseEntity<String> deleteUser(@PathVariable Long userId,
                                             @AuthenticationPrincipal UserDetailsImpl userDetails) {
        User user = userService.findUserById(userId);
        userService.deleteUser(user, userDetails.getUser());
        return ResponseEntity.ok().body("유저 삭제 성공");
    }

    @GetMapping("/mypage")
    public String myPage(Model model, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        // userDetails 객체에서 현재 사용자의 정보를 가져와서 모델에 추가
        model.addAttribute("user", userDetails.getUser());

        List<CardResponseDto> cardResponseDtos = cardUserService.findCardId(userDetails.getUser().getId());

        model.addAttribute("cards", cardResponseDtos);

        List<CommentResponseDto> commentResponseDtos = commentService.findComments(userDetails.getUser().getId());

        model.addAttribute("comments", commentResponseDtos);

        return "mypage";
    }
}
