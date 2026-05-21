package com.example.videogamereviewservice.integration.service.local;

import com.example.videogamereviewservice.annotations.IT;
import com.example.videogamereviewservice.dto.request.base.PlatformRequestDto;
import com.example.videogamereviewservice.dto.response.PlatformResponseDto;
import com.example.videogamereviewservice.error.NotFoundException;
import com.example.videogamereviewservice.service.local.PlatformServiceLocal;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

@IT
@Transactional
public class PlatformServiceLocalIT {
    @Autowired
    private PlatformServiceLocal platformServiceLocal;

    private static PlatformRequestDto createPlatformRequestDto(String name) {
        return PlatformRequestDto.builder()
                .name(name)
                .build();
    }

    @Test
    void createPlatform_whenValidData_thenReturnsSavedPlatform() {
        PlatformRequestDto requestDto = createPlatformRequestDto("Android");

        PlatformResponseDto savedPlatform = platformServiceLocal.createPlatform(requestDto);

        assertThat(savedPlatform).isNotNull();
        assertThat(savedPlatform.getId()).isNotNull();
        assertThat(savedPlatform.getName()).isEqualTo("Android");
    }

    @Test
    void getPlatformById_whenNotFound_thenThrowsException() {
        assertThatThrownBy(() -> platformServiceLocal.getPlatformById(999L))
                .isInstanceOf(NotFoundException.class);
    }
}
