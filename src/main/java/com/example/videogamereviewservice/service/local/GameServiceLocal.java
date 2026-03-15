package com.example.videogamereviewservice.service.local;

import com.example.videogamereviewservice.dto.request.GameRequestDto;
import com.example.videogamereviewservice.dto.response.GameResponseDto;
import com.example.videogamereviewservice.entity.Game;
import com.example.videogamereviewservice.entity.Genre;
import com.example.videogamereviewservice.entity.Platform;
import com.example.videogamereviewservice.entity.Tag;
import com.example.videogamereviewservice.error.InvalidIdException;
import com.example.videogamereviewservice.error.NotFoundException;
import com.example.videogamereviewservice.mapper.GameMapper;
import com.example.videogamereviewservice.repository.GameRepository;
import com.example.videogamereviewservice.repository.GenreRepository;
import com.example.videogamereviewservice.repository.PlatformRepository;
import com.example.videogamereviewservice.repository.TagRepository;
import com.example.videogamereviewservice.service.noImp.GameService;
import org.springframework.context.annotation.Profile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

@Service
@Slf4j
public class GameServiceLocal implements GameService {
    private final GameRepository gameRepository;
    private final GameMapper gameMapper;
    private final TagRepository tagRepository;
    private final GenreRepository genreRepository;
    private final PlatformRepository platformRepository;

    public GameServiceLocal(GameMapper gameMapper, GameRepository gameRepository, TagRepository tagRepository, GenreRepository genreRepository, PlatformRepository platformRepository) {
        this.gameMapper = gameMapper;
        this.gameRepository = gameRepository;
        this.tagRepository = tagRepository;
        this.genreRepository = genreRepository;
        this.platformRepository = platformRepository;
    }

    @Override
    @Transactional
    public GameResponseDto createGame(GameRequestDto gameRequestDto) {
        // Жёстко, нужно комменты оставить, а то чёрт голову свернёт
        // Reverse mapping
        Game game = gameMapper.toEntity(gameRequestDto);
        // Оставляем только уникальные id, чтобы не было повторов
        gameRequestDto.setTagIds(getUniqueIds(gameRequestDto.getTagIds()));
        gameRequestDto.setGenreIds(getUniqueIds(gameRequestDto.getGenreIds()));
        gameRequestDto.setPlatformIds(getUniqueIds(gameRequestDto.getPlatformIds()));
        // Проверка на несуществующие id
        checkInvalidIds(tagRepository, gameRequestDto.getTagIds(), "Tag");
        checkInvalidIds(platformRepository, gameRequestDto.getPlatformIds(), "Platform");
        checkInvalidIds(genreRepository, gameRequestDto.getGenreIds(), "Genre");
        // Сохраняем, используя явный маппинг id, реализованный в toEntityIds
        Game savedGame = gameRepository.save(toEntityIds(gameRequestDto, game));
        // Записываем в логи
        log.info("Game is created with title {}", gameRequestDto.getTitle());
        // Возвращаем используя явный маппинг id и вшитый Straight mapping в метод toResponseDto
        return toResponseDto(savedGame);
    }

    private List<Long> getUniqueIds(List<Long> ids){
        if (ids == null || ids.isEmpty()) {
            return List.of();
        }

        return ids.stream()
                .filter(Objects::nonNull)
                .distinct()
                .toList();
    }

    private <T> void checkInvalidIds(JpaRepository<T, Long> repository, List<Long> ids, String type) {
        if (ids == null || ids.isEmpty()) {
            return;
        }

        List<T> entities = repository.findAllById(ids);

        if (entities.size() != ids.size()){
            throw new InvalidIdException("Some %s Ids do not exist".formatted(type));
        }
    }

    private Game toEntityIds(GameRequestDto gameRequestDto, Game game) {
        if (gameRequestDto.getTagIds() != null && !gameRequestDto.getTagIds().isEmpty()){
            List<Tag> tags = tagRepository.findAllById(gameRequestDto.getTagIds());
            game.setTags(tags);
        }
        if (gameRequestDto.getGenreIds() != null && !gameRequestDto.getGenreIds().isEmpty()){
            List<Genre> genres = genreRepository.findAllById(gameRequestDto.getGenreIds());
            game.setGenres(genres);
        }
        if (gameRequestDto.getPlatformIds() != null && !gameRequestDto.getPlatformIds().isEmpty()){
            List<Platform> platforms = platformRepository.findAllById(gameRequestDto.getPlatformIds());
            game.setPlatforms(platforms);
        }
        return game;
    }

    private GameResponseDto toResponseDto(Game game){
        GameResponseDto responseDto = gameMapper.toDto(game);

        responseDto.setTagIds(game.getTags().stream().map(Tag::getId).toList());
        responseDto.setGenreIds(game.getGenres().stream().map(Genre::getId).toList());
        responseDto.setPlatformIds(game.getPlatforms().stream().map(Platform::getId).toList());

        return responseDto;
    }

    @Override
    @Transactional
    public GameResponseDto updateGameById(GameRequestDto gameRequestDto, Long id) {
        log.debug("Game was updated with id {}", id);

        Game game = gameRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Game not found with id " + id));

        gameMapper.updateGameFromDto(gameRequestDto, toEntityIds(gameRequestDto, game));

        log.info("Game was updated with title {} and id {}", gameRequestDto.getTitle(), id);
        
        Game savedGame = gameRepository.save(game);

        return toResponseDto(savedGame);
    }

    @Override
    @Transactional(readOnly = true)
    public GameResponseDto getGameById(Long id) {
        Game game = gameRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Game not found with id " + id));

        log.debug("Game is received with id {}", gameRepository.getReferenceById(id).getTitle());

        return toResponseDto(game);
    }

    @Override
    @Transactional(readOnly = true)
    public List<GameResponseDto> getGames() {
        log.debug("Receiving all games...");

        List<Game> reviews = gameRepository.findAll();

        log.info("All games is received! Count {}", reviews.size());

        return reviews.stream()
                .map(this::toResponseDto)
                .toList();
    }

    @Override
    @Transactional
    public void deleteGameById(Long id) {
        Game game = gameRepository.findById(id).orElseThrow(() -> new NotFoundException("Game not found with id " + id));

        String title = game.getTitle();

        log.debug("Game is being deleted with title {}", title);
            
        gameRepository.deleteById(id);

        log.info("Game was deleted with title {}", title);
    }
}
