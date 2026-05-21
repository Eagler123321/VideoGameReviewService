package com.example.videogamereviewservice.integration.service.local;

import com.example.videogamereviewservice.annotations.IT;
import com.example.videogamereviewservice.dto.request.base.GenreRequestDto;
import com.example.videogamereviewservice.dto.response.GenreResponseDto;
import com.example.videogamereviewservice.error.NotFoundException;
import com.example.videogamereviewservice.service.local.GenreServiceLocal;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

@IT
@Transactional
public class GenreServiceLocalIT {
    @Autowired
    private GenreServiceLocal genreServiceLocal;

    private static GenreRequestDto createGenreRequestDto(String name) {
        return GenreRequestDto.builder()
                .name(name)
                .build();
    }

    @Test
    void createGenre_whenValidData_thenReturnsSavedGenre() {
        GenreRequestDto requestDto = createGenreRequestDto("RPG");

        GenreResponseDto savedGenre = genreServiceLocal.createGenre(requestDto);

        assertThat(savedGenre).isNotNull();
        assertThat(savedGenre.getId()).isNotNull();
        assertThat(savedGenre.getName()).isEqualTo("RPG");
    }

    @Test
    void getGenreById_whenNotFound_thenThrowsException() {
        assertThatThrownBy(() -> genreServiceLocal.getGenreById(999L))
                .isInstanceOf(NotFoundException.class);
    }
}
