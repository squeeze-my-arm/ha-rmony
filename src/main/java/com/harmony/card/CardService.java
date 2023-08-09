package com.harmony.card;

import com.harmony.aop.BoardUserCheck;
import com.harmony.boardColumn.BoardColumn;
import com.harmony.boardColumn.BoardColumnRepository;
import com.harmony.boardUser.BoardUserRepository;
import com.harmony.cardUser.CardUser;
import com.harmony.cardUser.CardUserRepository;
import com.harmony.user.User;
import com.harmony.user.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.concurrent.RejectedExecutionException;

@Slf4j
@RequiredArgsConstructor
@Service
public class CardService {

    private final CardRepository cardRepository;
    private final CardUserRepository cardUserRepository;
    private final BoardColumnRepository boardColumnRepository;
    private final UserRepository userRepository;
    private final BoardUserRepository boardUserRepository;

    //카드 생성
    //목록에 추가하고 그 index 에 따른 값 등록
    @BoardUserCheck
    @Transactional
    public CardResponseDto createCard(Long boardId, Long columnId, String cardName, User user) {
        BoardColumn boardColumn = boardColumnRepository.findById(columnId)
                .orElseThrow(() -> new IllegalArgumentException("컬럼이 존재하지 않습니다."));
        Card card = Card.builder().cardname(cardName).boardColumn(boardColumn).color("BLACK").build();

        cardRepository.save(card);

        boardColumn.addCard(card);
        log.info("card 생성 성공");
        return new CardResponseDto(card);
    }

    //카드 수정
    @BoardUserCheck
    @Transactional
    public CardResponseDto updateCard(Long boardId, Long cardId, CardRequestDto requestDto,
                                      User user) {
        Card card = findCard(cardId);


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
                createCardUser(boardId, card, requestUser);
            }
            log.info("생성 반복 확인");
        }
        card.updateCard(requestDto);
        cardUserRepository.deleteAllByCard(card);
        return new CardResponseDto(card);
    }

    //카드 삭제
    @BoardUserCheck
    @Transactional
    public void deleteCard(Long boardId, Long cardId, User user) {
        cardRepository.delete(findCard(cardId));
    }

    //카드유저 생성

    @Transactional
    public void createCardUser(Long boardId, Card card, User user) {
        if (!boardUserRepository.existsByUserAndBoard_Id(user, boardId)) {
            throw new RejectedExecutionException("해당 유저는 보드에 등록되어있지 않습니다.");
        }
        CardUser cardUser = new CardUser(card, user);

        card.addCardUser(cardUser);
        cardUserRepository.save(cardUser);
    }

    //특정 카드를 가진 카드유저 삭제

    @Transactional
    public void deleteCardUser(Card card) {
        cardUserRepository.deleteAllByCard(card);
    }

    @Transactional
    public Card findCard(Long cardId) {
        return cardRepository.findById(cardId)
                .orElseThrow(() -> new IllegalArgumentException("선택한 카드는 존재하지 않습니다."));
    }
}
