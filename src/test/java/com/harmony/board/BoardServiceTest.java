package com.harmony.board;

import com.harmony.boardUser.BoardUser;
import com.harmony.boardUser.BoardUserEnum;
import com.harmony.boardUser.BoardUserRepository;
import com.harmony.card.CardService;
import com.harmony.user.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BoardServiceTest {

    @InjectMocks
    BoardService boardService;

    @Mock
    BoardRepository boardRepository;

    @Mock
    BoardUserRepository boardUserRepository;

    @BeforeEach
    void init() {
        MockitoAnnotations.openMocks(this);
        boardService = new BoardService(boardRepository, boardUserRepository);
    }

    @Test
    @DisplayName("BoardService: 보드 리스트 가져오기")
    void getBoardList() {
        var user = User.builder().username("user@naver.com").password("pass").nickname("nininin").build();
        var board = Board.builder().boardTitle("newBoard").build();
        var board2 = Board.builder().boardTitle("newThing").build();
        var boarduser = new BoardUser(board, user, BoardUserEnum.ADMIN);
        var boarduser2 = new BoardUser(board2, user, BoardUserEnum.ADMIN);
        List<BoardUser> boardUsers = new ArrayList<>();
        boardUsers.add(boarduser);
        boardUsers.add(boarduser2);

        when(boardUserRepository.findAllByUser(user)).thenReturn(boardUsers);


    }



}