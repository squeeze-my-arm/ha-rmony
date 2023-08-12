package com.harmony.social;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.harmony.user.User;
import com.harmony.user.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class GoogleService {

    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final RestTemplate restTemplate;

    @Value("${spring.security.oauth2.client.registration.google.client-id}")
    private String clientId;

    @Value("${spring.security.oauth2.client.registration.google.client-secret}")
    private String secretId;

    public String googleLogin(String code) throws JsonProcessingException {
        // 1. "인가 코드"로 "액세스 토큰" 요청
        String accessToken = getToken(code);

        log.info("googleLogin accessToken: " + accessToken);

        // 2. 토큰으로 구글 API 호출 : "액세스 토큰"으로 "구글 사용자 정보" 가져오기
        GoogleUserInfo googleUserInfo = getUserInfo(accessToken);

        // 3. 필요시에 회원가입
        User googleUser = registerGoogleUserIfNeeded(googleUserInfo);

        // 4. username 반환
        return googleUser.getUsername();
    }

    // 애플리케이션은 인증 코드로 구글 서버에 토큰을 요청하고, 토큰을 전달 받습니다.
    // 1) 액세스 토큰 요청 메서드
    public String getToken(String code) throws JsonProcessingException {
        log.info("getToken code: " + code);
        // 요청 URL 만들기
        URI uri = UriComponentsBuilder
                .fromUriString("https://oauth2.googleapis.com")
                .path("/token")
                .encode()
                .build()
                .toUri();

        // HTTP Header 생성
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-type", "application/x-www-form-urlencoded");

        // HTTP Body 생성
        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("grant_type", "authorization_code");
        body.add("client_id", clientId);
        body.add("client_secret", secretId);
        body.add("redirect_uri", "http://localhost:8080/api/users/google/callback"); // 애플리케이션 등록시 설정한 redirect_uri
        body.add("code", code); // 인가 코드

        RequestEntity<MultiValueMap<String, String>> requestEntity = RequestEntity
                .post(uri) // body 가 있으므로 post 메서드
                .headers(headers)
                .body(body);

        // HTTP 요청 보내기
        ResponseEntity<String> response = restTemplate.exchange(
                requestEntity,
                String.class // 반환값 타입은 String
        );

        // HTTP 응답 (JSON) -> 액세스 토큰 값을 반환합니다.
        JsonNode jsonNode = new ObjectMapper().readTree(response.getBody());
        return jsonNode.get("access_token").asText();
    }

    // 2) 토큰에서 사용자 정보 가져오기
    public GoogleUserInfo getUserInfo(String accessToken) {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(accessToken);

        HttpEntity<?> entity = new HttpEntity<>(headers);

        ResponseEntity<GoogleUserInfo> response = restTemplate.exchange(
                "https://www.googleapis.com/oauth2/v2/userinfo",
                HttpMethod.GET,
                entity,
                GoogleUserInfo.class
        );

        GoogleUserInfo userInfo = null;

        if (response.getStatusCode() == HttpStatus.OK) {
            userInfo = response.getBody();

            // 만약 이름이 null인 경우 랜덤 동물 이름 선택
            if (userInfo.getName() == null) {
                userInfo.setName(getRandomAnimalName());
            }

            System.out.println("User ID: " + userInfo.getId());
            System.out.println("Email: " + userInfo.getEmail());

        } else {
            System.out.println("Failed to fetch user info");
        }

        return userInfo;
    }

    // 3) 구글 ID 정보로 회원가입
    private User registerGoogleUserIfNeeded(GoogleUserInfo googleUserInfo) {
        // DB 에 중복된 Google Id 가 있는지 확인
        String googleUserInfoId = googleUserInfo.getId();
        // 중복 확인
        User googleuser = userRepository.findByGoogleId(googleUserInfoId).orElse(null);

        if (googleuser == null) {
            // 구글 사용자 email 동일한 email 가진 회원이 있는지 확인
            String googleEmail = googleUserInfo.getEmail();  // email == username
            User sameEmailuser = userRepository.findByUsername(googleEmail).orElse(null);

            if (sameEmailuser != null) {
                googleuser = sameEmailuser;
                // 기존 회원정보에 구글 Id 추가
                googleuser = googleuser.googleIdupdate(googleUserInfoId);
            } else {
                // 신규 회원가입
                // password: random UUID
                String password = UUID.randomUUID().toString();
                String encodedPassword = passwordEncoder.encode(password);

                // email: google email
                String username = googleUserInfo.getEmail();

                googleuser = new User(username, encodedPassword, googleUserInfo.getName(), googleUserInfoId);
            }

            userRepository.save(googleuser);
        }

        return googleuser;
    }

    private List<String> animalNames = new ArrayList<String>() {{
        add("사자");
        add("호랑이");
        add("곰");
        add("토끼");
        add("고양이");
        add("강아지");
        add("여우");
    }};

    private String getRandomAnimalName() {
        // 리스트에서 랜덤한 동물 이름 선택
        int randomIndex = new Random().nextInt(animalNames.size());
        return animalNames.get(randomIndex);
    }
}
