package com.harmony.boardUser;

import com.harmony.security.UserDetailsServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@Slf4j(topic = "보드 유저")
public class BoardUserController {

    private final BoardUserService boardUserService;
    private final UserDetailsServiceImpl userDetailsService;

    // 초대 링크 클릭시 실행되는 controller
    @PostMapping("/boardUser/join/{boardId}")
    public ResponseEntity<?> joinUserBoard(@PathVariable Long boardId,
                                           @RequestBody Map<String, String> invitedUser) {
        String invitedUsername = invitedUser.get("username");
        boardUserService.joinUserBoard(boardId, invitedUsername);

        if (SecurityContextHolder.getContext().getAuthentication().getPrincipal() instanceof UserDetails) {
            UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            if (userDetails.getUsername().equals(invitedUsername)) {
                log.info("참여 완료되었습니다!");
                // 해당 보드 페이지로 넘어가도록 할 예정
                return ResponseEntity.ok().body("참여 완료");
            }
        }
        /*            // 로그인된 사용자의 username과 초대된 사용자의 username이 다를 경우 -> 로그인 페이지로
            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.setLocation(URI.create("/api/users/login"));
            return new ResponseEntity<>(httpHeaders, HttpStatus.MOVED_PERMANENTLY);*/
        log.info("로그인이 필요합니다!");
        return ResponseEntity.ok().body("로그인필요함ㄴ");
    }
}
