package com.samda.card.game.entity;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.*;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Table(name = "games") // created users table
@NoArgsConstructor
public class Game {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer game_id;
    private String game_type;
    private Integer playerId;
    private String computerCards;
    private String playerCards;
    private String winner;
    private Integer computerScore;
    private Integer playerScore;
    private Integer playerLostCardId;
    private Integer computerLostCardId;
    private String turn;
    private Integer winChestNumber;

    public Integer getGame_id() {
        return game_id;
    }

    public void setGame_id(Integer game_id) {
        this.game_id = game_id;
    }

    public String getGame_type() {
        return game_type;
    }

    public void setGame_type(String game_type) {
        this.game_type = game_type;
    }

    public Integer getPlayerId() {
        return playerId;
    }

    public void setPlayerId(Integer playerId) {
        this.playerId = playerId;
    }

    public List<Integer> getComputerCards() {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            return objectMapper.readValue(this.computerCards, new TypeReference<List<Integer>>(){});
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    public void setComputerCards(List<Integer> computerCards) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        this.computerCards = objectMapper.writeValueAsString(computerCards);
    }

    public List<Integer> getPlayerCards() {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            return objectMapper.readValue(this.playerCards, new TypeReference<List<Integer>>(){});
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    public void setPlayerCards(List<Integer> playerCards) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        this.playerCards = objectMapper.writeValueAsString(playerCards);
    }

    public String getWinner() {
        return winner;
    }

    public void setWinner(String winner) {
        this.winner = winner;
    }

    public Integer getPlayerScore() {
        return playerScore;
    }

    public void setPlayerScore(Integer playerScore) {
        this.playerScore = playerScore;
    }

    public Integer getComputerScore() {
        return computerScore;
    }

    public void setComputerScore(Integer computerScore) {
        this.computerScore = computerScore;
    }

    public Integer getPlayerLostCardId() {
        return playerLostCardId;
    }

    public void setPlayerLostCardId(Integer playerLostCardId) {
        this.playerLostCardId = playerLostCardId;
    }

    public Integer getComputerLostCardId() {
        return computerLostCardId;
    }

    public void setComputerLostCardId(Integer computerLostCardId) {
        this.computerLostCardId = computerLostCardId;
    }

    public String getTurn() {
        return turn;
    }

    public void setTurn(String turn) {
        this.turn = turn;
    }

    public Integer getWinChestNumber() {
        return winChestNumber;
    }

    public void setWinChestNumber(Integer winChestNumber) {
        this.winChestNumber = winChestNumber;
    }
}
