package com.harmony.common;


import com.harmony.board.Board;
import com.harmony.board.BoardRepository;
import com.harmony.boardUser.BoardUser;
import com.harmony.boardUser.BoardUserRepository;
import com.harmony.card.Card;
import com.harmony.card.CardResponseDto;
import com.harmony.card.CardService;
import com.harmony.cardUser.CardUser;
import com.harmony.cardUser.CardUserRepository;
import com.harmony.cardUser.CardUserResponseDto;
import com.harmony.comment.CommentRepository;
import com.harmony.comment.CommentResponseDto;
import com.harmony.user.UserResponseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@Slf4j
@Controller
@RequiredArgsConstructor
public class ViewController {

    private final CardService cardService;
    private final CommentRepository commentRepository;
    private final BoardUserRepository boardUserRepository;
    private final BoardRepository boardRepository;
    private final CardUserRepository cardUserRepository;

    @GetMapping("/")
    public String mainPage() {
        return "index";
    }

    @GetMapping("/api/users/login-page")
    public String loginPage() {
        return "loginsignup";
    }
    
    @GetMapping("/api/cards/{cardid}")
    public String cardPage(@PathVariable Long cardid, Model model) {
        // 일단 해당 카드에 대한 정보를 찾자
        Card card = cardService.findCard(cardid);
        // CardResponseDto에 담음
        CardResponseDto cardResponseDto = new CardResponseDto(card);
        // model 객체에 card 이름으로 담아서 반환
        model.addAttribute("card", cardResponseDto);

        // ++ 추가하기
        // card에 있는 user 목록들 + 리스트에 보여줄 board user 목록들
        List<BoardUser> boardUser = boardUserRepository.findByBoard_Id(card.getBoardColumn().getBoard().getId()).stream().toList();
        List<CardUser> cardUsers = cardUserRepository.findByCard_Id(card.getId()).stream().toList();

        List<CardUserResponseDto> boardusers = cardService.findUsers(boardUser, cardUsers);
        log.info("username: " + boardUser.get(0).getUsername());
        model.addAttribute("boardusers", boardusers);

        // card에 있는 댓글 정보도 같이 가져가자
        List<CommentResponseDto> commentResponseDtoList = commentRepository.findAllByCard(card).stream().map(CommentResponseDto::new).toList();
        model.addAttribute("comments", commentResponseDtoList);

        return "card";
    }

}
