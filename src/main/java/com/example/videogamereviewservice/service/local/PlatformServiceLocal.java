package com.example.videogamereviewservice.service.local;

import com.example.videogamereviewservice.dto.request.PlatformRequestDto;
import com.example.videogamereviewservice.dto.response.PlatformResponseDto;
import com.example.videogamereviewservice.entity.Platform;
import com.example.videogamereviewservice.error.NotFoundException;
import com.example.videogamereviewservice.mapper.PlatformMapper;
import com.example.videogamereviewservice.repository.PlatformRepository;
import com.example.videogamereviewservice.service.noImp.PlatformService;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class PlatformServiceLocal implements PlatformService {
    private final PlatformRepository platformRepository;
    private final PlatformMapper platformMapper;

    public PlatformServiceLocal(PlatformMapper platformMapper, PlatformRepository platformRepository) {
        this.platformMapper = platformMapper;
        this.platformRepository = platformRepository;
    }

    @Override
    @Transactional
    public PlatformResponseDto createPlatform(PlatformRequestDto platformRequestDto) {
        Platform platform = platformRepository.save(platformMapper.toEntity(platformRequestDto));

        log.info("Platform is created with name {}", platformRequestDto.getName());

        return platformMapper.toDto(platform);
    }

    @Override
    @Transactional
    public PlatformResponseDto updatePlatformById(PlatformRequestDto platformRequestDto, Long id) {
        log.debug("Platform is being updated with id {}", id);

        Platform platform = platformRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Platform not found with id " + id));

        platformMapper.updatePlatformFromDto(platformRequestDto, platform);

        log.info("Platform was updated with name {} and id {}", platformRequestDto.getName(), id);

        return platformMapper.toDto(platform);
    }

    @Override
    @Transactional
    public PlatformResponseDto getPlatformById(Long id) {
        Platform platform = platformRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Platform not found with id " + id));

        log.info("Platform is received with name {}", platform.getName());

        return platformMapper.toDto(platform);
    }

    @Override
    @Transactional
    public List<PlatformResponseDto> getPlatforms() {
        log.debug("Receiving all platforms...");

        List<Platform> platforms = platformRepository.findAll();

        log.info("All platforms is received! Count {}", platforms.size());

        return platforms.stream()
                .map(platformMapper::toDto)
                .toList();
    }

    @Override
    @Transactional
    public void deletePlatformById(Long id) {
        Platform platform = platformRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Platform not found with id " + id));
        String name = platform.getName();

        log.debug("Platform is being deleted with name {}", name);

        platformRepository.deleteById(id);

        log.info("Platform was deleted with name {}", name);
    }
}
