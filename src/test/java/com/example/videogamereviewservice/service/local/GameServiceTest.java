package com.example.videogamereviewservice.service.local;

import com.example.videogamereviewservice.dto.request.GameRequestDto;
import com.example.videogamereviewservice.dto.request.ReviewRequestDto;
import com.example.videogamereviewservice.dto.response.GameResponseDto;
import com.example.videogamereviewservice.dto.response.ReviewResponseDto;
import com.example.videogamereviewservice.entity.*;
import com.example.videogamereviewservice.mapper.GameMapper;
import com.example.videogamereviewservice.repository.GameRepository;
import com.example.videogamereviewservice.repository.GenreRepository;
import com.example.videogamereviewservice.repository.PlatformRepository;
import com.example.videogamereviewservice.repository.TagRepository;
import org.assertj.core.api.AssertionsForInterfaceTypes;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class GameServiceTest {
    @Mock
    private GameMapper gameMapper;
    @Mock
    private GameRepository gameRepository;
    @Mock
    private GenreRepository genreRepository;
    @Mock
    private TagRepository tagRepository;
    @Mock
    private PlatformRepository platformRepository;
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
    List<Tag> tags = List.of(Tag.builder().id(1L).build(), Tag.builder().id(2L).build(), Tag.builder().id(3L).build());
    List<Genre> genres = List.of(Genre.builder().id(15L).build());
    List<Platform> platforms = List.of(Platform.builder().id(1L).build(), Platform.builder().id(2L).build(), Platform.builder().id(3L).build(), Platform.builder().id(4L).build());
    private Game game;
    private Game game2;

    @BeforeEach
    public void init(){
        game = Game.builder()
                .id(gameId)
                .publisher(publisher)
                .imageUrl(imageUrl)
                .releaseDate(releaseDate)
                .description(description)
                .developer(developer)
                .build();

        game2 = Game.builder()
                .id(gameId2)
                .publisher(publisher2)
                .imageUrl(imageUrl2)
                .releaseDate(releaseDate2)
                .description(description2)
                .title(title2)
                .developer(developer2)
                .build();

        game.setGenres(new ArrayList<>(genres));
        game.setTags(new ArrayList<>(tags));
        game.setPlatforms(new ArrayList<>(platforms));

        game2.setGenres(new ArrayList<>(genres));
        game2.setTags(new ArrayList<>(tags));
        game2.setPlatforms(new ArrayList<>(platforms));

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

    private void assertsThat(GameResponseDto savedGame) {
        assertThat(savedGame.getTitle()).isEqualTo(title);
        assertThat(savedGame.getDescription()).isEqualTo(description);
        assertThat(savedGame.getDeveloper()).isEqualTo(developer);
        assertThat(savedGame.getPublisher()).isEqualTo(publisher);
        assertThat(savedGame.getImageUrl()).isEqualTo(imageUrl);
        assertThat(savedGame.getReleaseDate()).isEqualTo(releaseDate);
        assertThat(savedGame.getGenreIds()).isEqualTo(genreIds);
        assertThat(savedGame.getPlatformIds()).isEqualTo(platformIds);
        assertThat(savedGame.getTagIds()).isEqualTo(tagIds);
    }

    @Test
    public void createGame_whenValidRequest_thenReturnsSavedGame(){
        when(gameMapper.toEntity(gameRequestDto)).thenReturn(game);

        when(tagRepository.findAllById(tagIds))
                .thenReturn(List.of(Tag.builder().id(1L).build(), Tag.builder().id(2L).build(), Tag.builder().id(3L).build()));

        when(genreRepository.findAllById(genreIds))
                .thenReturn(List.of(Genre.builder().id(15L).build()));

        when(platformRepository.findAllById(platformIds))
                .thenReturn(List.of(Platform.builder().id(1L).build(), Platform.builder().id(2L).build(), Platform.builder().id(3L).build(), Platform.builder().id(4L).build()));

        when(gameRepository.save(any(Game.class))).thenReturn(game);
        when(gameMapper.toDto(game)).thenReturn(gameResponseDto);

        GameResponseDto savedGame = gameServiceLocal.createGame(gameRequestDto);

        assertThat(savedGame).isNotNull();
        assertsThat(savedGame);

    }

    @Test
    public void getGameById_whenExists_thenReturnsGame(){
        when(gameRepository.findById(gameId)).thenReturn(Optional.of(game));
        when(gameMapper.toDto(any(Game.class))).thenReturn(gameResponseDto);

        GameResponseDto savedGame = gameServiceLocal.getGameById(gameId);

        assertThat(savedGame).isNotNull();
        assertsThat(savedGame);
    }

    @Test
    public void getGames_whenListNotEmpty_thenReturnsGames(){
        List<Game> games = List.of(game, game2);

        when(gameRepository.findAll()).thenReturn(games);
        when(gameMapper.toDto(any(Game.class))).thenAnswer(invocation -> {
            Game t = invocation.getArgument(0);
            return switch ((int) (long) t.getId()) {
                case 1 -> gameResponseDto;
                case 2 -> gameResponseDto2;
                default -> throw new RuntimeException("Unknown game id: " + t.getId());
            };
        });

        List<GameResponseDto> result = gameServiceLocal.getGames();

        AssertionsForInterfaceTypes.assertThat(result).isNotNull();
        AssertionsForInterfaceTypes.assertThat(result).hasSize(2);
        AssertionsForInterfaceTypes.assertThat(result).containsExactlyInAnyOrder(gameResponseDto, gameResponseDto2);
    }

    @Test
    public void deleteGameById_whenExists_thenReturnsDoesNotThrow(){
        when(gameRepository.findById(gameId)).thenReturn(Optional.of(game));

        gameServiceLocal.deleteGameById(gameId);

        verify(gameRepository).deleteById(gameId);
    }

    @Test
    public void updateGameById_whenValidRequest_thenReturnsUpdatedGame(){
        when(gameRepository.findById(gameId)).thenReturn(Optional.of(game));

        Mockito.lenient().doAnswer(invocation -> {
            Game g = invocation.getArgument(1);
            g.setGenres(new ArrayList<>(genres));
            g.setTags(new ArrayList<>(tags));
            g.setPlatforms(new ArrayList<>(platforms));
            return null;
        }).when(gameMapper).updateGameFromDto(any(GameRequestDto.class), any(Game.class));

        Mockito.lenient().when(gameMapper.toDto(any(Game.class))).thenReturn(gameResponseDto);
        when(gameRepository.save(any(Game.class))).thenReturn(game);

        GameResponseDto savedGame = gameServiceLocal.updateGameById(gameRequestDto, gameId);

        assertThat(savedGame).isNotNull();
        assertsThat(savedGame);

        verify(gameMapper).updateGameFromDto(any(GameRequestDto.class), any(Game.class));
        verify(gameMapper).toDto(any(Game.class));
    }
}
