package com.example.videogamereviewservice.controller.contract;

import com.example.videogamereviewservice.dto.auth.JwtAuthenticationDto;
import com.example.videogamereviewservice.dto.auth.RefreshTokenDto;
import com.example.videogamereviewservice.dto.auth.UserCredentialsDto;
import com.example.videogamereviewservice.dto.request.contract.RegisterRequestDto;
import com.example.videogamereviewservice.dto.request.base.UserRequestDto;
import com.example.videogamereviewservice.dto.response.UserResponseDto;
import com.example.videogamereviewservice.error.ErrorDto;
import com.example.videogamereviewservice.service.local.UserServiceLocal;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.*;

import org.springframework.security.core.AuthenticationException;


@RestController
@RequestMapping("/auth")
@Tag(name = "Authentication", description = "API для регистрации и аутентификации пользователей")
public class AuthController {
    private final UserServiceLocal userServiceLocal;

    public AuthController(UserServiceLocal userServiceLocal) {
        this.userServiceLocal = userServiceLocal;
    }

    @Operation(
            summary = "Регистрация нового пользователя",
            description = "Создаёт нового пользователя в системе и возвращает JWT токены"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Пользователь успешно зарегистрирован"),
            @ApiResponse(responseCode = "400", description = "Невалидные данные (email уже существует, слабый пароль)",
                    content = @Content(schema = @Schema(implementation = ErrorDto.class))),
            @ApiResponse(responseCode = "500", description = "Внутренняя ошибка сервера")
    })
    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody RegisterRequestDto registerRequestDto){
        UserRequestDto userRequestDto = UserRequestDto.builder()
                .username(registerRequestDto.getUsername())
                .email(registerRequestDto.getEmail())
                .password(registerRequestDto.getPassword())
                .build();

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(userServiceLocal.createUser(userRequestDto));
    }

    @Operation(
            summary = "Вход в систему (Login)",
            description = "Проверяет учётные данные и возвращает JWT access и refresh токены"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Successful log-in",
                    content = @Content(schema = @Schema(implementation = JwtAuthenticationDto.class))),
            @ApiResponse(responseCode = "403", description = "Invalid email/username or password",
                    content = @Content(schema = @Schema(implementation = ErrorDto.class))),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")
    })
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody UserCredentialsDto userCredentialsDto){
        try{
            JwtAuthenticationDto jwtAuthenticationDto = userServiceLocal.singIn(userCredentialsDto);
            return ResponseEntity.ok(jwtAuthenticationDto);
        }catch (BadCredentialsException ex) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(new ErrorDto(403, "Forbidden", "Invalid username or password"));
        }
    }

    @Operation(
            summary = "Обновить JWT токен",
            description = "Использует refresh token для получения новой пары access и refresh токенов"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Tokens successfully updated!",
                    content = @Content(schema = @Schema(implementation = JwtAuthenticationDto.class))),
            @ApiResponse(responseCode = "403", description = "Authentication failed! Invalid refresh token",
                    content = @Content(schema = @Schema(implementation = ErrorDto.class))),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")
    })
    @PostMapping("/refresh")
    public ResponseEntity<?> refresh(@RequestBody RefreshTokenDto refreshTokenDto) throws Exception {
        try {
            return ResponseEntity.ok(userServiceLocal.refreshToken(refreshTokenDto));
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(new ErrorDto(403, "Forbidden", "Invalid refresh token"));
        }
    }
/*    @PostMapping("/logout")
    public Void logout(){
        return null;
    }*/
}
