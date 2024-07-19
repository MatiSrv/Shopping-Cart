package com.cart.shoppingcart.controllers.auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cart.shoppingcart.dtos.user.AuthResponse;
import com.cart.shoppingcart.dtos.user.UserRequestDTO;
import com.cart.shoppingcart.services.user.UserService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/auth")
@CrossOrigin("http://localhost:4200")
public class AuthController {
      @Autowired
    private UserService userService;


    @PostMapping("/register")
    public ResponseEntity<AuthResponse>  register(@RequestBody @Valid UserRequestDTO user) {
        return ResponseEntity.ok(userService.saveUser(user));
    }


    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody @Valid UserRequestDTO credentials) {
        return ResponseEntity.ok(userService.login(credentials));
    }
    
}
