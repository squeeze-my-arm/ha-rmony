package com.harmony.card;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class CardService {

    private final CardRepository cardRepository;

    public CardResponseDto getOneCard(Long cardid) {
        Card card = findCard(cardid);

        return new CardResponseDto(card);
    }

    public Card findCard(Long cardid) {
        return cardRepository.findById(cardid).orElseThrow(IllegalArgumentException::new);
    }
}
