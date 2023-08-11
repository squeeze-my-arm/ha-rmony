package com.harmony.social;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.harmony.security.JwtUtil;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.yaml.snakeyaml.util.UriEncoder;

import java.io.UnsupportedEncodingException;

@Slf4j
@Controller
@RequiredArgsConstructor
public class SocialController {

    private final GoogleService googleService;
    private final JwtUtil jwtUtil;

    @GetMapping("/api/users/google/callback")
    public String googleLogin(@RequestParam String code, HttpServletResponse response) throws JsonProcessingException {
        String username = googleService.googleLogin(code); // 반환 값이 JWT 토큰

        String token = jwtUtil.createToken(username);
        jwtUtil.addJwtToCookie(token, response);
        log.info(token);

        return "redirect:/";
    }

}
