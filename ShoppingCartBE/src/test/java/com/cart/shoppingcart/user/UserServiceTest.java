package com.cart.shoppingcart.user;

import com.cart.shoppingcart.dtos.user.AuthResponse;
import com.cart.shoppingcart.dtos.user.UserRequestDTO;
import com.cart.shoppingcart.entities.user.Role;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.cart.shoppingcart.entities.user.UserEntity;
import com.cart.shoppingcart.repositories.user.UserJpaRepository;
import com.cart.shoppingcart.services.jwt.JwtService;
import com.cart.shoppingcart.services.user.UserService;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;


public class UserServiceTest {

    @Mock
    private UserJpaRepository userJpaRepository;
   

    @Mock
    private PasswordEncoder passwordEncoder;

    
    @Mock
    private AuthenticationManager authenticationManager;
    @InjectMocks
    private UserService userService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void saveUser() {
        UserRequestDTO userRequestDTO = new UserRequestDTO("email@email", "1234");
        UserEntity userEntity = new UserEntity(1L, Role.USER, "email@email", "1234");

        when(userJpaRepository.findByEmail(userRequestDTO.getEmail())).thenReturn(Optional.empty());
        when(passwordEncoder.encode(userRequestDTO.getPassword())).thenReturn("EncodedPassword");
        when(userJpaRepository.save(any(UserEntity.class))).thenReturn(userEntity);

        AuthResponse response = userService.saveUser(userRequestDTO);

        assertNotNull(response);

    }

    @Test
    public void saveExistingUser() {
        UserRequestDTO userRequestDTO = new UserRequestDTO("email@email", "1234");
        UserEntity userEntity = new UserEntity(1L, Role.USER, "email@email", "1234");

        when(userJpaRepository.findByEmail(userRequestDTO.getEmail())).thenReturn(Optional.of(userEntity));
        when(passwordEncoder.encode(userRequestDTO.getPassword())).thenReturn("EncodedPassword");
        when(userJpaRepository.save(any(UserEntity.class))).thenReturn(userEntity);
       
        assertThrows(RuntimeException.class, () -> userService.saveUser(userRequestDTO));
    }


    }
    

