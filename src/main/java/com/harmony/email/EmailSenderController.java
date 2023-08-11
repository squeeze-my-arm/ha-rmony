package com.harmony.email;

import com.harmony.security.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@Slf4j(topic = "이메일컨트롤러")
public class EmailSenderController {
    private final EmailSenderService emailSenderService;

    // User에게 초대링크 전송
    @PostMapping("/invited/{boardId}")
    public ResponseEntity<String> inviteUser(@PathVariable Long boardId,
                                             @RequestBody Map<String, String> invitedUser,
                                             @AuthenticationPrincipal UserDetailsImpl userDetails) throws Exception {
        log.info(invitedUser.get("username"));
        return emailSenderService.confirmBoardUser(boardId, invitedUser.get("username"), userDetails.getUser());
    }
}
