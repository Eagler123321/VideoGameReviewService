package com.example.videogamereviewservice.controller;

import com.example.videogamereviewservice.dto.request.UserRequestDto;
import com.example.videogamereviewservice.dto.response.UserResponseDto;
import com.example.videogamereviewservice.service.local.UserServiceLocal;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "/users")
public class UserController {
    private final UserServiceLocal userServiceLocal;

    public UserController(UserServiceLocal userServiceLocal) {
        this.userServiceLocal = userServiceLocal;
    }

    @PostMapping
    public ResponseEntity<UserResponseDto> createUser(@Valid @RequestBody UserRequestDto userRequestDto){
        return ResponseEntity.status(HttpStatus.CREATED).body(userServiceLocal.createUser(userRequestDto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserResponseDto> updateUserById(@Valid @RequestBody UserRequestDto userRequestDto, @PathVariable Long id){
        return ResponseEntity.status(HttpStatus.OK).body(userServiceLocal.updateUserById(userRequestDto, id));
    }
    @GetMapping("/{id}")
    public ResponseEntity<UserResponseDto> getUserById(@PathVariable Long id){
        return ResponseEntity.status(HttpStatus.OK).body(userServiceLocal.getUserById(id));
    }
    @GetMapping
    public ResponseEntity<List<UserResponseDto>> getAllUsers(){
        return ResponseEntity.status(HttpStatus.OK).body(userServiceLocal.getUsers());
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUserById(@PathVariable Long id){
        userServiceLocal.deleteUserById(id);
        return ResponseEntity.noContent().build();
    }
}
