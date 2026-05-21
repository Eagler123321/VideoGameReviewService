package com.example.videogamereviewservice.controller.contract;

import com.example.videogamereviewservice.dto.request.contract.UserPasswordDto;
import com.example.videogamereviewservice.dto.request.contract.UserProfileDto;
import com.example.videogamereviewservice.dto.response.UserResponseDto;
import com.example.videogamereviewservice.security.CustomUserDetails;
import com.example.videogamereviewservice.service.local.UserProfileServiceLocal;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/me")
@Tag(name = "UserProfile", description = "API для работы пользователя со своими данными")
public class UserProfileController {
    private final UserProfileServiceLocal userProfileServiceLocal;

    public UserProfileController(UserProfileServiceLocal userProfileServiceLocal) {
        this.userProfileServiceLocal = userProfileServiceLocal;
    }

    @PutMapping("/profile")
    public ResponseEntity<UserResponseDto> updateUserProfile(@RequestBody @Valid UserProfileDto userProfileDto,
                                                             @AuthenticationPrincipal CustomUserDetails customUserDetails){
        return ResponseEntity.status(HttpStatus.OK).body(userProfileServiceLocal.updateUserProfileData(userProfileDto, customUserDetails.user().getId()));
    }
    @PutMapping("/password")
    public ResponseEntity<UserResponseDto> updateUserPassword(@RequestBody @Valid UserPasswordDto userPasswordDto,
                                                             @AuthenticationPrincipal CustomUserDetails customUserDetails){
        return ResponseEntity.status(HttpStatus.OK).body(userProfileServiceLocal.updateUserProfilePassword(userPasswordDto, customUserDetails.user().getId()));
    }
    @GetMapping
    public ResponseEntity<UserResponseDto> getUserProfile(@AuthenticationPrincipal CustomUserDetails customUserDetails){
        return ResponseEntity.status(HttpStatus.OK).body(userProfileServiceLocal.getUserProfileData(customUserDetails.user().getId()));
    }
}
