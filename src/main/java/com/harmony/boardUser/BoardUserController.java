package com.harmony.boardUser;

import com.harmony.security.UserDetailsImpl;
import com.harmony.security.UserDetailsServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/api")
@RequiredArgsConstructor
@Slf4j(topic = "보드 유저")
public class BoardUserController {

    private final BoardUserService boardUserService;
    private final UserDetailsServiceImpl userDetailsService;

    // 초대 링크 클릭시 실행되는 controller
    @GetMapping("/boardUser/join/{boardId}")
    public String joinUserBoard(@PathVariable Long boardId,
                                @RequestParam String to) {
        log.info(to);
        boardUserService.joinUserBoard(boardId, to);

        if (SecurityContextHolder.getContext().getAuthentication().getPrincipal() instanceof UserDetails) {
            UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            if (userDetails.getUsername().equals(to)) {
                log.info("참여 완료되었습니다!");
                // 해당 보드 페이지로 넘어가도록 할 예정
                return "redirect:/api/boards/" + boardId;
            }
        }

        log.info("로그인이 필요합니다!");
        return "loginsignup";
    }

    @ResponseBody
    @DeleteMapping("/boardUser/{boardId}")
    public ResponseEntity<String> deleteBoardUser(@PathVariable Long boardId, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        boardUserService.deleteBoardUser(boardId, userDetails.getUser());
        return ResponseEntity.ok().body("board 삭제 성공");
    }
}
