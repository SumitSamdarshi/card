package com.samda.card.game.payload;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class CardDto {
    @NotNull
    private Integer cardId;
    @NotEmpty(message = "Card Name required !!")
    private String cardName;
    private String cardImage;
    @NotEmpty(message = "Card Type required !!")
    private String cardType;
    @NotEmpty(message = "Speed required !!")
    private String speed;
    @NotEmpty(message = "Combat required !!")
    private String combat;
    @NotEmpty(message = "Chakra required !!")
    private String chakra;
    @NotEmpty(message = "Jutsu required !!")
    private String jutsu;
    @NotEmpty(message = "Intel required !!")
    private String intel;
    @NotEmpty(message = "Regen required !!")
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
