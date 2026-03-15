package com.example.videogamereviewservice.service;

import com.example.videogamereviewservice.dto.request.GenreRequestDto;
import com.example.videogamereviewservice.dto.response.GenreResponseDto;
import com.example.videogamereviewservice.entity.Genre;
import com.example.videogamereviewservice.mapper.GenreMapper;
import com.example.videogamereviewservice.repository.GenreRepository;
import com.example.videogamereviewservice.service.local.GenreServiceLocal;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.Mockito;

@ExtendWith(MockitoExtension.class)
public class GenreServiceTest {
    @Mock
    private GenreMapper genreMapper;
    @Mock
    private GenreRepository genreRepository;
    @InjectMocks
    private GenreServiceLocal genreServiceLocal;

    private Genre genre;
    private Genre genre2;

    private GenreResponseDto genreResponseDto;
    private GenreResponseDto genreResponseDto2;

    private GenreRequestDto genreRequestDto;
    private GenreRequestDto genreRequestDto2;

    private final Long genreId = 1L;
    private final Long genreId2 = 2L;

    private final String name = "cat";
    private final String name2 = "dog";

    @BeforeEach // ИТОГО 5 тестов
    public void init(){
        genreResponseDto = GenreResponseDto.builder().id(genreId).name(name).build();
        genreResponseDto2 = GenreResponseDto.builder().id(genreId2).name(name2).build();

        genreRequestDto = GenreRequestDto.builder().name(name).build();
        genreRequestDto2 = GenreRequestDto.builder().name(name2).build();

        genre = Genre.builder().id(genreId).name(name).build();
        genre2 = Genre.builder().id(genreId2).name(name2).build();
    }

    @Test
    public void CreateGenreById_(){
        when(genreMapper.toEntity(genreRequestDto)).thenReturn(genre);
        when(genreRepository.save(any(Genre.class))).thenReturn(genre);
        when(genreMapper.toDto(genre)).thenReturn(genreResponseDto);

        GenreResponseDto savedGenre = genreServiceLocal.createGenre(genreRequestDto);

        assertThat(savedGenre).isNotNull();
        assertThat(savedGenre.getName()).isEqualTo(name);
    }

    @Test
    public void GetGenreById_(){
        when(genreRepository.findById(genreId)).thenReturn(Optional.of(genre));
        when(genreMapper.toDto(any(Genre.class))).thenReturn(genreResponseDto);

        GenreResponseDto savedGenre = genreServiceLocal.getGenreById(genreId);

        assertThat(savedGenre).isNotNull();
        assertThat(savedGenre.getName()).isEqualTo(name);
    }

    @Test
    public void GetGenres_(){
        List<Genre> genres = List.of(genre, genre2);

        when(genreRepository.findAll()).thenReturn(genres);
        when(genreMapper.toDto(any(Genre.class))).thenAnswer(invocation -> {
            Genre g = invocation.getArgument(0);
            return switch ((int) (long) g.getId()) {
                case 1 -> genreResponseDto;
                case 2 -> genreResponseDto2;
                default -> throw new RuntimeException("Unknown genre id: " + g.getId());
            };
        });

        List<GenreResponseDto> result = genreServiceLocal.getGenres();

        assertThat(result).isNotNull();
        assertThat(result).hasSize(2);
        assertThat(result).containsExactlyInAnyOrder(genreResponseDto, genreResponseDto2);
    }

    @Test
    public void DeleteGenreById_(){
        when(genreRepository.findById(genreId)).thenReturn(Optional.of(genre));

        genreServiceLocal.deleteGenreById(genreId);

        verify(genreRepository).deleteById(genreId);
    }

    @Test
    public void UpdateGenreById_(){
        when(genreRepository.findById(genreId)).thenReturn(Optional.of(genre));

        Mockito.lenient().doNothing().when(genreMapper).updateGenreFromDto(any(GenreRequestDto.class), any(Genre.class));

        Mockito.lenient().when(genreMapper.toDto(any(Genre.class))).thenReturn(genreResponseDto);

        GenreResponseDto savedGenre = genreServiceLocal.updateGenreById(genreRequestDto, genreId);

        assertThat(savedGenre).isNotNull();
        assertThat(savedGenre.getName()).isEqualTo(name);

        verify(genreMapper).updateGenreFromDto(any(GenreRequestDto.class), any(Genre.class));
        verify(genreMapper).toDto(any(Genre.class));
    }
}