package com.harmony.card;

import com.harmony.board.Board;
import com.harmony.boardColumn.BoardColumn;
import com.harmony.boardColumn.BoardColumnRepository;
import com.harmony.boardUser.BoardUserRepository;
import com.harmony.cardUser.CardUserRepository;
import com.harmony.user.User;
import com.harmony.user.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CardServiceTest {

    @InjectMocks
    CardService cardService;

    @Mock
    CardRepository cardRepository;

    @Mock
    CardUserRepository cardUserRepository;

    @Mock
    BoardColumnRepository boardColumnRepository;

    @Mock
    BoardUserRepository boardUserRepository;

    @Mock
    UserRepository userRepository;

    @BeforeEach
    void init() {
        MockitoAnnotations.openMocks(this);
        cardService = new CardService(cardRepository, cardUserRepository, boardColumnRepository, userRepository, boardUserRepository);
    }

    @Test
    @DisplayName("CardService: 카드 상세 조회 확인")
    void getOneCard() {
        // given
        var board = Board.builder().boardTitle("newBoard").build();
        var boardColumn = BoardColumn.builder().boardColumnName("new").board(board).build();
        var card = Card.builder().cardname("cardname").boardColumn(boardColumn).build();

        // when
        when(cardRepository.findById(any(Long.class))).thenReturn(Optional.ofNullable(card));
        CardResponseDto cardResponseDto = cardService.getOneCard(20L);

        // then
        assert card.getCardname().equals(cardResponseDto.getCardName());
    }

    @Test
    @DisplayName("CardService: 카드 생성 확인")
    void createCard() {
        // given
        var user = User.builder().username("user@naver.com").password("pass").nickname("nininin").build();
        var board = Board.builder().boardTitle("newBoard").build();
        var boardColumn = BoardColumn.builder().boardColumnName("new").board(board).build();
        var card = Card.builder().cardname("cardname").boardColumn(boardColumn).build();

        // when
        when(boardColumnRepository.findById(any())).thenReturn(Optional.ofNullable(boardColumn));
        when(cardRepository.save(any(Card.class))).thenReturn(card); // save 테스트 시 바로 return
        CardResponseDto cardResponseDto = cardService.createCard(20L, 20L, "cardname", user);

        // then
        assert card.getCardname().equals(cardResponseDto.getCardName());
    }

    @Test
    @DisplayName("CardService: 카드 수정 확인")
    void updateCard() {
        // given
        var user = User.builder().username("user@naver.com").password("pass").nickname("nininin").build();
        var board = Board.builder().boardTitle("newBoard").build();
        var boardColumn = BoardColumn.builder().boardColumnName("new").board(board).build();
        var card = Card.builder().cardname("cardname").boardColumn(boardColumn).build();
        var cardRequestDto = CardRequestDto.builder().cardName("card").build();

        // when
        when(cardRepository.findById(any())).thenReturn(Optional.of(card)); // save 테스트 시 바로 return
        CardResponseDto cardResponseDto = cardService.updateCard(board.getId(), card.getId(), cardRequestDto, user);

        // then
        assert cardRequestDto.getCardName().equals(cardResponseDto.getCardName());
    }

    @Test
    @DisplayName("CardService: 카드 삭제 확인")
    void deleteCard() {
        // given
        var user = User.builder().username("user@naver.com").password("pass").nickname("nininin").build();
        var board = Board.builder().boardTitle("newBoard").build();
        var boardColumn = BoardColumn.builder().boardColumnName("new").board(board).build();
        var card = Card.builder().cardname("cardname").boardColumn(boardColumn).build();
        Integer beforeSize = cardRepository.findAll().size();

        // when
        when(cardRepository.findById(any())).thenReturn(Optional.of(card)); // save 테스트 시 바로 return
        when(boardColumnRepository.findById(any())).thenReturn(Optional.of(boardColumn));
        cardService.deleteCard(board.getId(), card.getId(), user);

        Integer afterSize = cardRepository.findAll().size();
        // then
        assert beforeSize == afterSize;
    }



}