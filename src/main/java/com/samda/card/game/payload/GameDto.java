package com.samda.card.game.payload;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotEmpty;

import java.util.ArrayList;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class GameDto {
    private Integer game_id;
    private String game_type;
    private Integer playerId;
    private List<CardDto> computerCards =new ArrayList<>();
    private List<CardDto> playerCards =new ArrayList<>();
    private String winner;
    private Integer computerScore;
    private Integer playerScore;
    private Integer playerLostCardId;
    private Integer computerLostCardId;

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

    @JsonIgnore
    public List<CardDto> getComputerCards() {
        return computerCards;
    }

    @JsonProperty
    public void setComputerCards(List<CardDto> computerCards) {
        this.computerCards = computerCards;
    }

    public List<CardDto> getPlayerCards() {
        return playerCards;
    }

    public void setPlayerCards(List<CardDto> playerCards) {
        this.playerCards = playerCards;
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

    @JsonIgnore
    public Integer getPlayerLostCardId() {
        return playerLostCardId;
    }

    @JsonProperty
    public void setPlayerLostCardId(Integer playerLostCardId) {
        this.playerLostCardId = playerLostCardId;
    }

    @JsonIgnore
    public Integer getComputerLostCardId() {
        return computerLostCardId;
    }

    @JsonProperty
    public void setComputerLostCardId(Integer computerLostCardId) {
        this.computerLostCardId = computerLostCardId;
    }
}
