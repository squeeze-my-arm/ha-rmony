package com.harmony.card;


import com.harmony.boardcolumn.BoardColumn;
import com.harmony.boardcolumn.BoardColumnRepository;

import com.harmony.aop.BoardUserCheck;
import com.harmony.cardUser.CardUser;
import com.harmony.cardUser.CardUserRepository;
import com.harmony.column.Columns;
import com.harmony.user.User;
import com.harmony.user.UserRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@RequiredArgsConstructor
public class CardService {

    private final CardRepository cardRepository;
    private final CardUserRepository cardUserRepository;
    private final UserRepository userRepository;;
    private final BoardColumnRepository boardColumnRepository;

    public CardResponseDto getOneCard(Long cardid) {
        log.info("조회 하기");
        Card card = findCard(cardid);
        log.info(card.getCardname());
        log.info("카드의 댓글 조회");
        return new CardResponseDto(card);
    }

    @Transactional
    public void changeCardOrder(Long cardid, CardOrderRequestDto cardOrderRequestDto) {
        // 이동하는 카드 찾기
        Card card = findCard(cardid);
        log.info("cardId : " + card.getId().toString());

        // 이동하는 컬럼 찾기
        BoardColumn oldcolumn = card.getBoardColumn();
        log.info("이동 전 columnId : " + oldcolumn.getId().toString());
        BoardColumn newcolumn = findBoardColumn(cardOrderRequestDto.getColumnId());
        log.info("이동 후 columnId : " + newcolumn.getId().toString());

        if (oldcolumn.equals(newcolumn)) {
            log.info("컬럼 변경 X");

            log.info("이전 순서: " + String.valueOf(oldcolumn.getCards().indexOf(card)));
            // 1. 카드가 해당 컬럼 안에서 순서만 바뀌는 경우
            // => 원래 카드에 저장된 column의 id와 cardOrderRequestDto 에서 가져온 column의 id 값이 일치
            oldcolumn.changeCardOrder(card, cardOrderRequestDto.getCardOrder());

            log.info("이후 순서: " + String.valueOf(oldcolumn.getCards().indexOf(card)));

            for (int i=0; i<oldcolumn.getCards().size(); i++) {
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
        Card card = Card.builder().cardname(cardName).boardColumn(column).color("BLACK").build();

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
        card.updateCard(requestDto);
        //프론트에서 입력받아온 name 들, 기존 목록 비우고 새 목록의 걸 모두 추가
        if (!requestDto.getCardUserNames().isEmpty()) {
            card.clearCardUsers();
            cardUserRepository.deleteAllByCard(card);
            log.info("삭제 확인");
            //카드유저(card id가 이거인 카드유저 모두 삭제)
            for (String name : requestDto.getCardUserNames()) {
                User requestUser = userRepository.findByUsername(name)
                        .orElseThrow(() -> new IllegalArgumentException("해당 아이디를 가진 유저가 없습니다."));
                createCardUser(card, requestUser);
                //새로운 목록의 것들 모두 등록
            }
            log.info("생성 반복 확인");
        }
        return new CardResponseDto(card);
    }

    //카드 삭제
    public void deleteCard(Long boardId, Long cardId, User user) {
        cardRepository.delete(findCard(cardId));
    }

    //카드유저 생성
    @Transactional
    public void createCardUser(Card card, User user) {
        CardUser cardUser = new CardUser(card, user);

        card.addCardUser(cardUser);
        cardUserRepository.save(cardUser);
    }

    // 카드 찾기
    public Card findCard(Long cardid) {
        return cardRepository.findById(cardid).orElseThrow(IllegalArgumentException::new);
    }

    // 컬럼 찾기
    public BoardColumn findBoardColumn(Long columnid) {
        return boardColumnRepository.findById(columnid).orElseThrow(IllegalArgumentException::new);
    }
}
