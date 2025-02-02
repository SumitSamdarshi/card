package com.samda.card.game.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.samda.card.game.payload.*;
import com.samda.card.game.service.PvPService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/card-game/api/pvp")
public class PvPController {
    @Autowired
    PvPService pvpService;

    @PostMapping("/create")
    public ResponseEntity<PvPDto> createPvP(@RequestBody PvPDto pvpDto) throws JsonProcessingException {
        PvPDto pvp=pvpService.createPvP(pvpDto);
        return  new ResponseEntity<>(pvp, HttpStatus.CREATED);
    }

    @PostMapping("/join")
    public ResponseEntity<PvPDto> joinPvP(@RequestBody PvPDto pvpDto) throws JsonProcessingException {
        PvPDto pvp=pvpService.joinPvP(pvpDto);
        return  new ResponseEntity<>(pvp, HttpStatus.OK);
    }

    @PostMapping("/submit")
    public ResponseEntity<ApiResponse> submitCard(@RequestBody SubmitCardRequestDto requestDto) throws JsonProcessingException {
        pvpService.submitCard(requestDto);
        return  new ResponseEntity<>(new ApiResponse("Card submited successfully",true), HttpStatus.OK);
    }

    @GetMapping("/enquiry/{gameId}/{id}")
    public ResponseEntity<PvPDto> enquiry(@PathVariable Integer gameId, @PathVariable Integer id) throws JsonProcessingException {
        PvPDto response= pvpService.enquiry(gameId,id);
        return new ResponseEntity<>(response,HttpStatus.OK);
    }
}
