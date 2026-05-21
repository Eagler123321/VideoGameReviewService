package com.example.videogamereviewservice.service.noImp;

import com.example.videogamereviewservice.dto.request.base.PlatformRequestDto;
import com.example.videogamereviewservice.dto.response.PlatformResponseDto;

import java.util.List;

public interface PlatformService {
    PlatformResponseDto createPlatform(PlatformRequestDto platformRequestDto);

    PlatformResponseDto updatePlatformById(PlatformRequestDto platformRequestDto, Long id);

    PlatformResponseDto getPlatformById(Long id);

    List<PlatformResponseDto> getPlatforms();

    void deletePlatformById(Long id);
}
