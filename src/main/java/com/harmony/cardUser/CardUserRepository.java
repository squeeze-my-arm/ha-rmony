package com.harmony.cardUser;

import com.harmony.card.Card;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CardUserRepository extends JpaRepository<CardUser, Long> {

  void deleteAllByCard(Card card);
}
