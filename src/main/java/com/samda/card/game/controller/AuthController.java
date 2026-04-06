package com.samda.card.game.controller;


import com.samda.card.game.entity.User;
import com.samda.card.game.exceptions.ApiExceptionHandler;
import com.samda.card.game.payload.JwtAuthRequest;
import com.samda.card.game.payload.JwtAuthResponse;
import com.samda.card.game.payload.UserDto;
import com.samda.card.game.security.JwtTokenHelper;
import com.samda.card.game.service.UserService;
import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/card-game/api/auth/")
public class AuthController {


    @Autowired
    private JwtTokenHelper jwtTokenHelper;

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private UserService userService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private ModelMapper modelMapper;

    @GetMapping("/ping")
    public String ping() {
        System.out.println("Service is running");
        return "ok";
    }

    @PostMapping("/login")//url is /api/auth/login
    public ResponseEntity<JwtAuthResponse> createToken(@RequestBody JwtAuthRequest request) throws Exception{

        this.authenticate(request.getUsername(), request.getPassword());

        UserDetails userDetails = this.userDetailsService.loadUserByUsername(request.getUsername());

        String ourGeneratedToken = this.jwtTokenHelper.generateToken(userDetails);//generateToken takes userDetails
        JwtAuthResponse response = new JwtAuthResponse();
        response.setToken(ourGeneratedToken);
        response.setUser(this.modelMapper.map((User)userDetails, UserDto.class));

        return new ResponseEntity<JwtAuthResponse>(response, HttpStatus.OK);

    }

    private void authenticate(String username, String password) throws Exception {

        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(username, password);
        System.out.println(usernamePasswordAuthenticationToken+"Samda...");

        try {
            this.authenticationManager.authenticate(usernamePasswordAuthenticationToken);

        } catch (BadCredentialsException e) {
            System.out.println("Invalid Username or Password!");
            throw new ApiExceptionHandler("Invalid Username or Password!");
        }
    }

    @PostMapping("/register")
    public ResponseEntity<UserDto> registerNewUser(@RequestBody @Valid UserDto userDto){
        UserDto registeredNewUser = this.userService.registerNewUser(userDto);

        return new ResponseEntity<UserDto>(registeredNewUser, HttpStatus.CREATED);

    }
}

