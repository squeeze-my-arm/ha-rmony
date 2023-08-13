package com.harmony.common;


import com.harmony.board.BoardRepository;
import com.harmony.board.BoardResponseDto;
import com.harmony.boardColumn.BoardColumnRepository;
import com.harmony.boardColumn.BoardColumnResponseDto;
import com.harmony.boardUser.BoardUser;
import com.harmony.boardUser.BoardUserRepository;
import com.harmony.boardUser.BoardUserResponseDto;
import com.harmony.card.*;
import com.harmony.cardUser.CardUser;
import com.harmony.cardUser.CardUserRepository;
import com.harmony.cardUser.CardUserResponseDto;
import com.harmony.comment.CommentRepository;
import com.harmony.comment.CommentResponseDto;
import com.harmony.security.JwtUtil;
import com.harmony.security.UserDetailsImpl;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Controller
@RequiredArgsConstructor
public class ViewController {
    private final BoardRepository boardRepository;
    private final BoardColumnRepository boardColumnRepository;
    private final CardRepository cardRepository;

    private final CardService cardService;
    private final CommentRepository commentRepository;
    private final BoardUserRepository boardUserRepository;
    private final CardUserRepository cardUserRepository;
    private final JwtUtil jwtUtil;

    @GetMapping("/")
    public String mainPage() {
        return "index";
    }

    @GetMapping("/api/users/login-page")
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

    @GetMapping("/api/boards/{boardid}")
    public String boardPage(@PathVariable Long boardid, Model model) {
        // 일단 보드에 대한 정보가 필요함
        BoardResponseDto boardResponseDto = boardRepository.findById(boardid).map(BoardResponseDto::new).orElseThrow();
        model.addAttribute("board", boardResponseDto);

        // 해당 보드에 참여하고 있는 사용자에 대한 정보
        List<BoardUserResponseDto> boardUsers = boardUserRepository.findByBoard_Id(boardResponseDto.getBoardId()).stream().map(BoardUserResponseDto::new).toList();
        model.addAttribute("boardUsers", boardUsers);

        // 그리고 컬럼에 대한 정보가 필요함
        List<BoardColumnResponseDto> boardColumnResponseDto = boardColumnRepository.findAllByBoardIdOrderByBoardColumnOrder(boardResponseDto.getBoardId())

                .stream().map(BoardColumnResponseDto::new).toList();

        for (BoardColumnResponseDto b : boardColumnResponseDto) {
            b.setCardsName(cardRepository.findByBoardColumn_IdOrderByCardOrder(b.getColumnId()).stream().map(CardInColumnResponseDto::new).toList());
            b.setBoardId(boardid);
        }

        for (BoardColumnResponseDto b : boardColumnResponseDto) {
            log.info("보드아이디: " + b.getBoardId() + " | 컬럼 아이디: " + b.getColumnId());
            for (CardInColumnResponseDto c : b.getCardsName()) {
                log.info("카드아이디: " + c.getCardId());
            }
        }

        model.addAttribute("columns", boardColumnResponseDto);
        return "board";
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

    @GetMapping("/api/users/mypage")
    public String myPage(Model model, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        // userDetails 객체에서 현재 사용자의 정보를 가져와서 모델에 추가
        model.addAttribute("user", userDetails.getUser());

        List<CardUser> cardUsers = cardUserRepository.findByUserId(userDetails.getUser().getId());

        List<Long> cardIds = cardUsers.stream()
                .map(CardUser::getCard)
                .map(Card::getId)
                .collect(Collectors.toList());

        List<CardResponseDto> cardResponseDtos = new ArrayList<>();

        for (Long id: cardIds) {
            cardResponseDtos.add(cardRepository.findById(id).map(CardResponseDto::new).orElseThrow());
        }

        model.addAttribute("cards", cardResponseDtos);

        List<CommentResponseDto> commentResponseDtos = commentRepository.findByUserId(userDetails.getUser().getId()).stream().map(CommentResponseDto::new).toList();

        model.addAttribute("comments", commentResponseDtos);
        // 카드 유저를 찾았으면 ? 그 카드 유저의 카드 아이디로 카드 레파지토리에서 찾아와 ?

//        String userId = String.valueOf(userDetails.getUser().getId()); // userId를 문자열로 변환
//        model.addAttribute("userId", userId);
//        log.info(userId);
        return "mypage"; // This should match the name of your HTML file without the .html extension
    }
//    @GetMapping("/api/users/mypage")
//    public String myPage(Model model) {
//        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
//        String userId = userDetails.getUsername();
//        model.addAttribute("userId", userId);
//        return "mypage";
//    }
}
