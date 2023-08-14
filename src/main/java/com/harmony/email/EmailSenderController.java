package com.harmony.email;

import com.harmony.security.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class EmailSenderController {
    private final EmailSenderService emailSenderService;

    // User에게 초대링크 전송
    @PostMapping("/invited/{boardId}")
    public ResponseEntity<String> inviteUser(@PathVariable Long boardId,
                                             @RequestBody Map<String, String> invitedUser,
                                             @AuthenticationPrincipal UserDetailsImpl userDetails) throws Exception {
        return emailSenderService.confirmBoardUser(boardId, invitedUser.get("username"), userDetails.getUser());
    }
}
