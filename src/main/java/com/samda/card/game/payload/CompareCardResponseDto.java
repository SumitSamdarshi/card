package com.samda.card.game.payload;

public class CompareCardResponseDto {
    CardDto computerCard;
    GameDto game;
    String stat;
    String roundWinner;
    CardDto playerLostCard;

    public CardDto getPlayerLostCard() {
        return playerLostCard;
    }

    public void setPlayerLostCard(CardDto playerLostCard) {
        this.playerLostCard = playerLostCard;
    }

    public CardDto getComputerCard() {
        return computerCard;
    }

    public void setComputerCard(CardDto computerCard) {
        this.computerCard = computerCard;
    }

    public GameDto getGame() {
        return game;
    }

    public void setGame(GameDto game) {
        this.game = game;
    }

    public String getStat() {
        return stat;
    }

    public void setStat(String stat) {
        this.stat = stat;
    }

    public String getRoundWinner() {
        return roundWinner;
    }

    public void setRoundWinner(String roundWinner) {
        this.roundWinner = roundWinner;
    }
}
