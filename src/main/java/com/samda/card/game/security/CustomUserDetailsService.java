package com.samda.card.game.security;

import com.samda.card.game.entity.User;
import com.samda.card.game.exceptions.ResourcesNotFoundException;
import com.samda.card.game.repository.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService{

    @Autowired
    UserRepo userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = this.userRepository.findByEmail(username).orElseThrow(()->new ResourcesNotFoundException("User ", " email: "+username, 0));
        return user;
    }

}
