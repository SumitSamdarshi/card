package com.samda.card.game.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.samda.card.game.payload.*;
import com.samda.card.game.service.CardService;
import com.samda.card.game.service.FileService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

@RestController
@RequestMapping("/card-game/api/cards")
public class CardController {
    @Autowired
    CardService cardService;

    @Autowired
    FileService fileService;

    @Value("${project.image}")
    private String uploadPath;

    @PostMapping("/add")
    public ResponseEntity<CardDto> addCard(@Valid @RequestBody CardDto cardDto){
        CardDto createdCard=cardService.addCard(cardDto);
        return new ResponseEntity<>(createdCard, HttpStatus.CREATED);
    }

    @GetMapping("/{card_id}")
    public ResponseEntity<CardDto> addCard(@PathVariable("card_id") Integer id){
        CardDto card=cardService.getCard(id);
        return new ResponseEntity<>(card, HttpStatus.OK);
    }

    @PostMapping("/update/{card_id}")
    public ResponseEntity<CardDto> updateCard(@RequestBody CardDto cardDto, @PathVariable("card_id") Integer id){
        CardDto updatedCard=cardService.updateCard(cardDto,id);
        return new ResponseEntity<>(updatedCard, HttpStatus.OK);
    }

    @GetMapping("/")
    public ResponseEntity<List<CardDto>> getAllCard(){
        List<CardDto> cardDtos=cardService.getAllCard();
        return new ResponseEntity<>(cardDtos, HttpStatus.OK);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<CardDto>> getAllCardForUser(@PathVariable("userId") Integer userId){
        List<CardDto> cardDtos=cardService.getAllCardForUser(userId);
        return new ResponseEntity<>(cardDtos, HttpStatus.OK);
    }

    @GetMapping("/type/{cardType}")
    public ResponseEntity<List<CardDto>> getCardByType(@PathVariable("cardType") String cardType){
        List<CardDto> cardDtos=cardService.getCardByType(cardType);
        return new ResponseEntity<>(cardDtos, HttpStatus.OK);
    }

    @GetMapping("/user/{userId}/{cardType}")
    public ResponseEntity<List<CardDto>> getUserCardByType(@PathVariable("userId") Integer userId,@PathVariable("cardType") String cardType){
        List<CardDto> cardDtos=cardService.getUserCardByType(userId,cardType);
        return new ResponseEntity<>(cardDtos, HttpStatus.OK);
    }

    @DeleteMapping("/delete/{card_id}")
    public ResponseEntity<ApiResponse> deleteCard(@PathVariable("card_id") Integer id){
        cardService.deleteCard(id);
        return new ResponseEntity<>(new ApiResponse("Card deleted Successfully", true), HttpStatus.OK);
    }

    @PostMapping("/draw")
    public ResponseEntity<DrawCardDto> drawCard(@RequestBody UserDto userDto){
        DrawCardDto drawCardDto=cardService.drawCard(userDto);
        return new ResponseEntity<>(drawCardDto,HttpStatus.OK);
    }

    @PostMapping("/image/upload/card/{cardId}")
    public ResponseEntity<CardDto> uploadImage(@RequestParam MultipartFile cardImage, @PathVariable Integer cardId) throws IOException {
        CardDto cardDto = cardService.getCard(cardId);
        String fileName = fileService.uploadImage(uploadPath, cardImage);
        cardDto.setCardImage(fileName);
        CardDto updatedCard=cardService.updateCard(cardDto,cardId);
        return new ResponseEntity<>(updatedCard,HttpStatus.OK);
    }

    @GetMapping(value="/image/card/{imageName}",produces = MediaType.IMAGE_JPEG_VALUE)
    public void viewImage(@PathVariable String imageName, HttpServletResponse response) throws IOException{
        InputStream resource = this.fileService.getResource(uploadPath, imageName);
        response.setContentType(MediaType.IMAGE_JPEG_VALUE);
        StreamUtils.copy(resource,response.getOutputStream());
    }

    @PostMapping("/combine")
    public ResponseEntity<DrawCardDto> combineCard (@RequestBody CombineCardRequestDto combineCardRequestDto) throws JsonProcessingException {
        DrawCardDto drawCardDto= cardService.combineCard(combineCardRequestDto);
        return new ResponseEntity<>(drawCardDto, HttpStatus.OK);
    }
}
