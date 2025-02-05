package com.samda.card.game.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.samda.card.game.entity.Card;
import com.samda.card.game.entity.PvP;
import com.samda.card.game.entity.User;
import com.samda.card.game.exceptions.ApiExceptionHandler;
import com.samda.card.game.exceptions.ResourcesNotFoundException;
import com.samda.card.game.payload.*;
import com.samda.card.game.repository.CardRepo;
import com.samda.card.game.repository.PvPRepo;
import com.samda.card.game.repository.UserRepo;
import com.samda.card.game.service.CardService;
import com.samda.card.game.service.PvPService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class PvPServiceImpl implements PvPService {
    @Autowired
    PvPRepo pvpRepo;

    @Autowired
    UserRepo userRepo;

    @Autowired
    CardRepo cardRepo;

    @Autowired
    CardService cardService;

    @Autowired
    ModelMapper modelMapper;

    @Override
    public PvPDto createPvP(PvPDto pvpDto) throws JsonProcessingException {
        User playerOne = userRepo.findById(pvpDto.getPlayerOneId()).orElseThrow(() -> new ResourcesNotFoundException("User", "Id", pvpDto.getPlayerOneId()));
        int gameSize=pvpDto.getPvpGameSize();
        List<Integer> playerOneCards= new ArrayList<>(playerOne.getCards().stream()
                .distinct()
                .toList());
        if(playerOneCards.size()<gameSize){
            throw new ApiExceptionHandler("Player having insufficient cards");
        }
        Collections.shuffle(playerOneCards);
        playerOneCards = playerOneCards.subList(0, gameSize);
        List<CardDto> playerCardDtoList = playerOneCards.stream()
                .map(id -> cardRepo.findById(id).orElse(null))
                .map(card -> modelMapper.map(card, CardDto.class))
                .toList();

        Random rand = new Random();
        int randomIndex = rand.nextInt(playerOneCards.size());
        Integer rewardCard =playerOneCards.get(randomIndex);

        pvpDto.setPlayerOneScore(0);
        pvpDto.setPlayerTwoScore(0);
        pvpDto.setPlayerOneName(playerOne.getName());
        pvpDto.setPlayerOneCards(playerCardDtoList);
        pvpDto.setTurn("p1");
        pvpDto.setPlayerOneRewardCard(rewardCard);

        PvP pvp =pvpDtoToPvP(pvpDto);
        PvP createdPvP= pvpRepo.save(pvp);
        pvpDto.setPvpGameId(createdPvP.getPvpGameId());
        return pvpDto;
    }

    @Override
    public PvPDto joinPvP(PvPDto request) throws JsonProcessingException {
        User playerTwo = userRepo.findById(request.getPlayerTwoId()).orElseThrow(() -> new ResourcesNotFoundException("User", "Id", request.getPlayerTwoId()));
        PvP pvp=pvpRepo.findById(request.getPvpGameId()).orElseThrow(() -> new ResourcesNotFoundException("Game", "Id", request.getPvpGameId()));
        int gameSize=pvp.getPvpGameSize();
        List<Integer> playerTwoCards= new ArrayList<>(playerTwo.getCards().stream()
                .distinct()
                .toList());
        if(playerTwoCards.size()<gameSize){
            throw new ApiExceptionHandler("Player having insufficient cards");
        }
        Collections.shuffle(playerTwoCards);
        playerTwoCards = playerTwoCards.subList(0, gameSize);
        List<CardDto> playerCardDtoList = playerTwoCards.stream()
                .map(id -> cardRepo.findById(id).orElse(null))
                .map(card -> modelMapper.map(card, CardDto.class))
                .toList();
        Random rand = new Random();
        int randomIndex = rand.nextInt(playerTwoCards.size());
        Integer rewardCard =playerTwoCards.get(randomIndex);

        pvp.setPlayerTwoName(playerTwo.getName());
        pvp.setPlayerTwoCards(playerTwoCards);
        pvp.setPlayerTwoId(request.getPlayerTwoId());
        pvp.setPlayerTwoRewardCard(rewardCard);
        pvpRepo.save(pvp);
        return pvpToPvpDto(pvp);
    }

    @Override
    public void submitCard(SubmitCardRequestDto req) throws JsonProcessingException {
        Integer gameId =req.getPvpGameId();
        PvP pvp =pvpRepo.findById(gameId).orElseThrow(() -> new ResourcesNotFoundException("Game", "Id", gameId));
        if(!Objects.equals(req.getPlayerId(), pvp.getPlayerOneId()) && !Objects.equals(req.getPlayerId(), pvp.getPlayerTwoId())){
            throw new ApiExceptionHandler("Player not in game");
        }
        if(!pvp.getPlayerOneCards().contains(req.getCardId()) && !pvp.getPlayerTwoCards().contains(req.getCardId())){
            throw new ApiExceptionHandler("Card not found in this game");
        }
        if(pvp.getTurn().equalsIgnoreCase("p1")){
            if(req.getStat()==null){
                pvp.setPlayerTwoLastCard(req.getCardId());
            }else {
                pvp.setPlayerOneLastCard(req.getCardId());
            }
        }else{
            if(req.getStat()==null){
                pvp.setPlayerOneLastCard(req.getCardId());
            }else{
                pvp.setPlayerTwoLastCard(req.getCardId());
            }
        }
        pvp.setStat(req.getStat() != null ? req.getStat() : pvp.getStat());
        pvpRepo.save(pvp);
    }

    @Override
    public PvPDto enquiry(Integer pvpGameId,Integer id) throws JsonProcessingException {
        PvP pvp =pvpRepo.findById(pvpGameId).orElseThrow(() -> new ResourcesNotFoundException("Game", "Id", pvpGameId));
        if(pvp.getPlayerOneLastCard()==null || pvp.getPlayerTwoLastCard()==null){
            return pvpToPvpDto(pvp);
        }
        if(id==2){
            if(pvp.getRoundWinner()==null){
                return pvpToPvpDto(pvp);
            }
            pvp.setOtherPlayerCard(pvp.getPlayerOneLastCard());
            PvPDto pvPDto=pvpToPvpDto(pvp);
            pvp.setRoundWinner(null);
            pvp.setPlayerOneLastCard(null);
            pvp.setPlayerTwoLastCard(null);
            pvpRepo.save(pvp);
            return pvPDto;
        }
        String stat=pvp.getStat();
        CardDto playerOneCard=cardService.getCard(pvp.getPlayerOneLastCard());
        CardDto playerTwoCard=cardService.getCard(pvp.getPlayerTwoLastCard());

        pvp.setOtherPlayerCard(playerTwoCard.getCardId());

        Integer winner =0;
        switch (stat) {
            case "speed":
                winner=getWinnerId(Integer.valueOf(playerOneCard.getSpeed()),Integer.valueOf(playerTwoCard.getSpeed()));
                break;
            case "combat" :
                winner=getWinnerId(Integer.valueOf(playerOneCard.getCombat()),Integer.valueOf(playerTwoCard.getCombat()));
                break;
            case "chakra" :
                winner=getWinnerId(Integer.valueOf(playerOneCard.getChakra()),Integer.valueOf(playerTwoCard.getChakra()));
                break;
            case "jutsu" :
                winner=getWinnerId(Integer.valueOf(playerOneCard.getJutsu()),Integer.valueOf(playerTwoCard.getJutsu()));
                break;
            case "intel" :
                winner=getWinnerId(Integer.valueOf(playerOneCard.getIntel()),Integer.valueOf(playerTwoCard.getIntel()));
                break;
            case "regen" :
                winner=getWinnerId(Integer.valueOf(playerOneCard.getRegen()),Integer.valueOf(playerTwoCard.getRegen()));
                break;
        }

        List<Integer> playerOneCards=pvp.getPlayerOneCards();
        playerOneCards.remove(Integer.valueOf(playerOneCard.getCardId()));
        List<Integer> playerTwoCards=pvp.getPlayerTwoCards();
        playerTwoCards.remove(Integer.valueOf(playerTwoCard.getCardId()));
        pvp.setPlayerOneCards(playerOneCards);
        pvp.setPlayerTwoCards(playerTwoCards);

        if(winner==1){
            pvp.setRoundWinner("p1");
            pvp.setTurn("p2");
            pvp.setPlayerOneScore(pvp.getPlayerOneScore()+1);
        }else if(winner==2){
            pvp.setRoundWinner("p2");
            pvp.setTurn("p1");
            pvp.setPlayerTwoScore(pvp.getPlayerTwoScore()+1);
        }else{
            pvp.setRoundWinner("draw");
            pvp.setPlayerOneScore(pvp.getPlayerOneScore()+1);
            pvp.setPlayerTwoScore(pvp.getPlayerTwoScore()+1);
        }

        if(playerOneWins(pvp)){
            User playerOne = userRepo.findById(pvp.getPlayerOneId()).orElseThrow(() -> new ResourcesNotFoundException("User", "Id", pvp.getPlayerOneId()));
            User playerTwo = userRepo.findById(pvp.getPlayerTwoId()).orElseThrow(() -> new ResourcesNotFoundException("User", "Id", pvp.getPlayerTwoId()));
            playerOne.setMatches(playerOne.getMatches()+1);
            playerOne.setWins(playerOne.getWins()+1);
            playerOne.setWinStreak(playerOne.getWinStreak()+1);
            playerTwo.setMatches(playerTwo.getMatches()+1);
            playerTwo.setLosses(playerTwo.getLosses()+1);
            playerTwo.setWinStreak(0);
            pvp.setWinner("p1");
            pvp.setRewardCard(pvp.getPlayerTwoRewardCard());

            List<Integer> playerTwoOriginalCardList=playerTwo.getCards();
            List<Integer> playerTwoDistinctCards=playerTwoOriginalCardList.stream()
                    .distinct()
                    .toList();
            List<Integer> playerOneOriginalCardList=playerOne.getCards();
            playerOneOriginalCardList.add(pvp.getPlayerTwoRewardCard());
            if(playerTwoDistinctCards.size()>7){
                playerTwoOriginalCardList.remove(Integer.valueOf(pvp.getPlayerTwoRewardCard()));
                playerTwo.setNoOfCards(playerTwoOriginalCardList.size());
                playerTwo.setCards(playerTwoOriginalCardList);
            }
            playerOne.setNoOfCards(playerOneOriginalCardList.size());
            playerOne.setCards(playerOneOriginalCardList);
            userRepo.save(playerOne);
            userRepo.save(playerTwo);
        }else if(playerTwoWins(pvp)){
            User playerOne = userRepo.findById(pvp.getPlayerOneId()).orElseThrow(() -> new ResourcesNotFoundException("User", "Id", pvp.getPlayerOneId()));
            User playerTwo = userRepo.findById(pvp.getPlayerTwoId()).orElseThrow(() -> new ResourcesNotFoundException("User", "Id", pvp.getPlayerTwoId()));
            playerOne.setMatches(playerOne.getMatches()+1);
            playerOne.setLosses(playerOne.getLosses()+1);
            playerOne.setWinStreak(0);
            playerTwo.setMatches(playerTwo.getMatches()+1);
            playerTwo.setWins(playerTwo.getWins()+1);
            playerTwo.setWinStreak(playerTwo.getWinStreak()+1);
            pvp.setWinner("p2");
            pvp.setRewardCard(pvp.getPlayerOneRewardCard());

            List<Integer> playerTwoOriginalCardList=playerTwo.getCards();
            playerTwoOriginalCardList.add(pvp.getPlayerOneRewardCard());
            List<Integer> playerOneOriginalCardList=playerOne.getCards();
            List<Integer> playerOneDistinctCards=playerOneOriginalCardList.stream()
                    .distinct()
                    .toList();
            if(playerOneDistinctCards.size()>7){
                playerOneOriginalCardList.remove(Integer.valueOf(pvp.getPlayerOneRewardCard()));
                playerOne.setNoOfCards(playerOneOriginalCardList.size());
                playerOne.setCards(playerOneOriginalCardList);
            }
            playerTwo.setCards(playerTwoOriginalCardList);
            playerTwo.setNoOfCards(playerTwoOriginalCardList.size());
            userRepo.save(playerOne);
            userRepo.save(playerTwo);
        }

        if(playerOneCards.isEmpty() && (Objects.equals(pvp.getPlayerOneScore(), pvp.getPlayerTwoScore()))){
            User playerOne = userRepo.findById(pvp.getPlayerOneId()).orElseThrow(() -> new ResourcesNotFoundException("User", "Id", pvp.getPlayerOneId()));
            User playerTwo = userRepo.findById(pvp.getPlayerTwoId()).orElseThrow(() -> new ResourcesNotFoundException("User", "Id", pvp.getPlayerTwoId()));
            playerOne.setMatches(playerOne.getMatches()+1);
            playerOne.setDraws(playerOne.getDraws()+1);
            playerTwo.setMatches(playerTwo.getMatches()+1);
            playerTwo.setDraws(playerTwo.getDraws()+1);
            pvp.setWinner("draw");
            userRepo.save(playerOne);
            userRepo.save(playerTwo);
        }

        pvpRepo.save(pvp);
        return pvpToPvpDto(pvp);
    }

    PvPDto pvpToPvpDto(PvP pvp){
        PvPDto pvpDto=new PvPDto();
        pvpDto.setPvpGameId(pvp.getPvpGameId());
        pvpDto.setPvpGameSize(pvp.getPvpGameSize());
        pvpDto.setWinner(pvp.getWinner());
        pvpDto.setTurn(pvp.getTurn());
        pvpDto.setStat(pvp.getStat());
        pvpDto.setPlayerOneId(pvp.getPlayerOneId());
        pvpDto.setPlayerTwoId(pvp.getPlayerTwoId());
        pvpDto.setPlayerTwoId(pvp.getPlayerTwoId());
        pvpDto.setPlayerTwoCards(playerCardDtoList(pvp.getPlayerTwoCards()));
        pvpDto.setPlayerOneCards(playerCardDtoList(pvp.getPlayerOneCards()));
        pvpDto.setPlayerOneScore(pvp.getPlayerOneScore());
        pvpDto.setPlayerTwoScore(pvp.getPlayerTwoScore());
        pvpDto.setPlayerOneName(pvp.getPlayerOneName());
        pvpDto.setPlayerTwoName(pvp.getPlayerTwoName());
        pvpDto.setRoundWinner(pvp.getRoundWinner());
        pvpDto.setPlayerOneRewardCard(pvp.getPlayerOneRewardCard());
        pvpDto.setPlayerTwoRewardCard(pvp.getPlayerTwoRewardCard());
        if(pvp.getRewardCard()!=null){
            pvpDto.setRewardCard(cardService.getCard(pvp.getRewardCard()));
        }
        if(pvp.getPlayerOneLastCard()!=null){
            pvpDto.setPlayerOneLastCard(cardService.getCard(pvp.getPlayerOneLastCard()));
        }
        if(pvp.getPlayerTwoLastCard() !=null){
            pvpDto.setPlayerTwoLastCard(cardService.getCard(pvp.getPlayerTwoLastCard()));
        }
        if(pvp.getOtherPlayerCard() != null){
            pvpDto.setOtherPlayerCard(cardService.getCard(pvp.getOtherPlayerCard()));
        }
        return pvpDto;
    }

    PvP pvpDtoToPvP(PvPDto pvpDto) throws JsonProcessingException {
        PvP pvp=new PvP();
        pvp.setPvpGameId(pvpDto.getPvpGameId());
        pvp.setPvpGameSize(pvpDto.getPvpGameSize());
        pvp.setWinner(pvpDto.getWinner());
        pvp.setTurn(pvpDto.getTurn());
        pvp.setStat(pvpDto.getStat());
        pvp.setPlayerOneId(pvpDto.getPlayerOneId());
        pvp.setPlayerTwoId(pvpDto.getPlayerTwoId());
        pvp.setPlayerTwoId(pvpDto.getPlayerTwoId());
        pvp.setPlayerTwoCards(playerCards(pvpDto.getPlayerTwoCards()));
        pvp.setPlayerOneCards(playerCards(pvpDto.getPlayerOneCards()));
        pvp.setPlayerOneScore(pvpDto.getPlayerOneScore());
        pvp.setPlayerTwoScore(pvpDto.getPlayerTwoScore());
        pvp.setPlayerOneName(pvpDto.getPlayerOneName());
        pvp.setPlayerTwoName(pvpDto.getPlayerTwoName());
        pvp.setPlayerOneRewardCard(pvpDto.getPlayerOneRewardCard());
        pvp.setPlayerTwoRewardCard(pvpDto.getPlayerTwoRewardCard());
        if(pvpDto.getRewardCard()!=null){
            pvp.setRewardCard(pvpDto.getRewardCard().getCardId());
        }
        if(pvpDto.getPlayerOneLastCard() !=null){
            pvp.setPlayerOneLastCard(pvpDto.getPlayerOneLastCard().getCardId());
        }
        if(pvpDto.getPlayerTwoLastCard() !=null){
            pvp.setPlayerTwoLastCard(pvpDto.getPlayerTwoLastCard().getCardId());
        }
        if(pvpDto.getOtherPlayerCard()!=null){
            pvp.setOtherPlayerCard(pvpDto.getOtherPlayerCard().getCardId());
        }
        pvp.setRoundWinner(pvpDto.getRoundWinner());
        return pvp;
    }

    private List<Integer> playerCards(List<CardDto> cardDtos) {
        return cardDtos.stream().map(CardDto::getCardId).toList();
    }

    private List<CardDto> playerCardDtoList(List<Integer> cards) {
        return cards.stream()
                .map(cardId -> cardRepo.findById(cardId).orElse(null))
                .map(card -> modelMapper.map(card, CardDto.class))
                .toList();
    }

    private Integer getWinnerId(Integer playerOneStat,Integer playerTwoStat){
        if(playerOneStat>playerTwoStat){
            return 1;
        }else if(playerOneStat<playerTwoStat){
            return 2;
        }else {
            return 0;
        }
    }

    private boolean playerOneWins(PvP pvp){
        if(pvp.getPlayerTwoScore()>=pvp.getPlayerOneScore()){
            return false;
        }
        int movesLeft=pvp.getPlayerOneCards().size();
        int diff=pvp.getPlayerOneScore()-pvp.getPlayerTwoScore();
        return diff > movesLeft;
    }

    private boolean playerTwoWins(PvP pvp){
        if(pvp.getPlayerOneScore()>=pvp.getPlayerTwoScore()){
            return false;
        }
        int movesLeft=pvp.getPlayerTwoCards().size();
        int diff=pvp.getPlayerTwoScore()-pvp.getPlayerOneScore();
        return diff > movesLeft;
    }

}