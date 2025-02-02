package com.samda.card.game.payload;

public class SubmitCardRequestDto {
    private Integer pvpGameId;
    private Integer playerId;
    private Integer cardId;
    private String stat;

    public Integer getPvpGameId() {
        return pvpGameId;
    }

    public void setPvpGameId(Integer pvpGameId) {
        this.pvpGameId = pvpGameId;
    }

    public Integer getPlayerId() {
        return playerId;
    }

    public void setPlayerId(Integer playerId) {
        this.playerId = playerId;
    }

    public Integer getCardId() {
        return cardId;
    }

    public void setCardId(Integer cardId) {
        this.cardId = cardId;
    }

    public String getStat() {
        return stat;
    }

    public void setStat(String stat) {
        this.stat = stat;
    }
}
