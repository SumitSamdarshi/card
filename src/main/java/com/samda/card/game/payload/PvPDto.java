package com.samda.card.game.payload;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;

public class PvPDto {
    private Integer pvpGameId;
    private Integer pvpGameSize;
    private Integer playerOneId;
    private Integer playerTwoId;
    private List<CardDto> playerOneCards =new ArrayList<>();
    private List<CardDto> playerTwoCards =new ArrayList<>();
    private String winner;
    private Integer playerOneScore;
    private Integer PlayerTwoScore;
    private CardDto playerOneLastCard;
    private CardDto playerTwoLastCard;
    private String playerOneName;
    private String playerTwoName;
    private String stat;
    private String turn;
    private String roundWinner;
    private CardDto otherPlayerCard;
    private Integer PlayerOneRewardCard;
    private Integer PlayerTwoRewardCard;
    private CardDto rewardCard;

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

    public List<CardDto> getPlayerOneCards() {
        return playerOneCards;
    }

    public void setPlayerOneCards(List<CardDto> playerOneCards) {
        this.playerOneCards = playerOneCards;
    }

    public List<CardDto> getPlayerTwoCards() {
        return playerTwoCards;
    }

    public void setPlayerTwoCards(List<CardDto> playerTwoCards) {
        this.playerTwoCards = playerTwoCards;
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

    public Integer getPvpGameSize() {
        return pvpGameSize;
    }

    public void setPvpGameSize(Integer pvpGameSize) {
        this.pvpGameSize = pvpGameSize;
    }

    public String getPlayerOneName() {
        return playerOneName;
    }

    public void setPlayerOneName(String playerOneName) {
        this.playerOneName = playerOneName;
    }

    public String getPlayerTwoName() {
        return playerTwoName;
    }

    public void setPlayerTwoName(String playerTwoName) {
        this.playerTwoName = playerTwoName;
    }

    public CardDto getPlayerOneLastCard() {
        return playerOneLastCard;
    }

    public void setPlayerOneLastCard(CardDto playerOneLastCard) {
        this.playerOneLastCard = playerOneLastCard;
    }

    public CardDto getPlayerTwoLastCard() {
        return playerTwoLastCard;
    }

    public void setPlayerTwoLastCard(CardDto playerTwoLastCard) {
        this.playerTwoLastCard = playerTwoLastCard;
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

    public CardDto getOtherPlayerCard() {
        return otherPlayerCard;
    }

    public void setOtherPlayerCard(CardDto otherPlayerCard) {
        this.otherPlayerCard = otherPlayerCard;
    }

    @JsonIgnore
    public Integer getPlayerOneRewardCard() {
        return PlayerOneRewardCard;
    }

    @JsonProperty
    public void setPlayerOneRewardCard(Integer playerOneRewardCard) {
        PlayerOneRewardCard = playerOneRewardCard;
    }

    @JsonIgnore
    public Integer getPlayerTwoRewardCard() {
        return PlayerTwoRewardCard;
    }

    @JsonProperty
    public void setPlayerTwoRewardCard(Integer playerTwoRewardCard) {
        PlayerTwoRewardCard = playerTwoRewardCard;
    }

    public CardDto getRewardCard() {
        return rewardCard;
    }

    public void setRewardCard(CardDto rewardCard) {
        this.rewardCard = rewardCard;
    }
}
