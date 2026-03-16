package com.example.videogamereviewservice.service.local;

import com.example.videogamereviewservice.dto.request.UserRequestDto;
import com.example.videogamereviewservice.dto.response.UserResponseDto;
import com.example.videogamereviewservice.entity.User;
import com.example.videogamereviewservice.mapper.UserMapper;
import com.example.videogamereviewservice.repository.UserRepository;
import org.assertj.core.api.AssertionsForInterfaceTypes;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {
    @Mock
    private UserMapper userMapper;
    @Mock
    private UserRepository userRepository;
    @InjectMocks
    private UserServiceLocal userServiceLocal;

    private User user;
    private User user2;
    private final Long userId = 1L;
    private final Long userId2 = 2L;
    private final String nickname = "Eagler";
    private final String nickname2 = "Decker";
    private final String username = "krol-makarol1488";
    private final String username2 = "lobada-rola228";
    private final String password = "jsuuvje123kai";
    private final String password2 = "asx2ojkaosx124";
    private final String email = "kirilka321@gmail.com";
    private final String email2 = "kira164@mail.ru";
    private final String avatarUrl = "sacefx.jpg";
    private final String avatarUrl2 = "dfscgkicj.jpg";
    private final LocalDateTime registeredAt = LocalDateTime.of(2025, 3, 16, 10, 35, 15);
    private final LocalDateTime registeredAt2 = LocalDateTime.of(2024, 1, 24, 1, 24, 54);
    private final String role = "USER";
    private final String role2 = "USER";
    private final String description = "Cool bruh";
    private final String description2 = "Fool bruh";

    private UserResponseDto userResponseDto;
    private UserResponseDto userResponseDto2;

    private UserRequestDto userRequestDto;
    private UserRequestDto userRequestDto2;

    @BeforeEach // ИТОГО 5 тестов
    public void init(){
        userResponseDto = UserResponseDto.builder()
                .id(userId)
                .nickname(nickname)
                .role(role)
                .email(email)
                .registeredAt(registeredAt)
                .avatarUrl(avatarUrl)
                .description(description)
                .build();

        userResponseDto2 = UserResponseDto.builder()
                .id(userId2)
                .nickname(nickname2)
                .role(role2)
                .email(email2)
                .registeredAt(registeredAt2)
                .avatarUrl(avatarUrl2)
                .description(description2)
                .build();

        userRequestDto = UserRequestDto.builder()
                .nickname(nickname)
                .role(role)
                .email(email)
                .password(password)
                .username(username)
                .avatarUrl(avatarUrl)
                .description(description)
                .build();

        userRequestDto2 = UserRequestDto.builder()
                .nickname(nickname2)
                .role(role2)
                .email(email2)
                .password(password2)
                .username(username2)
                .avatarUrl(avatarUrl2)
                .description(description2)
                .build();

        user = User.builder()
                .id(userId)
                .password(password)
                .username(username)
                .email(email)
                .nickname(nickname)
                .avatarUrl(avatarUrl)
                .description(description)
                .registeredAt(registeredAt)
                .role(role)
                .build();
        user2 = User.builder()
                .id(userId2)
                .password(password2)
                .username(username2)
                .email(email2)
                .nickname(nickname2)
                .avatarUrl(avatarUrl2)
                .description(description2)
                .registeredAt(registeredAt2)
                .role(role2)
                .build();
    }

    private void assertionsThat(UserResponseDto savedUser) {
        assertThat(savedUser).isNotNull();
        assertThat(savedUser.getEmail()).isEqualTo(email);
        assertThat(savedUser.getRole()).isEqualTo(role);
        assertThat(savedUser.getNickname()).isEqualTo(nickname);
        assertThat(savedUser.getAvatarUrl()).isEqualTo(avatarUrl);
        assertThat(savedUser.getDescription()).isEqualTo(description);
        assertThat(savedUser.getRegisteredAt()).isEqualTo(registeredAt);
    }

    @Test
    public void createUser_whenValidRequest_thenReturnsSavedUser(){
        when(userMapper.toEntity(userRequestDto)).thenReturn(user);
        when(userRepository.save(any(User.class))).thenReturn(user);
        when(userMapper.toDto(user)).thenReturn(userResponseDto);

        UserResponseDto savedUser = userServiceLocal.createUser(userRequestDto);

        assertionsThat(savedUser);
    }
    
    @Test
    public void getUserById_whenExists_thenReturnsUser(){
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(userMapper.toDto(any(User.class))).thenReturn(userResponseDto);

        UserResponseDto savedUser = userServiceLocal.getUserById(userId);

        assertionsThat(savedUser);
    }

    @Test
    public void getUsers_whenListNotEmpty_thenReturnsUsers(){
        List<User> users = List.of(user, user2);

        when(userRepository.findAll()).thenReturn(users);
        when(userMapper.toDto(any(User.class))).thenAnswer(invocation -> {
            User t = invocation.getArgument(0);
            return switch ((int) (long) t.getId()) {
                case 1 -> userResponseDto;
                case 2 -> userResponseDto2;
                default -> throw new RuntimeException("Unknown user id: " + t.getId());
            };
        });

        List<UserResponseDto> result = userServiceLocal.getUsers();

        AssertionsForInterfaceTypes.assertThat(result).isNotNull();
        AssertionsForInterfaceTypes.assertThat(result).hasSize(2);
        AssertionsForInterfaceTypes.assertThat(result).containsExactlyInAnyOrder(userResponseDto, userResponseDto2);
    }

    @Test
    public void deleteUserById_whenExists_thenReturnsDoesNotThrow(){
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        userServiceLocal.deleteUserById(userId);

        verify(userRepository).deleteById(userId);
    }

    @Test
    public void updateUserById_whenValidRequest_thenReturnsUpdatedUser(){
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        Mockito.lenient().doNothing().when(userMapper).updateUserFromDto(any(UserRequestDto.class), any(User.class));

        Mockito.lenient().when(userMapper.toDto(any(User.class))).thenReturn(userResponseDto);

        UserResponseDto savedUser = userServiceLocal.updateUserById(userRequestDto, userId);

        assertThat(savedUser).isNotNull();
        assertionsThat(savedUser);

        verify(userMapper).updateUserFromDto(any(UserRequestDto.class), any(User.class));
        verify(userMapper).toDto(any(User.class));
    }
}
