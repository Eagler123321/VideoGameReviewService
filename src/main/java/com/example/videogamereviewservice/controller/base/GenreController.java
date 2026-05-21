package com.example.videogamereviewservice.controller.base;

import com.example.videogamereviewservice.dto.request.base.GenreRequestDto;
import com.example.videogamereviewservice.dto.response.GenreResponseDto;
import com.example.videogamereviewservice.service.local.GenreServiceLocal;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "/genres")
public class GenreController {
    private final GenreServiceLocal genreServiceLocal;

    public GenreController(GenreServiceLocal genreServiceLocal) {
        this.genreServiceLocal = genreServiceLocal;
    }

    @PostMapping
    public ResponseEntity<GenreResponseDto> createGenre(@Valid @RequestBody GenreRequestDto genreRequestDto){
        return ResponseEntity.status(HttpStatus.CREATED).body(genreServiceLocal.createGenre(genreRequestDto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<GenreResponseDto> updateGenreById(@Valid @RequestBody GenreRequestDto genreRequestDto, @PathVariable Long id){
        return ResponseEntity.status(HttpStatus.OK).body(genreServiceLocal.updateGenreById(genreRequestDto, id));
    }
    @GetMapping("/{id}")
    public ResponseEntity<GenreResponseDto> getGenreById(@PathVariable Long id){
        return ResponseEntity.status(HttpStatus.OK).body(genreServiceLocal.getGenreById(id));
    }
    @GetMapping
    public ResponseEntity<List<GenreResponseDto>> getGenres(){
        return ResponseEntity.status(HttpStatus.OK).body(genreServiceLocal.getGenres());
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteGenreById(@PathVariable Long id){
        genreServiceLocal.deleteGenreById(id);
        return ResponseEntity.noContent().build();
    }
}
