package com.cart.shoppingcart.services.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.cart.shoppingcart.dtos.user.AuthResponse;
import com.cart.shoppingcart.dtos.user.UserRequestDTO;
import com.cart.shoppingcart.entities.user.Role;
import com.cart.shoppingcart.entities.user.UserEntity;
import com.cart.shoppingcart.repositories.user.UserJpaRepository;
import com.cart.shoppingcart.services.jwt.JwtService;

@Service
public class UserService {
    
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private UserJpaRepository userJpaRepository;
    @Autowired
    private AuthenticationManager authenticationManager;



    public AuthResponse saveUser(UserRequestDTO user) {

        UserEntity userEntity = getEntity(user);
    
        return AuthResponse.builder().token(JwtService.getToken(userEntity,userEntity.getId())).build();

    }

    public AuthResponse login(UserRequestDTO credentials){
             authenticationManager.authenticate((new UsernamePasswordAuthenticationToken(credentials.getEmail(), credentials.getPassword())));
        UserDetails userDetails = userJpaRepository.findByEmail(credentials.getEmail()).get();
        UserEntity userEntity = userJpaRepository.findByEmail(credentials.getEmail()).get();
        String token = JwtService.getToken(userDetails,userEntity.getId());
        return AuthResponse.builder().token(token).build();
    }


    private UserEntity getEntity(UserRequestDTO user) {


        userJpaRepository.findByEmail(user.getEmail()).ifPresent(u -> {
            throw new RuntimeException("User already exists");
        });

        UserEntity userEntity = UserEntity.builder()
        .email(user.getEmail())
        .password(passwordEncoder.encode(user.getPassword()))
        .role(Role.USER)
        .build();

        return userJpaRepository.save(userEntity);

    }
}
