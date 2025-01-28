package com.samda.card.game.payload;

public class QuitResponse {
    UserDto user;
    GameDto game;
    CardDto playerLostCard;

    public UserDto getUser() {
        return user;
    }

    public void setUser(UserDto user) {
        this.user = user;
    }

    public GameDto getGame() {
        return game;
    }

    public void setGame(GameDto game) {
        this.game = game;
    }

    public CardDto getPlayerLostCard() {
        return playerLostCard;
    }

    public void setPlayerLostCard(CardDto playerLostCard) {
        this.playerLostCard = playerLostCard;
    }
}
