package com.example.videogamereviewservice.integration.service.local;

import com.example.videogamereviewservice.annotations.IT;
import com.example.videogamereviewservice.dto.request.GameRequestDto;
import com.example.videogamereviewservice.dto.response.GameResponseDto;
import com.example.videogamereviewservice.error.NotFoundException;
import com.example.videogamereviewservice.service.local.GameServiceLocal;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

@IT
@Transactional
class GameServiceLocalIT {

    @Autowired
    private GameServiceLocal gameServiceLocal;

    private static GameRequestDto createGameRequestDto(String title) {
        return GameRequestDto.builder()
                .title(title)
                .description("cool jrpg game")
                .developer("Toby Fox")
                .publisher("Toby Fox")
                .imageUrl("heart.jpg")
                .releaseDate(LocalDateTime.of(2015, 9, 15, 3, 4, 2))
                .tagIds(List.of())
                .genreIds(List.of())
                .platformIds(List.of())
                .build();
    }

    @Test
    void createGame_whenValidData_thenReturnsSavedGame() {
        GameRequestDto requestDto = createGameRequestDto("Undertale");

        GameResponseDto savedGame = gameServiceLocal.createGame(requestDto);

        assertThat(savedGame).isNotNull();
        assertThat(savedGame.getId()).isNotNull();
        assertThat(savedGame.getTitle()).isEqualTo("Undertale");
        assertThat(savedGame.getDeveloper()).isEqualTo("Toby Fox");
    }

    @Test
    void getGameById_whenNotFound_thenThrowsException() {
        assertThatThrownBy(() -> gameServiceLocal.getGameById(999L))
                .isInstanceOf(NotFoundException.class);
    }
}