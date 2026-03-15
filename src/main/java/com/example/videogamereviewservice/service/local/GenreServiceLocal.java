package com.example.videogamereviewservice.service.local;

import com.example.videogamereviewservice.dto.request.GenreRequestDto;
import com.example.videogamereviewservice.dto.response.GenreResponseDto;
import com.example.videogamereviewservice.entity.Genre;
import com.example.videogamereviewservice.error.NotFoundException;
import com.example.videogamereviewservice.mapper.GenreMapper;
import com.example.videogamereviewservice.repository.GenreRepository;
import com.example.videogamereviewservice.service.noImp.GenreService;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class GenreServiceLocal implements GenreService {
    private final GenreRepository genreRepository;
    private final GenreMapper genreMapper;

    public GenreServiceLocal(GenreMapper genreMapper, GenreRepository genreRepository) {
        this.genreMapper = genreMapper;
        this.genreRepository = genreRepository;
    }

    @Override
    @Transactional
    public GenreResponseDto createGenre(GenreRequestDto genreRequestDto) {
        Genre genre = genreRepository.save(genreMapper.toEntity(genreRequestDto));

        log.info("Genre is created with name {}", genreRequestDto.getName());

        return genreMapper.toDto(genre);
    }

    @Override
    @Transactional
    public GenreResponseDto updateGenreById(GenreRequestDto genreRequestDto, Long id) {
        log.debug("Genre is being updated with id {}", id);

        Genre genre = genreRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Genre not found with id " + id));

        genreMapper.updateGenreFromDto(genreRequestDto, genre);

        log.info("Genre was updated with name {} and id {}", genreRequestDto.getName(), id);

        return genreMapper.toDto(genre);
    }

    @Override
    @Transactional
    public GenreResponseDto getGenreById(Long id) {
        Genre genre = genreRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Genre not found with id " + id));

        log.debug("Genre is received with name {}", genre.getName());

        return genreMapper.toDto(genre);
    }

    @Override
    @Transactional
    public List<GenreResponseDto> getGenres() {
        log.debug("Receiving all genres...");

        List<Genre> genres = genreRepository.findAll();

        log.info("All genres is received! Count {}", genres.size());

        return genres.stream()
                .map(genreMapper::toDto)
                .toList();
    }

    @Override
    @Transactional
    public void deleteGenreById(Long id) {
        Genre genre = genreRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Genre not found with id " + id));
        String name = genre.getName();

        log.debug("Genre is being deleted with name {}", name);

        genreRepository.deleteById(id);

        log.info("Genre was deleted with name {}", name);
    }
}
