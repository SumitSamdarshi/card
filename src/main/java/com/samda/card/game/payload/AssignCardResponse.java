package com.samda.card.game.payload;

import java.util.List;

public class AssignCardResponse {
    List<CardDto> cards;
    UserDto user;

    public List<CardDto> getCards() {
        return cards;
    }

    public void setCards(List<CardDto> cards) {
        this.cards = cards;
    }

    public UserDto getUser() {
        return user;
    }

    public void setUser(UserDto user) {
        this.user = user;
    }
}
