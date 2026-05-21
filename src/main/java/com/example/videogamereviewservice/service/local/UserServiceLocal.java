package com.example.videogamereviewservice.service.local;

import com.example.videogamereviewservice.dto.auth.JwtAuthenticationDto;
import com.example.videogamereviewservice.dto.auth.RefreshTokenDto;
import com.example.videogamereviewservice.dto.auth.UserCredentialsDto;
import com.example.videogamereviewservice.dto.request.base.UserRequestDto;
import com.example.videogamereviewservice.dto.response.UserResponseDto;
import com.example.videogamereviewservice.entity.User;
import com.example.videogamereviewservice.error.NotFoundException;
import com.example.videogamereviewservice.mapper.UserMapper;
import com.example.videogamereviewservice.repository.UserRepository;
import com.example.videogamereviewservice.security.jwt.JwtServiceLocal;
import com.example.videogamereviewservice.service.generator.NicknameGenerator;
import com.example.videogamereviewservice.service.noImp.UserService;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.authentication.BadCredentialsException;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class UserServiceLocal implements UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final JwtServiceLocal jwtServiceLocal;
    private final PasswordEncoder passwordEncoder;
    private final NicknameGenerator nicknameGenerator;

    public UserServiceLocal(UserMapper userMapper, UserRepository userRepository, JwtServiceLocal jwtServiceLocal, PasswordEncoder passwordEncoder, NicknameGenerator nicknameGenerator) {
        this.userMapper = userMapper;
        this.userRepository = userRepository;
        this.jwtServiceLocal = jwtServiceLocal;
        this.passwordEncoder = passwordEncoder;
        this.nicknameGenerator = nicknameGenerator;
    }

    @Override
    public JwtAuthenticationDto singIn(UserCredentialsDto userCredentialsDto) throws AuthenticationException{
        User user = findByCredentials(userCredentialsDto);
        
        return jwtServiceLocal.generateAuthToken(user.getUsername());
    }

    @Override
    public JwtAuthenticationDto refreshToken(RefreshTokenDto refreshTokenDto) throws Exception{
        String refreshToken = refreshTokenDto.getRefreshToken();
        if (refreshToken != null && jwtServiceLocal.validateJwtToken(refreshToken)){
            User user = findByUsername(jwtServiceLocal.getUsernameFromToken(refreshToken));
            return jwtServiceLocal.refreshBaseToken(user.getUsername(), refreshToken);
        }
        throw new BadCredentialsException("Invalid refresh token");
    }

    @Override
    @Transactional
    public UserResponseDto createUser(UserRequestDto userRequestDto) {
        User user = userMapper.toEntity(userRequestDto);
        user.setRole("USER");
        // проверяем и присваиваем никнейм
        if (user.getNickname() == null || user.getNickname().isBlank()){
            String nickname = nicknameGenerator.generateUnique("random", userRepository);
            user.setNickname(nickname);
            log.debug("Generated nickname: {} for user {}", nickname, user.getUsername());
        }

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        User savedUser = userRepository.save(user);

        log.info("User is created with email {}", userRequestDto.getEmail());

        return userMapper.toDto(savedUser);
    }

    @Override
    @Transactional
    public UserResponseDto updateUserById(UserRequestDto userRequestDto, Long id) {
        log.debug("User is being updated with id {}", id);

        User user = userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("User not found with id " + id));

        userMapper.updateUserFromDto(userRequestDto, user);

        user.setPassword(passwordEncoder.encode(user.getPassword()));

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

    public User findUserByEmail(String email) throws Exception {
        return userRepository.findByEmail(email).orElseThrow(() -> new Exception(String.format("User with email %s not found", email)));
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
    private User findByCredentials(UserCredentialsDto userCredentialsDto) throws AuthenticationException{
        Optional<User> optionalUser = userRepository.findByUsername(userCredentialsDto.getUsername());
        if (optionalUser.isPresent()){
            User user = optionalUser.get();
            if (passwordEncoder.matches(userCredentialsDto.getPassword(), user.getPassword())){
                return user;
            }
        }
        throw new BadCredentialsException("Username or password is not correct");
    }
    private User findByUsername(String username) throws Exception{
        return userRepository.findByUsername(username).orElseThrow(() -> new Exception(String.format("User with username %s not found", username)));
    }
}
