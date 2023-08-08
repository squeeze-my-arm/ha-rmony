package com.harmony.card;


import com.harmony.boardcolumn.BoardColumn;
import com.harmony.boardcolumn.BoardColumnRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

@Service
@Slf4j
@RequiredArgsConstructor
public class CardService {

    private final CardRepository cardRepository;
    private final BoardColumnRepository boardColumnRepository;

    public CardResponseDto getOneCard(Long cardid) {
        Card card = findCard(cardid);

        return new CardResponseDto(card);
    }

    public void changeCardOrder(Long cardid, CardOrderRequestDto cardOrderRequestDto) {
        // 카드 이동 (컬럼 내에서 순서만 이동하는 경우)
        // 1. 순서를 이동하는 경우 (위 혹은 아래)
        // 2. 제자리에 놔두는 경우
        // LinkedHashSet에 담긴 애들을 List로 담아서 프론트로 return 해야 함 (model에 담을때 !!)

        // 카드 이동 (컬럼 외부 이동 + 이동한 컬럼 내부에서 순서도 변경)
        // 1. 컬럼 이동 + 순서 이동
        // 2. 컬럼 이동 (맨 뒤로 가는 경우)
        // 3. 컬럼 이동 (맨 위로 가는 경우)
        // 4. 컬럼 이동 (원래 자리 번호로 가는 경우)

        // 이동하는 카드 찾기
        Card card = findCard(cardid);

        // 이동하는 컬럼 찾기
        BoardColumn oldcolumn = card.getBoardColumn();
        BoardColumn newcolumn = findBoardColumn(cardOrderRequestDto.getColumnId());

        if (oldcolumn.equals(newcolumn)) {
            // 1. 카드가 해당 컬럼 안에서 순서만 바뀌는 경우
            // => 원래 카드에 저장된 column의 id와 cardOrderRequestDto 에서 가져온 column의 id 값이 일치
            oldcolumn.changeCardOrder(card, cardOrderRequestDto.getCardOrder());

        } else {
            // 2. 카드가 다른 컬럼으로 이동하는 경우
            // => 원래 카드에 저장된 column의 id를 받아온 cardOrderRequestDto에서 가져온 column의 id값으로 새로운 column을 찾아서 set 해줌
            // => card의 순서는 자동으로 맨 뒤로 붙게 됨
            card.setBoardColumn(newcolumn); // 해당 카드의 컬럼을 새로 찾은 컬럼으로 설정함
            oldcolumn.removeCard(card);     // 기존 컬럼에서는 해당 카드를 제거해줌
            newcolumn.addCard(card);        // 새로운 컬럼의 맨 뒤로 자동으로 이동
        }
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
