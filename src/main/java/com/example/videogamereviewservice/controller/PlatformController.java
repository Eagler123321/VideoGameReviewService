package com.example.videogamereviewservice.controller;

import com.example.videogamereviewservice.dto.request.PlatformRequestDto;
import com.example.videogamereviewservice.dto.response.PlatformResponseDto;
import com.example.videogamereviewservice.service.local.PlatformServiceLocal;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "/platforms")
public class PlatformController {
    private final PlatformServiceLocal platformServiceLocal;

    public PlatformController(PlatformServiceLocal platformServiceLocal) {
        this.platformServiceLocal = platformServiceLocal;
    }

    @PostMapping
    public ResponseEntity<PlatformResponseDto> createPlatform(@Valid @RequestBody PlatformRequestDto platformRequestDto){
        return ResponseEntity.status(HttpStatus.CREATED).body(platformServiceLocal.createPlatform(platformRequestDto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<PlatformResponseDto> updatePlatformById(@Valid @RequestBody PlatformRequestDto platformRequestDto, @PathVariable Long id){
        return ResponseEntity.status(HttpStatus.OK).body(platformServiceLocal.updatePlatformById(platformRequestDto, id));
    }
    @GetMapping("/{id}")
    public ResponseEntity<PlatformResponseDto> getPlatformById(@PathVariable Long id){
        return ResponseEntity.status(HttpStatus.OK).body(platformServiceLocal.getPlatformById(id));
    }
    @GetMapping
    public ResponseEntity<List<PlatformResponseDto>> getPlatforms(){
        return ResponseEntity.status(HttpStatus.OK).body(platformServiceLocal.getPlatforms());
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePlatformById(@PathVariable Long id){
        platformServiceLocal.deletePlatformById(id);
        return ResponseEntity.noContent().build();
    }
}
