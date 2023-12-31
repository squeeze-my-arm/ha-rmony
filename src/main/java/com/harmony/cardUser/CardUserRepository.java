package com.harmony.cardUser;

import com.harmony.card.Card;
import com.harmony.card.CardResponseDto;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Arrays;
import java.util.List;

public interface CardUserRepository extends JpaRepository<CardUser, Long> {

  void deleteAllByCard(Card card);

    List<CardUser> findByCard_Id(Long id);

    List<CardUser> findByUserId(Long id);
}
