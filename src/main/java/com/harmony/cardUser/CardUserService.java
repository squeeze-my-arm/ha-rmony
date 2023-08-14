package com.harmony.cardUser;

import com.harmony.aop.BoardUserCheck;
import com.harmony.boardUser.BoardUser;
import com.harmony.boardUser.BoardUserRepository;
import com.harmony.card.Card;
import com.harmony.card.CardRepository;
import com.harmony.card.CardRequestUserDto;
import com.harmony.card.CardResponseDto;
import com.harmony.user.User;
import com.harmony.user.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.RejectedExecutionException;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class CardUserService {
    private final CardUserRepository cardUserRepository;
    private final BoardUserRepository boardUserRepository;
    private final UserRepository userRepository;
    private final CardRepository cardRepository;


    // 카드 유저 생성
    @BoardUserCheck
    @Transactional
    public void createCardUser(Long boardId, Card card, User user) {
        if (!boardUserRepository.existsByUserAndBoard_Id(user, boardId)) {
            throw new RejectedExecutionException("해당 유저는 보드에 등록되어있지 않습니다.");
        }
        CardUser cardUser = new CardUser(card, user);
        card.addCardUser(cardUser);
        cardUserRepository.save(cardUser);
        log.info("카드 유저 생성 및 저장");
    }

    @BoardUserCheck
    @Transactional
    public CardResponseDto updateCardUser(Long boardId, Long cardId,
                                          CardRequestUserDto requestDto, User user) {

        Card card = cardRepository.findById(cardId).orElseThrow();
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

        cardRepository.save(card);

        return new CardResponseDto(card);
    }

    //특정 카드를 가진 카드유저 삭제
    @Transactional
    public void deleteCardUser(Card card) {
        cardUserRepository.deleteAllByCard(card);
    }

    //보드 유저 중 카드유저 찾기
    public List<CardUserResponseDto> findUsers(List<BoardUser> boardUsers, List<CardUser> cardUsers) {
        List<CardUserResponseDto> cardUserResponseDtos = new ArrayList<>();

        for (BoardUser user : boardUsers) {
            boolean selected = false;
            if (cardUsers.size() > 0) {
                for (CardUser cardUser : cardUsers) {
                    // unique 값인 username 으로 비교함
                    if (user.getUsername().equals(cardUser.getUsername())) {
                        selected = true;
                        break;
                    }
                }
            }
            cardUserResponseDtos.add(new CardUserResponseDto(user, selected));
        }

        return cardUserResponseDtos;
    }

    public List<CardResponseDto> findCardId(Long userId) {
        List<CardUser> cardUsers = cardUserRepository.findByUserId(userId);

        List<Long> cardIds = cardUsers.stream()
                .map(CardUser::getCard)
                .map(Card::getId)
                .collect(Collectors.toList());


        List<CardResponseDto> cardResponseDtos = new ArrayList<>();

        for (Long id: cardIds) {
            cardResponseDtos.add(cardRepository.findById(id).map(CardResponseDto::new).orElseThrow());
        }

        return cardResponseDtos;
    }

    public List<CardUser> findCardUserByCardId(Long id) {
        return cardUserRepository.findByCard_Id(id);
    }
}
