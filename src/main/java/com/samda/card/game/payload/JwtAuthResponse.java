package com.samda.card.game.payload;

public class JwtAuthResponse {

    private String token;
    private UserDto user;


    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public UserDto getUser() {
        return user;
    }

    public void setUser(UserDto user) {
        this.user = user;
    }
}