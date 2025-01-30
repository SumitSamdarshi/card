package com.samda.card.game.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.samda.card.game.payload.CardDto;
import com.samda.card.game.payload.CombineCardRequestDto;
import com.samda.card.game.payload.DrawCardDto;
import com.samda.card.game.payload.UserDto;

import java.util.List;

public interface CardService {
    CardDto addCard(CardDto cardDto);
    CardDto getCard(Integer cardId);
    CardDto updateCard(CardDto cardDto,Integer cardId);
    List<CardDto> getAllCard();
    List<CardDto> getAllCardForUser(Integer userId);
    List<CardDto> getAllDistinctCardForUser(Integer userId);
    List<CardDto> getCardByType(String cardType);
    List<CardDto> getUserCardByType(Integer userId,String cardType);
    void deleteCard(Integer cardId);
    DrawCardDto drawCard(UserDto userDto);
    DrawCardDto combineCard(CombineCardRequestDto req) throws JsonProcessingException;
}
