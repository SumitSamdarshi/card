package com.samda.card.game.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.samda.card.game.payload.ApiResponse;
import com.samda.card.game.payload.AssignCardResponse;
import com.samda.card.game.payload.UserDto;
import com.samda.card.game.service.FileService;
import com.samda.card.game.service.UserService;
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

@RestController
@RequestMapping("/card-game/api/users")
public class UserController {
    @Autowired
    UserService userService;

    @Autowired
    FileService fileService;

    @Value("${project.image}")
    private String uploadPath;

    @PostMapping("/create")
    public ResponseEntity<UserDto> createUser(@Valid @RequestBody UserDto userDto) {
        UserDto createdUser =userService.createUser(userDto);
        return new ResponseEntity<>(createdUser, HttpStatus.CREATED);
    }

    @PostMapping("/update/{userId}")
    public ResponseEntity<UserDto> updateUser(@RequestBody UserDto userDto,@PathVariable("userId") Integer id) {
        UserDto updateUser =userService.updateUser(userDto,id);
        return new ResponseEntity<>(updateUser, HttpStatus.OK);
    }

    @DeleteMapping("/delete/{userId}")
    public ResponseEntity<ApiResponse> createUser(@PathVariable("userId") Integer id) {
        userService.deleteUser(id);
        return new ResponseEntity<>(new ApiResponse("User deleted Successfully", true), HttpStatus.OK);
    }

    @GetMapping("/{userId}")
    public ResponseEntity<UserDto> getUser(@PathVariable("userId") Integer id) {
        UserDto user =userService.getUserById(id);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @GetMapping("/assign/{userId}")
    public ResponseEntity<AssignCardResponse> assignFirstCards(@PathVariable("userId") Integer id) throws JsonProcessingException {
        AssignCardResponse response=userService.assignFirstCards(id);
        return new ResponseEntity<>(response,HttpStatus.OK);
    }

    @PostMapping("/image/upload/{userId}")
    public ResponseEntity<UserDto> uploadImage(@RequestParam MultipartFile userImage, @PathVariable Integer userId) throws IOException {
        UserDto userDto=userService.getUserById(userId);
        String fileName = fileService.uploadImage(uploadPath, userImage);
        userDto.setProfileImage(fileName);
        UserDto updatedUser=userService.updateUser(userDto,userId);
        return new ResponseEntity<>(updatedUser,HttpStatus.OK);
    }

    @GetMapping(value="/image/{imageName}",produces = MediaType.IMAGE_JPEG_VALUE)
    public void viewImage(@PathVariable String imageName, HttpServletResponse response) throws IOException{
        InputStream resource = this.fileService.getResource(uploadPath, imageName);
        response.setContentType(MediaType.IMAGE_JPEG_VALUE);
        StreamUtils.copy(resource,response.getOutputStream());

    }
}
