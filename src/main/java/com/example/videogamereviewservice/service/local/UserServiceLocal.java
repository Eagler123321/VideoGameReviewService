package com.example.videogamereviewservice.service.local;

import com.example.videogamereviewservice.dto.request.UserRequestDto;
import com.example.videogamereviewservice.dto.response.UserResponseDto;
import com.example.videogamereviewservice.entity.User;
import com.example.videogamereviewservice.error.NotFoundException;
import com.example.videogamereviewservice.mapper.UserMapper;
import com.example.videogamereviewservice.repository.UserRepository;
import com.example.videogamereviewservice.service.noImp.UserService;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Slf4j
public class UserServiceLocal implements UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    public UserServiceLocal(UserMapper userMapper, UserRepository userRepository) {
        this.userMapper = userMapper;
        this.userRepository = userRepository;
    }

    @Override
    @Transactional
    public UserResponseDto createUser(UserRequestDto userRequestDto) {
        User user = userRepository.save(userMapper.toEntity(userRequestDto));

        user.setRegisteredAt(LocalDateTime.now());

        log.info("User is created with email {}", userRequestDto.getEmail());

        return userMapper.toDto(user);
    }

    @Override
    @Transactional
    public UserResponseDto updateUserById(UserRequestDto userRequestDto, Long id) {
        log.debug("User is being updated with id {}", id);

        User user = userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("User not found with id " + id));
        
        userMapper.updateUserFromDto(userRequestDto, user);

        log.info("User was updated with email {} and id {}", userRequestDto.getEmail(), id);

        return userMapper.toDto(user);
    }

    @Override
    @Transactional
    public UserResponseDto getUserById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("User not found with id " + id));

        log.debug("User is received with email {}", user.getEmail());

        return userMapper.toDto(user);
    }

    @Override
    @Transactional
    public List<UserResponseDto> getUsers() {
        log.debug("Receiving all users...");

        List<User> users = userRepository.findAll();

        log.info("All users is received! Count {}", users.size());

        return users.stream()
                .map(userMapper::toDto)
                .toList();
    }

    @Override
    @Transactional
    public void deleteUserById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("User not found with id " + id));
        String email = user.getEmail();

        log.debug("User is being deleted with email {}", email);

        userRepository.deleteById(id);

        log.info("User was deleted with email {}", email);
    }
}
