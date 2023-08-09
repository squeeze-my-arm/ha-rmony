package com.harmony.email;

import com.harmony.board.Board;
import com.harmony.board.BoardService;
import com.harmony.boardUser.BoardUser;
import com.harmony.boardUser.BoardUserRepository;
import com.harmony.user.User;
import com.harmony.user.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.spring6.SpringTemplateEngine;

@Service
@Slf4j(topic = "이메일 전송")
@RequiredArgsConstructor
public class EmailSenderService {

    private final JavaMailSender javaMailSender;
    private final SpringTemplateEngine templateEngine;
    private final UserService userService;
    private final BoardService boardService;
    private final BoardUserRepository boardUserRepository;

    public void confirmBoardUser(Long boardId, String invitedUserName, User user) throws Exception {
        User inviteUser = userService.findUser(invitedUserName); // 초대받은 유저
        log.info(invitedUserName);
        Board board = boardService.findBoard(boardId);
        try {
            BoardUser boardUser = findBoardUser(inviteUser, board);
            // 이미 초대된 사용자가 있을 때
            // sendEmail 로직을 진행하지 않음
            log.info("이미 초대된 사용자입니다.");
        } catch (IllegalArgumentException ex) {
            // 초대된 사용자가 없을 때
            sendEmail(board, invitedUserName, user);
        }
    }

    @Async
    public void sendEmail(Board board, String invitedUserName, User ownUser) throws Exception {
        EmailSender emailSender = EmailSender.builder()
                .to(invitedUserName)
                .subject(ownUser.getNickname() + "님이 " + board.getBoardTitle() + "에 초대하셨습니다.")
                .build();
        MimeMessagePreparator messagePreparator = mimeMessage -> {
            MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage, true, "UTF-8");
            messageHelper.setTo(emailSender.getTo());
            messageHelper.setSubject(emailSender.getSubject());
            messageHelper.setText(setContext(emailSender.getTo(), ownUser.getUsername(), board), true);
        };

        try {
            javaMailSender.send(messagePreparator);
            log.info("활성화 메일이 보내졌다");
        } catch (MailException e) {
            e.printStackTrace();
            throw new Exception("메일을 여기로 보내는 중 에러 발생");
        }
    }

    public String setContext(String to, String from, Board board) {
        String msg = "";
        msg += "<h1 style=\"font-size: 30px; padding-right: 30px; padding-left: 30px;\">Ha-rmony</h1>";
        msg += "<p style=\"font-size: 17px; padding-right: 30px; padding-left: 30px;\">";
        msg += from + "으로부터 " + board.getBoardTitle() + "에 초대되었습니다:)</p>";
        msg += "<div style=\"padding-right: 30px; padding-left: 30px; margin: 32px 0 40px;\"><table style=\"border-collapse: collapse; border: 0; background-color: #F4F4F4; height: 70px; table-layout: fixed; word-wrap: break-word; border-radius: 6px;\"><tbody><tr><td style=\"text-align: center; vertical-align: middle; font-size: 30px;\">";
        msg += "<a href=\"http://localhost8080/api/boardUser/join/" + board.getId() + "\">" + "Board에 참여하기" + "</a>";
        msg += "</td></tr></tbody></table></div>";
        return msg;
    }

    public BoardUser findBoardUser(User user, Board board) throws IllegalArgumentException {
        return boardUserRepository.findByUserAndBoard(user, board).orElseThrow(() ->
                new IllegalArgumentException("등록되어 있지 않은 사용자 입니다")
        );
    }

}
