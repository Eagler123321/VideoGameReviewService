package com.example.videogamereviewservice.service.noImp;

import com.example.videogamereviewservice.dto.request.contract.UserPasswordDto;
import com.example.videogamereviewservice.dto.request.contract.UserProfileDto;
import com.example.videogamereviewservice.dto.response.UserResponseDto;

public interface UserProfileService {
    UserResponseDto updateUserProfileData(UserProfileDto userProfileDto, Long id);

    UserResponseDto getUserProfileData(Long id);

    UserResponseDto updateUserProfilePassword(UserPasswordDto userPasswordDto, Long id);
}
