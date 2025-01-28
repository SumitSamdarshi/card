package com.samda.card.game.payload;

public class CompareCardDto {
    private Integer gameId;
    private Integer playerCardId;
    private String stat;

    public Integer getGameId() {
        return gameId;
    }

    public void setGameId(Integer gameId) {
        this.gameId = gameId;
    }

    public Integer getPlayerCardId() {
        return playerCardId;
    }

    public void setPlayerCardId(Integer playerCardId) {
        this.playerCardId = playerCardId;
    }

    public String getStat() {
        return stat;
    }

    public void setStat(String stat) {
        this.stat = stat;
    }
}
