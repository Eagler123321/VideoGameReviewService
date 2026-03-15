package com.example.videogamereviewservice.service;

import com.example.videogamereviewservice.dto.request.GameRequestDto;
import com.example.videogamereviewservice.dto.response.GameResponseDto;
import com.example.videogamereviewservice.mapper.GameMapper;
import com.example.videogamereviewservice.repository.GameRepository;
import com.example.videogamereviewservice.service.local.GameServiceLocal;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;

@ExtendWith(MockitoExtension.class)
public class GameServiceTest {
    @Mock
    private GameMapper gameMapper;
    @Mock
    private GameRepository gameRepository;
    @InjectMocks
    private GameServiceLocal gameServiceLocal;

    private final Long gameId = 1L;
    private final Long gameId2 = 2L;
    private final String title = "Undertale";
    private final String title2 = "Deltarune";
    private final String description = "cool jrpg game";
    private final String description2 = "by Toby Fox";
    private final String imageUrl = "heart.jpg";
    private final String imageUrl2 = "rune.jpg";
    private final LocalDateTime releaseDate = LocalDateTime.of(2015, 9, 15, 3, 4, 2);
    private final LocalDateTime releaseDate2 = LocalDateTime.of(2018, 10, 31, 2, 2,3);
    private final String developer = "Toby Fox";
    private final String developer2 = "Toby Fox";
    private final String publisher = "Toby Fox";
    private final String publisher2 = "Toby Fox";
    private final List<Long> tagIds = List.of(1L, 2L, 3L);
    private final List<Long> tagIds2 = List.of(1L, 2L, 4L);
    private final List<Long> platformIds = List.of(1L, 2L, 3L, 4L);
    private final List<Long> platformIds2 = List.of(1L, 2L, 3L, 5L);
    private final List<Long> genreIds = List.of(15L);
    private final List<Long> genreIds2 = List.of(15L);
    private GameResponseDto gameResponseDto;
    private GameResponseDto gameResponseDto2;
    private GameRequestDto gameRequestDto;
    private GameRequestDto gameRequestDto2;

    @BeforeEach
    public void init(){
        gameRequestDto = GameRequestDto.builder()
                .title(title)
                .developer(developer)
                .publisher(publisher)
                .imageUrl(imageUrl)
                .releaseDate(releaseDate)
                .tagIds(tagIds)
                .genreIds(genreIds)
                .platformIds(platformIds)
                .description(description)
                .build();

        gameRequestDto2 = GameRequestDto.builder()
                .title(title2)
                .developer(developer2)
                .publisher(publisher2)
                .imageUrl(imageUrl2)
                .releaseDate(releaseDate2)
                .tagIds(tagIds2)
                .genreIds(genreIds2)
                .platformIds(platformIds2)
                .description(description2)
                .build();

        gameResponseDto = GameResponseDto.builder()
                .id(gameId)
                .title(title)
                .developer(developer)
                .publisher(publisher)
                .imageUrl(imageUrl)
                .releaseDate(releaseDate)
                .tagIds(tagIds)
                .genreIds(genreIds)
                .platformIds(platformIds)
                .description(description)
                .build();
        
        gameResponseDto2 = GameResponseDto.builder()
                .id(gameId2)
                .title(title2)
                .developer(developer2)
                .publisher(publisher2)
                .imageUrl(imageUrl2)
                .releaseDate(releaseDate2)
                .tagIds(tagIds2)
                .genreIds(genreIds2)
                .platformIds(platformIds2)
                .description(description2)
                .build();
    }
}
