package com.samda.card.game.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "cards")
@NoArgsConstructor
@Getter
@Setter
public class Card {
    @Id
    private Integer cardId;
    private String cardName;
    private String cardImage;
    private String cardType;
    private String speed;
    private String combat;
    private String chakra;
    private String jutsu;
    private String intel;
    private String regen;

    public Integer getCardId() {
        return cardId;
    }

    public void setCardId(Integer cardId) {
        this.cardId = cardId;
    }

    public String getCardName() {
        return cardName;
    }

    public void setCardName(String cardName) {
        this.cardName = cardName;
    }

    public String getCardImage() {
        return cardImage;
    }

    public void setCardImage(String cardImage) {
        this.cardImage = cardImage;
    }

    public String getCardType() {
        return cardType;
    }

    public void setCardType(String cardType) {
        this.cardType = cardType;
    }

    public String getSpeed() {
        return speed;
    }

    public void setSpeed(String speed) {
        this.speed = speed;
    }

    public String getCombat() {
        return combat;
    }

    public void setCombat(String combat) {
        this.combat = combat;
    }

    public String getChakra() {
        return chakra;
    }

    public void setChakra(String chakra) {
        this.chakra = chakra;
    }

    public String getJutsu() {
        return jutsu;
    }

    public void setJutsu(String jutsu) {
        this.jutsu = jutsu;
    }

    public String getIntel() {
        return intel;
    }

    public void setIntel(String intel) {
        this.intel = intel;
    }

    public String getRegen() {
        return regen;
    }

    public void setRegen(String regen) {
        this.regen = regen;
    }
}
