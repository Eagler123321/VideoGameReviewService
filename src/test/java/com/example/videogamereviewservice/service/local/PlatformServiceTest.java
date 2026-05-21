package com.example.videogamereviewservice.service.local;

import com.example.videogamereviewservice.dto.request.base.PlatformRequestDto;
import com.example.videogamereviewservice.dto.response.PlatformResponseDto;
import com.example.videogamereviewservice.entity.Platform;
import com.example.videogamereviewservice.error.NotFoundException;
import com.example.videogamereviewservice.mapper.PlatformMapper;
import com.example.videogamereviewservice.repository.PlatformRepository;
import org.assertj.core.api.AssertionsForInterfaceTypes;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class PlatformServiceTest {
    @Mock
    private PlatformMapper platformMapper;
    @Mock
    private PlatformRepository platformRepository;
    @InjectMocks
    private PlatformServiceLocal platformServiceLocal;

    private Platform platform;
    private Platform platform2;

    private PlatformResponseDto platformResponseDto;
    private PlatformResponseDto platformResponseDto2;

    private PlatformRequestDto platformRequestDto;
    private PlatformRequestDto platformRequestDto2;

    private final Long platformId = 1L;
    private final Long platformId2 = 2L;

    private final String name = "cat";
    private final String name2 = "dog";

    @BeforeEach // ИТОГО 9 тестов
    public void init(){
        platformResponseDto = PlatformResponseDto.builder().id(platformId).name(name).build();
        platformResponseDto2 = PlatformResponseDto.builder().id(platformId2).name(name2).build();

        platformRequestDto = PlatformRequestDto.builder().name(name).build();
        platformRequestDto2 = PlatformRequestDto.builder().name(name2).build();

        platform = Platform.builder().id(platformId).name(name).build();
        platform2 = Platform.builder().id(platformId2).name(name2).build();
    }

    @Test
    public void createPlatform_whenValidRequest_thenReturnsSavedPlatform(){
        when(platformMapper.toEntity(platformRequestDto)).thenReturn(platform);
        when(platformRepository.save(any(Platform.class))).thenReturn(platform);
        when(platformMapper.toDto(platform)).thenReturn(platformResponseDto);

        PlatformResponseDto savedPlatform = platformServiceLocal.createPlatform(platformRequestDto);

        assertThat(savedPlatform).isNotNull();
        assertThat(savedPlatform.getName()).isEqualTo(name);
    }

    @Test
    public void getPlatformById_whenExists_thenReturnsPlatform(){
        when(platformRepository.findById(platformId)).thenReturn(Optional.of(platform));
        when(platformMapper.toDto(any(Platform.class))).thenReturn(platformResponseDto);

        PlatformResponseDto savedPlatform = platformServiceLocal.getPlatformById(platformId);

        assertThat(savedPlatform).isNotNull();
        assertThat(savedPlatform.getName()).isEqualTo(name);
    }

    @Test
    public void getPlatformById_whenNotFound_thenThrowNotFoundException(){
        when(platformRepository.findById(999L)).thenReturn(Optional.empty());

        NotFoundException ex = assertThrows(NotFoundException.class,  () -> platformServiceLocal.getPlatformById(999L));

        assertThat(ex.getMessage()).contains("999");
    }

    @Test
    public void getPlatforms_whenListNotEmpty_thenReturnsPlatforms(){
        List<Platform> platforms = List.of(platform, platform2);

        when(platformRepository.findAll()).thenReturn(platforms);
        when(platformMapper.toDto(any(Platform.class))).thenAnswer(invocation -> {
            Platform t = invocation.getArgument(0);
            return switch ((int) (long) t.getId()) {
                case 1 -> platformResponseDto;
                case 2 -> platformResponseDto2;
                default -> throw new RuntimeException("Unknown platform id: " + t.getId());
            };
        });

        List<PlatformResponseDto> result = platformServiceLocal.getPlatforms();

        AssertionsForInterfaceTypes.assertThat(result).isNotNull();
        AssertionsForInterfaceTypes.assertThat(result).hasSize(2);
        AssertionsForInterfaceTypes.assertThat(result).containsExactlyInAnyOrder(platformResponseDto, platformResponseDto2);
    }

    @Test
    public void getPlatforms_whenListIsEmpty_thenReturnsEmptyList(){
        when(platformRepository.findAll()).thenReturn(List.of());

        List<PlatformResponseDto> result = platformServiceLocal.getPlatforms();

        AssertionsForInterfaceTypes.assertThat(result).isEmpty();
    }

    @Test
    public void deletePlatformById_whenExists_thenReturnsDoesNotThrow(){
        when(platformRepository.findById(platformId)).thenReturn(Optional.of(platform));

        platformServiceLocal.deletePlatformById(platformId);

        verify(platformRepository).deleteById(platformId);
    }

    @Test
    public void deletePlatformById_whenNotFound_thenReturnsDoesNotThrow(){
        when(platformRepository.findById(platformId)).thenReturn(Optional.of(platform));

        platformServiceLocal.deletePlatformById(platformId);

        verify(platformRepository).deleteById(platformId);
    }

    @Test
    public void updatePlatformById_whenValidRequest_thenReturnsUpdatedPlatform(){
        when(platformRepository.findById(platformId)).thenReturn(Optional.of(platform));

        Mockito.lenient().doNothing().when(platformMapper).updatePlatformFromDto(any(PlatformRequestDto.class), any(Platform.class));

        Mockito.lenient().when(platformMapper.toDto(any(Platform.class))).thenReturn(platformResponseDto);

        PlatformResponseDto savedPlatform = platformServiceLocal.updatePlatformById(platformRequestDto, platformId);

        assertThat(savedPlatform).isNotNull();
        assertThat(savedPlatform.getName()).isEqualTo(name);

        verify(platformMapper).updatePlatformFromDto(any(PlatformRequestDto.class), any(Platform.class));
        verify(platformMapper).toDto(any(Platform.class));
    }

    @Test
    public void updatePlatformById_whenNotFound_thenThrowNotFoundException(){
        when(platformRepository.findById(999L)).thenReturn(Optional.empty());

        NotFoundException ex = assertThrows(NotFoundException.class,  () -> platformServiceLocal.updatePlatformById(platformRequestDto, 999L));

        assertThat(ex.getMessage()).contains("999");
    }
}
