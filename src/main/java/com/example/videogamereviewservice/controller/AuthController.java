package com.example.videogamereviewservice.controller;

import com.example.videogamereviewservice.service.local.AuthServiceLocal;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/auth")
public class AuthController {
    private final AuthServiceLocal authServiceLocal;

    public AuthController(AuthServiceLocal authServiceLocal) {
        this.authServiceLocal = authServiceLocal;
    }
    // return statement is ResponseEntity<TokenPair>
    @PostMapping("/register")
    public Void register(){
        return null;
    }
    @PostMapping("/login")
    public Void login(){
        return null;
    }
    @PostMapping("/refresh")
    public Void refresh(){
        return null;
    }
    @PostMapping("/logout")
    public Void logout(){
        return null;
    }
}
