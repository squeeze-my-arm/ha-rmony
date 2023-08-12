package com.harmony.card;


import com.harmony.boardUser.BoardUser;
import com.harmony.boardUser.BoardUserService;
import com.harmony.cardUser.CardUser;
import com.harmony.cardUser.CardUserResponseDto;
import com.harmony.cardUser.CardUserService;
import com.harmony.comment.CommentResponseDto;
import com.harmony.comment.CommentService;
import com.harmony.common.ApiResponseDto;
import com.harmony.security.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RequestMapping("/api")
@Controller
@RequiredArgsConstructor
@Slf4j
public class CardController {
    private final CardService cardService;
    private final BoardUserService boardUserService;
    private final CardUserService cardUserService;
    private final CommentService commentService;

    @ResponseBody
    @PostMapping("/boards/{boardId}/columns/{columnId}/cards")
    public ResponseEntity<CardResponseDto> createCard(@PathVariable Long boardId,
                                                      @PathVariable Long columnId, @RequestBody String cardName,
                                                      @AuthenticationPrincipal UserDetailsImpl userDetails) {
        CardResponseDto result = cardService.createCard(boardId, columnId, cardName,
                userDetails.getUser());
        return ResponseEntity.status(HttpStatus.CREATED).body(result);
    }

    //카드 수정
    @ResponseBody
    @PatchMapping("/boards/{boardId}/columns/cards/{cardId}")
    public ResponseEntity<CardResponseDto> updateCard(@PathVariable Long boardId,
                                                      @PathVariable Long cardId,
                                                      @RequestBody CardRequestDto requestDto,
                                                      @AuthenticationPrincipal UserDetailsImpl userDetails) {
        log.info(requestDto.getDeadline());
        CardResponseDto result = cardService.updateCard(boardId, cardId, requestDto,
                userDetails.getUser());
        return ResponseEntity.ok(result);
    }

    //카드 유저 수정
    @ResponseBody
    @PatchMapping("/boards/{boardId}/cards/{cardId}")
    public ResponseEntity<CardResponseDto> updateCard(@PathVariable Long boardId,
                                                      @PathVariable Long cardId,
                                                      @RequestBody CardRequestUserDto requestDto,
                                                      @AuthenticationPrincipal UserDetailsImpl userDetails) {
        CardResponseDto result = cardUserService.updateCardUser(boardId, cardId, requestDto,
                userDetails.getUser());
        return ResponseEntity.ok(result);
    }

    //카드 삭제
    @ResponseBody
    @DeleteMapping("/boards/{boardId}/columns/cards/{cardId}")
    public ResponseEntity<String> deleteCard(@PathVariable Long boardId, @PathVariable Long cardId,
                                             @AuthenticationPrincipal UserDetailsImpl userDetails) {
        cardService.deleteCard(boardId, cardId, userDetails.getUser());
        return ResponseEntity.ok("card 삭제 성공");
    }

    // 카드 이동
    @ResponseBody
    @PutMapping("/cards/{cardId}/orders")
    public ResponseEntity<ApiResponseDto> changeCardOrder(@PathVariable Long cardId, @RequestBody CardOrderRequestDto cardOrderRequestDto) {
        log.info("이동 시도");
        try {
            cardService.changeCardOrder(cardId, cardOrderRequestDto);
            return ResponseEntity.ok().body(new ApiResponseDto("이동 성공", HttpStatus.OK.value()));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(new ApiResponseDto("이동 실패", HttpStatus.BAD_REQUEST.value()));
        }
    }

    //카드 상세 페이지
    @GetMapping("/cards/{cardId}")
    public String cardPage(@PathVariable Long cardId, Model model) {
        // 일단 해당 카드에 대한 정보를 찾자
        Card card = cardService.findCard(cardId);
        // CardResponseDto에 담음
        CardResponseDto cardResponseDto = new CardResponseDto(card);
        // model 객체에 card 이름으로 담아서 반환
        model.addAttribute("card", cardResponseDto);

        // card에 있는 user 목록들 + 리스트에 보여줄 board user 목록들
        List<BoardUser> boardUser = boardUserService.findBoardUserByBoardId(card.getBoardColumn().getBoard().getId());
        List<CardUser> cardUsers = cardUserService.findCardUserByCardId(card.getId());

        List<CardUserResponseDto> boardusers = cardUserService.findUsers(boardUser, cardUsers);
        log.info("username: " + boardUser.get(0).getUsername());
        model.addAttribute("boardusers", boardusers);

        // card에 있는 댓글 정보도 같이 가져가자
        List<CommentResponseDto> commentResponseDtoList = commentService.findAllCommentsByCard(card).stream().map(CommentResponseDto::new).toList();
        model.addAttribute("comments", commentResponseDtoList);

        return "card";
    }

}
