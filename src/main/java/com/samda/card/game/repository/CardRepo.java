package com.samda.card.game.repository;

import com.samda.card.game.entity.Card;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CardRepo extends JpaRepository<Card,Integer> {

    List<Card> findByCardType(String card_type);

    @Query("SELECT COUNT(c) FROM Card c WHERE c.cardType = :type")
    long countCards(@Param("type") String type);

    @Query("SELECT c.cardId FROM Card c WHERE c.cardType = :cardType AND c.cardId NOT IN :excludeIds")
    List<Integer> findValidCardIdsByCardTypeExcludingList(@Param("cardType") String cardType, @Param("excludeIds") List<Integer> excludeIds);
}
