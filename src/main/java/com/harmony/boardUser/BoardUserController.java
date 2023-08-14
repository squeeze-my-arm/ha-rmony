package com.harmony.boardUser;

import com.harmony.security.UserDetailsImpl;
import com.harmony.security.UserDetailsServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/api")
@RequiredArgsConstructor
public class BoardUserController {

    private final BoardUserService boardUserService;
    private final UserDetailsServiceImpl userDetailsService;

    // 초대 링크 클릭시 실행되는 controller
    @GetMapping("/boardUser/join/{boardId}")
    public String joinUserBoard(@PathVariable Long boardId, @RequestParam String to) {
        return boardUserService.joinUserBoard(boardId, to);
    }

    @ResponseBody
    @DeleteMapping("/boardUser/{boardId}")
    public ResponseEntity<String> deleteBoardUser(@PathVariable Long boardId, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return boardUserService.deleteBoardUser(boardId, userDetails.getUser());
    }
}
