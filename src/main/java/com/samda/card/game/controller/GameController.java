package com.samda.card.game.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.samda.card.game.payload.*;
import com.samda.card.game.service.GameService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/card-game/api/games")
public class GameController {
    @Autowired
    GameService gameService;

    @PostMapping("/createGame")
    public ResponseEntity<GameDto> createGame(@RequestBody GameDto gameDto){
        GameDto game= null;
        try {
            game = gameService.createGame(gameDto);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        return new  ResponseEntity<>(game, HttpStatus.OK);
    }

    @PostMapping("/compareCard")
    public ResponseEntity<CompareCardResponseDto> compareCards(@RequestBody CompareCardDto compareCardDto) throws JsonProcessingException {
        CompareCardResponseDto response=gameService.compareCards(compareCardDto);
        return new  ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/getReward/{gameId}")
    public ResponseEntity<RewardsDto> getReward(@PathVariable("gameId") Integer id){
        RewardsDto rewards=gameService.getRewards(id);
        return new ResponseEntity<>(rewards,HttpStatus.OK);
    }

    @GetMapping("/quit/{gameId}")
    public ResponseEntity<QuitResponse> quitGame(@PathVariable Integer gameId) throws JsonProcessingException {
        QuitResponse response=gameService.quitGame(gameId);
        return new ResponseEntity<>(response,HttpStatus.OK);
    }
}
