package com.samda.card.game.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.samda.card.game.payload.AssignCardResponse;
import com.samda.card.game.payload.UserDto;

import java.util.List;

public interface UserService {
    UserDto createUser(UserDto user);
    UserDto updateUser(UserDto user, Integer userId);
    UserDto getUserById(Integer userId);
    void deleteUser(Integer userId);
    UserDto registerNewUser(UserDto user);
    AssignCardResponse assignFirstCards(Integer userId) throws JsonProcessingException;
}
