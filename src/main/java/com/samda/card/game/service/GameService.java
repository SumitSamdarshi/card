package com.samda.card.game.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.samda.card.game.payload.*;

public interface GameService {
    GameDto createGame(GameDto gameDto) throws JsonProcessingException;
    CompareCardResponseDto compareCards(CompareCardDto compareCardDto) throws JsonProcessingException;
    RewardsDto getRewards(Integer gameId);
    QuitResponse quitGame(Integer gameId) throws JsonProcessingException;
}
