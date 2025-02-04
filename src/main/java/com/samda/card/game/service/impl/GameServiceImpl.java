package com.samda.card.game.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.samda.card.game.entity.Card;
import com.samda.card.game.entity.Game;
import com.samda.card.game.entity.User;
import com.samda.card.game.exceptions.ApiExceptionHandler;
import com.samda.card.game.exceptions.ResourcesNotFoundException;
import com.samda.card.game.payload.*;
import com.samda.card.game.repository.CardRepo;
import com.samda.card.game.repository.GameRepo;
import com.samda.card.game.repository.UserRepo;
import com.samda.card.game.service.GameService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class GameServiceImpl implements GameService {
    @Autowired
    GameRepo gameRepo;

    @Autowired
    UserRepo userRepo;

    @Autowired
    CardRepo cardRepo;

    @Autowired
    ModelMapper modelMapper;

    public static final int  Game_7v7=7;
    public static final int Game_11v11=11;
    public static final int Game_15v15=15;
    public static final int Game_Default=7;

    @Override
    public GameDto createGame(GameDto gameDto) throws JsonProcessingException {
        User player = userRepo.findById(gameDto.getPlayerId()).orElseThrow(() -> new ResourcesNotFoundException("User", "Id", gameDto.getPlayerId()));
        if(player.getGameId()!=null){
            Game checkGame = gameRepo.findById(player.getGameId()).orElse(null);
            if(checkGame !=null && checkGame.getWinner()!=null){
                gameRepo.delete(checkGame);
            }else if(checkGame !=null && checkGame.getWinner()==null){
                return gameToGameDto(checkGame,playerCardDtos(checkGame.getComputerCards()),playerCardDtos(checkGame.getPlayerCards()));
            }
        }

        boolean isCustomGame= gameDto.getGame_type().equalsIgnoreCase("custom");

        List<Integer> playerCards=null;

        if(isCustomGame){
            playerCards=new ArrayList<>(gameDto.getPlayerCards().stream().map(card -> modelMapper.map(card,CardDto.class)).map(CardDto::getCardId).toList());
            Collections.shuffle(playerCards);
        }else{
            int gameSize=getGameSize(gameDto.getGame_type(),player.getCards());
            playerCards= new ArrayList<>(player.getCards().stream()
                    .distinct()
                    .toList());
            if(playerCards.size()<gameSize){
                throw new ApiExceptionHandler("Player Having insufficient cards");
            }
            Collections.shuffle(playerCards);
            playerCards = playerCards.subList(0, gameSize);
        }

        List<Integer> computerCards = generateComputerCards(playerCards);
        Random rand = new Random();
        int randomIndex = rand.nextInt(playerCards.size());
        Integer playerLostCardId=playerCards.get(randomIndex);
        randomIndex = rand.nextInt(computerCards.size());
        Integer computerLostCardId=computerCards.get(randomIndex);

        List<CardDto> computerCardDto = computerCards.stream()
                .map(id -> cardRepo.findById(id).orElse(null))
                .map(card -> modelMapper.map(card, CardDto.class))
                .toList();
        List<CardDto> playerCardDto = playerCards.stream()
                .map(id -> cardRepo.findById(id).orElse(null))
                .map(card -> modelMapper.map(card, CardDto.class))
                .toList();

        gameDto.setComputerCards(computerCardDto);
        gameDto.setPlayerCards(playerCardDto);
        gameDto.setComputerScore(0);
        gameDto.setPlayerScore(0);
        gameDto.setTurn("player");
        gameDto.setPlayerLostCardId(playerLostCardId);
        gameDto.setComputerLostCardId(computerLostCardId);
        gameDto.setWinChestNumber(playerCards.size()/5);

        Game game = gameDtoToGame(gameDto, computerCards, playerCards);
        Game createdGame = gameRepo.save(game);
        player.setGameId(createdGame.getGame_id());
        userRepo.save(player);
        return gameToGameDto(createdGame, computerCardDto, playerCardDto);
    }

    @Override
    public CompareCardResponseDto compareCards(CompareCardDto compareCardDto) throws JsonProcessingException {
        CompareCardResponseDto response=new CompareCardResponseDto();

        Game game = gameRepo.findById(compareCardDto.getGameId()).orElseThrow(() -> new ResourcesNotFoundException("Game", "Id", compareCardDto.getGameId()));
        List<Integer> computerCardList=game.getComputerCards();
        List<Integer> playerCardList=game.getPlayerCards();

        if(!playerCardList.contains(compareCardDto.getPlayerCardId())){
            throw new ApiExceptionHandler("Please Select a valid card !!");
        }

        Card playerCard = cardRepo.findById(compareCardDto.getPlayerCardId()).orElse(null);
        CardDto playerCardDto = modelMapper.map(playerCard, CardDto.class);

        Random rand = new Random();
        int randomIndex = rand.nextInt(game.getComputerCards().size());
        int chance =rand.nextInt(2);
        Integer computerCardId = chance==0 ? game.getComputerCards().get(randomIndex) :  getComputerCardId(game.getComputerCards(),game.getTurn());
        CardDto computerCardDto=modelMapper.map(cardRepo.findById(computerCardId).orElse(null),CardDto.class);
        response.setComputerCard(computerCardDto);

        String stat = null;
        if (compareCardDto.getStat() == null) {
            stat = getStat(computerCardDto);
            response.setStat(stat);
        } else {
            stat = compareCardDto.getStat().toLowerCase();
        }

        Integer winner=null;


        switch (stat) {
            case "speed":
                winner=getWinnerId(Integer.valueOf(computerCardDto.getSpeed()),Integer.valueOf(playerCardDto.getSpeed()));
                break;
            case "combat" :
                winner=getWinnerId(Integer.valueOf(computerCardDto.getCombat()),Integer.valueOf(playerCardDto.getCombat()));
                break;
            case "chakra" :
                winner=getWinnerId(Integer.valueOf(computerCardDto.getChakra()),Integer.valueOf(playerCardDto.getChakra()));
                break;
            case "jutsu" :
                winner=getWinnerId(Integer.valueOf(computerCardDto.getJutsu()),Integer.valueOf(playerCardDto.getJutsu()));
                break;
            case "intel" :
                winner=getWinnerId(Integer.valueOf(computerCardDto.getIntel()),Integer.valueOf(playerCardDto.getIntel()));
                break;
            case "regen" :
                winner=getWinnerId(Integer.valueOf(computerCardDto.getRegen()),Integer.valueOf(playerCardDto.getRegen()));
                break;
        }

        computerCardList.remove(Integer.valueOf(computerCardDto.getCardId()));
        playerCardList.remove(Integer.valueOf(playerCardDto.getCardId()));

        List<CardDto> computerCardDtoList=playerCardDtos(computerCardList);
        List<CardDto> playerCardDtoList=playerCardDtos(playerCardList);

        game.setComputerCards(computerCardList);
        game.setPlayerCards(playerCardList);

        if(winner !=null && winner==1){
            game.setComputerScore(game.getComputerScore()+1);
            game.setTurn("player");
            response.setRoundWinner("Computer");
        }else if(winner !=null && winner==2){
            game.setPlayerScore(game.getPlayerScore()+1);
            response.setRoundWinner("Player");
            game.setTurn("computer");
        }else{
            game.setComputerScore(game.getComputerScore()+1);
            game.setPlayerScore(game.getPlayerScore()+1);
            response.setRoundWinner("Draw");
        }

        if(game.getWinner()==null && computerWins(game)){
            User player = userRepo.findById(game.getPlayerId()).orElseThrow(() -> new ResourcesNotFoundException("User", "Id", game.getPlayerId()));
            List<Integer> playerOriginalCardList=player.getCards();
            List<Integer> playerDistinctCards=playerOriginalCardList.stream()
                    .distinct()
                    .toList();
            if(playerDistinctCards.size()>7){
                playerOriginalCardList.remove(Integer.valueOf(game.getPlayerLostCardId()));
                Card lostCard=cardRepo.findById(game.getPlayerLostCardId()).orElseThrow(() -> new ResourcesNotFoundException("Card", "Id", game.getPlayerLostCardId()));
                response.setPlayerLostCard(modelMapper.map(lostCard,CardDto.class));
                player.setNoOfCards(player.getNoOfCards()-1);
                player.setCards(playerOriginalCardList);
            }
            player.setLosses(player.getLosses()+1);
            player.setMatches(player.getMatches()+1);
            player.setWinStreak(0);

            userRepo.save(player);
            game.setWinner("Computer");
        }else if(game.getWinner()==null && playerWins(game)){
            User player = userRepo.findById(game.getPlayerId()).orElseThrow(() -> new ResourcesNotFoundException("User", "Id", game.getPlayerId()));
            int chestNo=getChestNo(game.getGame_type());
            player.setChest(player.getChest()+chestNo);
            player.setWins(player.getWins()+1);
            player.setMatches(player.getMatches()+1);
            player.setWinStreak(player.getWinStreak()+1);
            userRepo.save(player);
            game.setWinner("Player");
        }

        if(playerCardList.isEmpty() && Objects.equals(game.getPlayerScore(), game.getComputerScore())){
            User player = userRepo.findById(game.getPlayerId()).orElseThrow(() -> new ResourcesNotFoundException("User", "Id", game.getPlayerId()));
            player.setMatches(player.getMatches()+1);
            player.setDraws(player.getDraws()+1);
            userRepo.save(player);
            game.setWinner("Draw");
        }

        Game savedGame=gameRepo.save(game);
        GameDto gameDto=gameToGameDto(savedGame,computerCardDtoList,playerCardDtoList);
        if(playerCardList.isEmpty()){
            gameRepo.delete(savedGame);
        }
        response.setGame(gameDto);

        return response;
    }

    @Override
    public RewardsDto getRewards(Integer gameId){
        Game game = gameRepo.findById(gameId).orElseThrow(() -> new ResourcesNotFoundException("Game", "Id", gameId));
        RewardsDto rewardsDto=new RewardsDto();
        rewardsDto.setWinner(game.getWinner());
        Card card=null;
        if(game.getWinner().equalsIgnoreCase("player")){
            card=cardRepo.findById(game.getComputerLostCardId()).orElse(null);
        }else if(game.getWinner().equalsIgnoreCase("computer")){
            card=cardRepo.findById(game.getPlayerLostCardId()).orElse(null);
        }
        rewardsDto.setCardDto(modelMapper.map(card,CardDto.class));
        return rewardsDto;
    }

    @Override
    public QuitResponse quitGame(Integer gameId) throws JsonProcessingException {
        QuitResponse response=new QuitResponse();
        Game game = gameRepo.findById(gameId).orElseThrow(() -> new ResourcesNotFoundException("Game", "Id", gameId));
        game.setWinner("Computer");
        game.setComputerScore(7);
        game.setPlayerScore(0);
        User player = userRepo.findById(game.getPlayerId()).orElseThrow(() -> new ResourcesNotFoundException("User", "Id", game.getPlayerId()));
        player.setMatches(player.getMatches()+1);
        player.setLosses(player.getLosses()+1);
        player.setWinStreak(0);
        Integer lostCardId=game.getPlayerLostCardId();
        Card lostCard=cardRepo.findById(lostCardId).orElseThrow(() -> new ResourcesNotFoundException("Card", "Id", lostCardId));
        List<Integer> playerOriginalCardList=player.getCards();
        if(playerOriginalCardList.size()>7){
            playerOriginalCardList.remove(Integer.valueOf(lostCardId));
            response.setPlayerLostCard(modelMapper.map(lostCard,CardDto.class));
            player.setNoOfCards(player.getNoOfCards()-1);
        }
        player.setCards(playerOriginalCardList);

        Game savedGame=gameRepo.save(game);
        User savedUser=userRepo.save(player);

        List<CardDto> computerCardDtoList=playerCardDtos(savedGame.getComputerCards());
        List<CardDto> playerCardDtoList=playerCardDtos(savedGame.getPlayerCards());


        response.setGame(gameToGameDto(savedGame,computerCardDtoList,playerCardDtoList));
        response.setUser(modelMapper.map(savedUser,UserDto.class));

        return response;
    }

    public List<Integer> generateComputerCards(List<Integer> playerCards) {

        Map<String, Long> playerDifferentCardTypeNumber = new HashMap<>();

        for (Integer cardId : playerCards) {
            Optional<Card> card = cardRepo.findById(cardId);
            if (card.isPresent()) {
                String cardType = card.get().getCardType();
                playerDifferentCardTypeNumber.put(cardType, playerDifferentCardTypeNumber.getOrDefault(cardType, 0L) + 1);
            }
        }

        List<Integer> computerCards = new ArrayList<>();

        for (Map.Entry<String, Long> entry : playerDifferentCardTypeNumber.entrySet()) {
            String cardType = entry.getKey();
            long count = entry.getValue();

            List<Integer> validCardIds = cardRepo.findValidCardIdsByCardTypeExcludingList(cardType, playerCards);

            if (validCardIds.size() < count) {
                List<Integer> allCardsOfType =cardRepo.findValidCardIdsByCardTypeExcludingList(cardType, validCardIds);
                Collections.shuffle(allCardsOfType);
                allCardsOfType=allCardsOfType.subList(0, (int) (count-validCardIds.size()));
                validCardIds.addAll(allCardsOfType);
            }

            Collections.shuffle(validCardIds);
            computerCards.addAll(validCardIds.subList(0, (int) count));
        }

        return computerCards;
    }

    private Game gameDtoToGame(GameDto gameDto, List<Integer> computerCards, List<Integer> playerCards) throws JsonProcessingException {
        Game game = new Game();
        game.setGame_id(gameDto.getGame_id());
        game.setGame_type(gameDto.getGame_type());
        game.setPlayerId(gameDto.getPlayerId());
        game.setComputerCards(computerCards);
        game.setPlayerCards(playerCards);
        game.setComputerScore(gameDto.getComputerScore());
        game.setPlayerScore(gameDto.getPlayerScore());
        game.setWinner(gameDto.getWinner());
        game.setPlayerLostCardId(gameDto.getPlayerLostCardId());
        game.setComputerLostCardId(gameDto.getComputerLostCardId());
        game.setTurn(gameDto.getTurn());
        game.setWinChestNumber(gameDto.getWinChestNumber());
        return game;
    }

    private GameDto gameToGameDto(Game game, List<CardDto> computerCardDtos, List<CardDto> playerCardDtos) throws JsonProcessingException {
        GameDto gameDto = new GameDto();
        gameDto.setGame_id(game.getGame_id());
        gameDto.setGame_type(game.getGame_type());
        gameDto.setPlayerId(game.getPlayerId());
        gameDto.setComputerCards(computerCardDtos);
        gameDto.setPlayerCards(playerCardDtos);
        gameDto.setComputerScore(game.getComputerScore());
        gameDto.setPlayerScore(game.getPlayerScore());
        gameDto.setWinner(game.getWinner());
        gameDto.setPlayerLostCardId(game.getPlayerLostCardId());
        gameDto.setComputerLostCardId(game.getComputerLostCardId());
        gameDto.setTurn(game.getTurn());
        gameDto.setWinChestNumber(game.getWinChestNumber());
        return gameDto;
    }

    private List<Integer> playerCards(List<CardDto> cardDtos) {
        List<Integer> cards = cardDtos.stream().map(CardDto::getCardId).toList();
        return cards;
    }

    private List<CardDto> playerCardDtos(List<Integer> cards) {
        List<CardDto> cardDtos = cards.stream()
                .map(cardId -> cardRepo.findById(cardId).orElse(null))
                .map(card -> modelMapper.map(card, CardDto.class))
                .toList();
        return cardDtos;
    }

    private String getStat(CardDto cardDto){
        Map<String, Integer> m = new HashMap<>();
        m.put("speed", Integer.valueOf(cardDto.getSpeed()));
        m.put("combat", Integer.valueOf(cardDto.getCombat()));
        m.put("chakra", Integer.valueOf(cardDto.getChakra()));
        m.put("jutsu", Integer.valueOf(cardDto.getJutsu()));
        m.put("intel", Integer.valueOf(cardDto.getIntel()));
        m.put("regen", Integer.valueOf(cardDto.getRegen()));


        List<Map.Entry<String, Integer>> entryList = new ArrayList<>(m.entrySet());
        entryList.sort((entry1, entry2) -> entry2.getValue().compareTo(entry1.getValue()));

        Random rand = new Random();
        int randomIndex = rand.nextInt(2);

        if(entryList.get(0).getValue()-entryList.get(1).getValue()>3){
            randomIndex=0;
        }

        List<String> result = new ArrayList<>();
        return entryList.get(randomIndex).getKey();
    }

    private int getComputerCardId(List<Integer> computerCards,String turn){
        Map<Integer, Integer> m = new HashMap<>();
        for (Integer computerCard : computerCards) {
            CardDto card = modelMapper.map(cardRepo.findById(computerCard).orElse(null), CardDto.class);
            int val = Math.max(Math.max(Math.max(Math.max(Math.max(Integer.parseInt(card.getSpeed()), Integer.parseInt(card.getCombat())),
                    Integer.parseInt(card.getChakra())), Integer.parseInt(card.getJutsu())), Integer.parseInt(card.getIntel())), Integer.parseInt(card.getRegen()));
            m.put(card.getCardId(), val);
        }
        List<Map.Entry<Integer, Integer>> entryList = new ArrayList<>(m.entrySet());
        entryList.sort((entry1, entry2) -> entry2.getValue().compareTo(entry1.getValue()));
        Random rand = new Random();

        if(turn.equalsIgnoreCase("player")){
            int randomIndex = rand.nextInt(entryList.size()/2 + entryList.size()%2);
            return entryList.get(randomIndex + entryList.size()/2).getKey();
        }else{
            int k=entryList.size()<4 ? 1 : (entryList.size()<6 ? 2 :3);
            int randomIndex = rand.nextInt(k);
            return entryList.get(randomIndex).getKey();
        }
    }

    private Integer getWinnerId(Integer computerStat,Integer playerStat){
        if(computerStat>playerStat){
            return 1;
        }else if(computerStat<playerStat){
            return 2;
        }else {
            return 0;
        }
    }

    boolean computerWins(Game game){
        if(game.getPlayerScore()>=game.getComputerScore()){
            return false;
        }
        int movesLeft=game.getPlayerCards().size();
        int diff=game.getComputerScore()-game.getPlayerScore();
        return diff > movesLeft;
    }

    boolean playerWins(Game game){
        if(game.getPlayerScore()<=game.getComputerScore()){
            return false;
        }
        int movesLeft=game.getPlayerCards().size();
        int diff=game.getPlayerScore()-game.getComputerScore();
        return diff > movesLeft;
    }

    public int getGameSize(String gameType,List<Integer>playerCards){
        if(gameType.equalsIgnoreCase("7v7")){
            return Game_7v7;
        }else if(gameType.equalsIgnoreCase(("11v11"))){
            return Game_11v11;
        }else if(gameType.equalsIgnoreCase("15v15")){
            return Game_15v15;
        }else if(gameType.equalsIgnoreCase("all")){
            playerCards= new ArrayList<>(playerCards.stream()
                    .distinct()
                    .toList());
            return playerCards.size();
        }else{
            return Game_Default;
        }
    }

    public int getChestNo(String gameType){
        if(gameType.equalsIgnoreCase("7v7")){
            return 1;
        }else if(gameType.equalsIgnoreCase(("11v11"))){
            return 2;
        }else if(gameType.equalsIgnoreCase("15v15")){
            return 3;
        }else{
            return 1;
        }
    }
}
