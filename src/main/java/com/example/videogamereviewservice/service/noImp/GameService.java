package com.example.videogamereviewservice.service.noImp;

import com.example.videogamereviewservice.dto.request.base.GameRequestDto;
import com.example.videogamereviewservice.dto.response.GameResponseDto;

import java.util.List;

public interface GameService {
    GameResponseDto createGame(GameRequestDto gameRequestDto);

    GameResponseDto updateGameById(GameRequestDto gameRequestDto, Long id);

    GameResponseDto getGameById(Long id);

    List<GameResponseDto> getGames();

    void deleteGameById(Long id);
}
