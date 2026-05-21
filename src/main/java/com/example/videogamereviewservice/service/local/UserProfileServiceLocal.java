package com.example.videogamereviewservice.service.local;

import com.example.videogamereviewservice.dto.request.contract.UserPasswordDto;
import com.example.videogamereviewservice.dto.request.contract.UserProfileDto;
import com.example.videogamereviewservice.dto.response.UserResponseDto;
import com.example.videogamereviewservice.entity.User;
import com.example.videogamereviewservice.error.NotFoundException;
import com.example.videogamereviewservice.mapper.UserProfileMapper;
import com.example.videogamereviewservice.repository.UserRepository;
import com.example.videogamereviewservice.security.jwt.JwtServiceLocal;
import com.example.videogamereviewservice.service.noImp.UserProfileService;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class UserProfileServiceLocal implements UserProfileService {
    private final UserRepository userRepository;
    private final UserProfileMapper userProfileMapper;
    private final JwtServiceLocal jwtServiceLocal;
    private final PasswordEncoder passwordEncoder;


    public UserProfileServiceLocal(UserRepository userRepository, UserProfileMapper userProfileMapper, JwtServiceLocal jwtServiceLocal, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.userProfileMapper = userProfileMapper;
        this.jwtServiceLocal = jwtServiceLocal;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @Transactional
    public UserResponseDto updateUserProfileData(UserProfileDto userProfileDto, Long id) {
        log.debug("User profile is being updated with id {}", id);

        User user = userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("User not found with id " + id));

        userProfileMapper.updateUserFromDto(userProfileDto, user);

        log.info("User was updated with nickname {} and id {}", userProfileDto.getNickname(), id);

        return userProfileMapper.toDto(user);
    }

    @Override
    @Transactional
    public UserResponseDto updateUserProfilePassword(UserPasswordDto userPasswordDto, Long id) {
        log.debug("User password is being updated with id {}", id);

        User user = userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("User not found with id " + id));

        user.setPassword(userPasswordDto.getPassword());

        log.info("User password was updated id {}", id);

        return userProfileMapper.toDto(user);
    }

    @Override
    @Transactional
    public UserResponseDto getUserProfileData(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("User not found with id " + id));

        log.debug("User profile is received with email {}", user.getEmail());

        return userProfileMapper.toDto(user);
    }
}
