package com.example.videogamereviewservice.controller.base;

import com.example.videogamereviewservice.dto.request.base.GameRequestDto;
import com.example.videogamereviewservice.dto.response.GameResponseDto;
import com.example.videogamereviewservice.service.local.GameServiceLocal;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "/games")
public class GameController {
    private final GameServiceLocal gameServiceLocal;

    public GameController(GameServiceLocal gameServiceLocal) {
        this.gameServiceLocal = gameServiceLocal;
    }

    @PostMapping
    public ResponseEntity<GameResponseDto> createGame(@Valid @RequestBody GameRequestDto gameRequestDto){
        return ResponseEntity.status(HttpStatus.CREATED).body(gameServiceLocal.createGame(gameRequestDto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<GameResponseDto> updateGameById(@Valid @RequestBody GameRequestDto gameRequestDto, @PathVariable Long id){
        return ResponseEntity.status(HttpStatus.OK).body(gameServiceLocal.updateGameById(gameRequestDto, id));
    }
    @GetMapping("/{id}")
    public ResponseEntity<GameResponseDto> getGameById(@PathVariable Long id){
        return ResponseEntity.status(HttpStatus.OK).body(gameServiceLocal.getGameById(id));
    }
    @GetMapping
    public ResponseEntity<List<GameResponseDto>> getAllGames(){
        return ResponseEntity.status(HttpStatus.OK).body(gameServiceLocal.getGames());
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteGameById(@PathVariable Long id){
        gameServiceLocal.deleteGameById(id);
        return ResponseEntity.noContent().build();
    }
}
