package com.samda.card.game.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.samda.card.game.payload.*;

import java.util.List;

public interface PvPService {
    PvPDto createPvP(PvPDto pvpDto) throws JsonProcessingException;
    PvPDto joinPvP(PvPDto pvpDto) throws JsonProcessingException;
    void submitCard(SubmitCardRequestDto pvPDto) throws JsonProcessingException;
    PvPDto enquiry(Integer pvpGameId,Integer id) throws JsonProcessingException;
}
