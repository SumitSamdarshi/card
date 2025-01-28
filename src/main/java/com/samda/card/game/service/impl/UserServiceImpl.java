package com.samda.card.game.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.samda.card.game.entity.User;
import com.samda.card.game.exceptions.ApiExceptionHandler;
import com.samda.card.game.exceptions.ResourcesNotFoundException;
import com.samda.card.game.payload.AssignCardResponse;
import com.samda.card.game.payload.CardDto;
import com.samda.card.game.payload.UserDto;
import com.samda.card.game.repository.CardRepo;
import com.samda.card.game.repository.UserRepo;
import com.samda.card.game.service.CardService;
import com.samda.card.game.service.GameService;
import com.samda.card.game.service.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserRepo userRepo;

    @Autowired
    private CardRepo cardRepo;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    CardService cardService;

    @Autowired
    GameServiceImpl gameService;

    @Override
    public UserDto createUser(UserDto userDto) {
        User user=modelMapper.map(userDto,User.class);
        User savedUser=this.userRepo.save(user);
        return modelMapper.map(savedUser,UserDto.class);
    }

    @Override
    public UserDto updateUser(UserDto userDto, Integer userId) {
        User user = userRepo.findById(userId).orElseThrow(() -> new ResourcesNotFoundException("User", "Id", userId));
        user.setEmail(userDto.getEmail() != null ? userDto.getEmail() : user.getEmail());
        user.setAbout(userDto.getAbout()!= null ? userDto.getAbout() : user.getAbout());
        user.setName(userDto.getName() != null ? userDto.getName() : user.getName());
        user.setPassword(userDto.getPassword()!=null ? userDto.getPassword() : user.getPassword());
        user.setChest(userDto.getChest() !=null ? userDto.getChest() : user.getChest());
        user.setProfileImage(userDto.getProfileImage() != null ? userDto.getProfileImage() : user.getProfileImage());
        User updatedUser=userRepo.save(user);
        return modelMapper.map(updatedUser,UserDto.class);
    }

    @Override
    public UserDto getUserById(Integer userId) {
        User user = userRepo.findById(userId).orElseThrow(() -> new ResourcesNotFoundException("User", "Id", userId));
        return modelMapper.map(user,UserDto.class);
    }

    @Override
    public void deleteUser(Integer userId) {
        User user = userRepo.findById(userId).orElseThrow(() -> new ResourcesNotFoundException("User", "Id", userId));
        userRepo.delete(user);
    }

    @Override
    public UserDto registerNewUser(UserDto userDto) {
        User user = this.modelMapper.map(userDto, User.class);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setMatches(0);
        user.setWins(0);
        user.setLosses(0);
        user.setNoOfCards(0);
        user.setWinStreak(0);
        user.setDraws(0);
        user.setChest(0);
        User newUser = this.userRepo.save(user);
        return this.modelMapper.map(newUser, UserDto.class);
    }

    @Override
    public AssignCardResponse assignFirstCards(Integer userId) throws JsonProcessingException {
        User user = userRepo.findById(userId).orElseThrow(() -> new ResourcesNotFoundException("User", "Id", userId));
        if(!user.getCards().isEmpty()){
            throw new ApiExceptionHandler("Already Assigned !!!");
        }
        List<Integer> cards=generateCardList();
        List<CardDto> cardDtos=getCardDtos(cards);
        Collections.sort(cards);
        user.setCards(cards);
        user.setNoOfCards(10);
        User updatedUser=userRepo.save(user);
        AssignCardResponse response=new AssignCardResponse();
        response.setUser(modelMapper.map(updatedUser,UserDto.class));
        response.setCards(cardDtos);
        return response;
    }

    List<Integer> generateCardList(){
        List<CardDto>  cardDtos=cardService.getCardByType("common");
        List<Integer> commonCards=new ArrayList<>(cardDtos.stream().map(CardDto::getCardId).toList());
        Collections.shuffle(commonCards);
        return commonCards.subList(0, 10);
    }

    List<CardDto> getCardDtos(List<Integer> cards){
        List<CardDto> cardDtos = cards.stream()
                .map(cardId -> cardRepo.findById(cardId).orElse(null))
                .map(card -> modelMapper.map(card, CardDto.class))
                .toList();
        return cardDtos;
    }
}
