package com.samda.card.game.entity;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.samda.card.game.payload.CardDto;
import jakarta.persistence.*;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Table(name = "pvp") // created users table
@NoArgsConstructor
public class PvP {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer pvpGameId;
    private Integer pvpGameSize;
    private Integer playerOneId;
    private Integer playerTwoId;
    private String playerOneCards;
    private String playerTwoCards;
    private String winner;
    private Integer playerOneScore;
    private Integer PlayerTwoScore;
    private Integer playerOneLastCard;
    private Integer playerTwoLastCard;
    private String playerOneName;
    private String playerTwoName;
    private String stat;
    private String turn;
    private String roundWinner;
    private Integer otherPlayerCard;
    private Integer PlayerOneRewardCard;
    private Integer PlayerTwoRewardCard;
    private Integer rewardCard;

    public Integer getPvpGameId() {
        return pvpGameId;
    }

    public void setPvpGameId(Integer pvpGameId) {
        this.pvpGameId = pvpGameId;
    }

    public Integer getPlayerOneId() {
        return playerOneId;
    }

    public void setPlayerOneId(Integer playerOneId) {
        this.playerOneId = playerOneId;
    }

    public Integer getPlayerTwoId() {
        return playerTwoId;
    }

    public void setPlayerTwoId(Integer playerTwoId) {
        this.playerTwoId = playerTwoId;
    }

    public List<Integer> getPlayerOneCards() {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            return objectMapper.readValue(this.playerOneCards, new TypeReference<List<Integer>>(){});
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    public void setPlayerOneCards(List<Integer> playerOneCards) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        this.playerOneCards = objectMapper.writeValueAsString(playerOneCards);
    }

    public List<Integer> getPlayerTwoCards() {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            return objectMapper.readValue(this.playerTwoCards, new TypeReference<List<Integer>>(){});
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    public void setPlayerTwoCards(List<Integer> playerTwoCards) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        this.playerTwoCards = objectMapper.writeValueAsString(playerTwoCards);
    }

    public String getWinner() {
        return winner;
    }

    public void setWinner(String winner) {
        this.winner = winner;
    }

    public Integer getPlayerOneScore() {
        return playerOneScore;
    }

    public void setPlayerOneScore(Integer playerOneScore) {
        this.playerOneScore = playerOneScore;
    }

    public Integer getPlayerTwoScore() {
        return PlayerTwoScore;
    }

    public void setPlayerTwoScore(Integer playerTwoScore) {
        PlayerTwoScore = playerTwoScore;
    }

    public Integer getPlayerOneLastCard() {
        return playerOneLastCard;
    }

    public void setPlayerOneLastCard(Integer playerOneLastCard) {
        this.playerOneLastCard = playerOneLastCard;
    }

    public Integer getPlayerTwoLastCard() {
        return playerTwoLastCard;
    }

    public void setPlayerTwoLastCard(Integer playerTwoLastCard) {
        this.playerTwoLastCard = playerTwoLastCard;
    }

    public Integer getPvpGameSize() {
        return pvpGameSize;
    }

    public void setPvpGameSize(Integer pvpGameSize) {
        this.pvpGameSize = pvpGameSize;
    }

    public String getPlayerTwoName() {
        return playerTwoName;
    }

    public void setPlayerTwoName(String playerTwoName) {
        this.playerTwoName = playerTwoName;
    }

    public String getPlayerOneName() {
        return playerOneName;
    }

    public void setPlayerOneName(String playerOneName) {
        this.playerOneName = playerOneName;
    }

    public String getStat() {
        return stat;
    }

    public void setStat(String stat) {
        this.stat = stat;
    }

    public String getTurn() {
        return turn;
    }

    public void setTurn(String turn) {
        this.turn = turn;
    }

    public String getRoundWinner() {
        return roundWinner;
    }

    public void setRoundWinner(String roundWinner) {
        this.roundWinner = roundWinner;
    }

    public Integer getOtherPlayerCard() {
        return otherPlayerCard;
    }

    public void setOtherPlayerCard(Integer otherPlayerCard) {
        this.otherPlayerCard = otherPlayerCard;
    }

    public Integer getPlayerOneRewardCard() {
        return PlayerOneRewardCard;
    }

    public void setPlayerOneRewardCard(Integer playerOneRewardCard) {
        PlayerOneRewardCard = playerOneRewardCard;
    }

    public Integer getPlayerTwoRewardCard() {
        return PlayerTwoRewardCard;
    }

    public void setPlayerTwoRewardCard(Integer playerTwoRewardCard) {
        PlayerTwoRewardCard = playerTwoRewardCard;
    }

    public Integer getRewardCard() {
        return rewardCard;
    }

    public void setRewardCard(Integer rewardCard) {
        this.rewardCard = rewardCard;
    }
}