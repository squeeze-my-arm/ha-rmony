package com.harmony.card;


import com.harmony.aop.BoardUserCheck;
import com.harmony.boardColumn.BoardColumn;
import com.harmony.boardColumn.BoardColumnRepository;
import com.harmony.boardUser.BoardUser;
import com.harmony.boardUser.BoardUserRepository;
import com.harmony.cardUser.CardUser;
import com.harmony.cardUser.CardUserRepository;
import com.harmony.cardUser.CardUserResponseDto;
import com.harmony.user.User;
import com.harmony.user.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.RejectedExecutionException;

@Service
@Slf4j
@RequiredArgsConstructor
public class CardService {

    private final CardRepository cardRepository;
    private final CardUserRepository cardUserRepository;
    private final BoardColumnRepository boardColumnRepository;
    private final UserRepository userRepository;
    private final BoardUserRepository boardUserRepository;

    public CardResponseDto getOneCard(Long cardId) {
        log.info("조회 하기");
        Card card = findCard(cardId);
        log.info(card.getCardname());
        log.info("카드의 댓글 조회");
        return new CardResponseDto(card);
    }

    @Transactional
    public void changeCardOrder(Long cardId, CardOrderRequestDto cardOrderRequestDto) {
        // 이동하는 카드 찾기
        Card card = findCard(cardId);
        log.info("cardId : " + card.getId().toString());

        // 전달받는 index는 0부터 시작하는 값을 기준으로 받아야 함
        // 카드의 order는 0부터 시작하는 것으로 설정

        // 이동하는 컬럼 찾기
        BoardColumn oldcolumn = card.getBoardColumn();
        log.info("이동 전 columnId : " + oldcolumn.getId().toString());
        BoardColumn newcolumn = findBoardColumn(cardOrderRequestDto.getColumnId());
        log.info("이동 후 columnId : " + newcolumn.getId().toString());

        if (oldcolumn.equals(newcolumn)) {
            log.info("컬럼 변경 X");

            log.info("이전 순서: " + oldcolumn.getCards().indexOf(card));
            // 1. 카드가 해당 컬럼 안에서 순서만 바뀌는 경우
            // => 원래 카드에 저장된 column의 id와 cardOrderRequestDto 에서 가져온 column의 id 값이 일치
            oldcolumn.changeCardOrder(card, cardOrderRequestDto.getCardOrder());

            log.info("이후 순서: " + oldcolumn.getCards().indexOf(card));

            for (int i = 0; i < oldcolumn.getCards().size(); i++) {
                log.info(oldcolumn.getCards().get(i).getCardname() + ", index: " + i);
                oldcolumn.getCards().get(i).setCardOrder(i);
            }
            BoardColumn boardColumn = boardColumnRepository.save(oldcolumn);
        } else {
            log.info("컬럼 변경 O");
            // 2. 카드가 다른 컬럼으로 이동하는 경우
            // => 원래 카드에 저장된 column의 id를 받아온 cardOrderRequestDto에서 가져온 column의 id값으로 새로운 column을 찾아서 set 해줌
            // => card의 순서는 자동으로 맨 뒤로 붙게 됨
            card.setBoardColumn(newcolumn); // 해당 카드의 컬럼을 새로 찾은 컬럼으로 설정함
            oldcolumn.removeCard(card);     // 기존 컬럼에서는 해당 카드를 제거해줌
            newcolumn.addCard(card);        // 새로운 컬럼의 맨 뒤로 자동으로 이동
        }
    }

    //카드 생성
    //목록에 추가하고 그 index 에 따른 값 등록
    @BoardUserCheck
    @Transactional
    public CardResponseDto createCard(Long boardId, Long columnId, String cardName, User user) {
        BoardColumn column = boardColumnRepository.findById(columnId)
                .orElseThrow(() -> new IllegalArgumentException("컬럼이 존재하지 않습니다."));
        Card card = Card.builder().cardname(cardName).boardColumn(column).color("#FFFFFF").build();

        card.setCardOrder(column.getCards().size() + 1);

        cardRepository.save(card);

        column.addCard(card);
        log.info("card 생성 성공");
        return new CardResponseDto(card);
    }

    //카드 수정
    @BoardUserCheck
    @Transactional
    public CardResponseDto updateCard(Long boardId, Long cardId, CardRequestDto requestDto,
                                      User user) {
        Card card = findCard(cardId);
        for (String s : requestDto.getCardUserNames()) {
            log.info("username: " + s);
        }

        cardUserRepository.deleteAllByCard(card);

        //프론트에서 입력받아온 name 들, 기존 목록 비우고 새 목록의 걸 모두 추가
        if (!requestDto.getCardUserNames().isEmpty()) {
            //카드에 있는 카드유저 목록 비우기
            card.clearCardUsers();
            //유저에 있는 카드 유저 목록 중 이 카드의 카드유저만 제거
            user.getCardUsers().removeIf(cu -> cu.getCard().getId().equals(cardId));
            //카드유저(card id가 이거인 카드유저 모두 삭제)
            deleteCardUser(card);


            log.info("삭제 확인");

            //새로운 목록의 것들 모두 등록
            for (String name : requestDto.getCardUserNames()) {
                User requestUser = userRepository.findByUsername(name)
                        .orElseThrow(() -> new IllegalArgumentException("해당 아이디를 가진 유저가 없습니다."));
                log.info(user.getUsername());
                createCardUser(boardId, card, requestUser);
            }
            log.info("생성 반복 확인");
        }

        card.updateCard(requestDto);
        return new CardResponseDto(card);
    }

    //카드 삭제
    @Transactional
    public void deleteCard(Long boardId, Long cardId, User user) {
        Card card = findCard(cardId);
        BoardColumn boardColumn = findBoardColumn(card.getBoardColumn().getId());

        // 해당 컬럼의 카드 리스트에서 카드 지우기
        boardColumn.removeCard(card);

        // 카드 order 재정렬하기
        List<Card> cards = boardColumn.getCards();
        for (int i = 0; i < cards.size(); i++) {
            cards.get(i).setCardOrder(i);
            cardRepository.save(cards.get(i));
        }

        cardRepository.delete(findCard(cardId));
    }

    // 카드 유저 생성
    @Transactional
    public void createCardUser(Long boardId, Card card, User user) {
        if (!boardUserRepository.existsByUserAndBoard_Id(user, boardId)) {
            throw new RejectedExecutionException("해당 유저는 보드에 등록되어있지 않습니다.");
        }
        CardUser cardUser = new CardUser(card, user);
        log.info(cardUser.getUsername());
        log.info(cardUser.getNickname());
        card.addCardUser(cardUser);
        cardUserRepository.save(cardUser);
        log.info("카드 유저 생성 및 저장");
    }


    //특정 카드를 가진 카드유저 삭제

    @Transactional
    public void deleteCardUser(Card card) {
        cardUserRepository.deleteAllByCard(card);
    }

    // 카드 찾기
    public Card findCard(Long cardId) {
        return cardRepository.findById(cardId).orElseThrow(IllegalArgumentException::new);
    }

    // 컬럼 찾기
    public BoardColumn findBoardColumn(Long columnid) {
        return boardColumnRepository.findById(columnid).orElseThrow(IllegalArgumentException::new);

    }

    // 사용자 찾기
    public List<CardUserResponseDto> findUsers(List<BoardUser> boardUser, List<CardUser> cardUsers) {
        List<CardUserResponseDto> cardUserResponseDtos = new ArrayList<>();

        for (BoardUser user: boardUser) {
            Boolean selected = false;
            if (cardUsers.size()>0) {
                for (CardUser cardUser: cardUsers) {
                    // unique 값인 username으로 비교함
                    if (user.getUsername().equals(cardUser.getUsername())) {
                        selected = true;
                    }
                }
            }
            cardUserResponseDtos.add(new CardUserResponseDto(user, selected));
        }

        return cardUserResponseDtos;
    }
}
