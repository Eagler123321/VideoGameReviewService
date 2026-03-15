package com.example.videogamereviewservice.service.noImp;

import com.example.videogamereviewservice.dto.request.GenreRequestDto;
import com.example.videogamereviewservice.dto.response.GenreResponseDto;

import java.util.List;

public interface GenreService {
    GenreResponseDto createGenre(GenreRequestDto genreRequestDto);

    GenreResponseDto updateGenreById(GenreRequestDto genreRequestDto, Long id);

    GenreResponseDto getGenreById(Long id);

    List<GenreResponseDto> getGenres();

    void deleteGenreById(Long id);
}
