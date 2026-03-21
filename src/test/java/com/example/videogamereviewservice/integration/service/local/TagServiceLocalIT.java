package com.example.videogamereviewservice.integration.service.local;

import com.example.videogamereviewservice.annotations.IT;
import com.example.videogamereviewservice.dto.request.PlatformRequestDto;
import com.example.videogamereviewservice.dto.request.TagRequestDto;
import com.example.videogamereviewservice.dto.response.PlatformResponseDto;
import com.example.videogamereviewservice.dto.response.TagResponseDto;
import com.example.videogamereviewservice.error.NotFoundException;
import com.example.videogamereviewservice.service.local.TagServiceLocal;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

@IT
@Transactional
public class TagServiceLocalIT {
    @Autowired
    private TagServiceLocal tagServiceLocal;

    private static TagRequestDto createTagRequestDto(String name) {
        return TagRequestDto.builder()
                .name(name)
                .build();
    }

    @Test
    void createTag_whenValidData_thenReturnsSavedTag() {
        TagRequestDto requestDto = createTagRequestDto("Cat");

        TagResponseDto savedTag = tagServiceLocal.createTag(requestDto);

        assertThat(savedTag).isNotNull();
        assertThat(savedTag.getId()).isNotNull();
        assertThat(savedTag.getName()).isEqualTo("Cat");
    }

    @Test
    void getTagById_whenNotFound_thenThrowsException() {
        assertThatThrownBy(() -> tagServiceLocal.getTagById(999L))
                .isInstanceOf(NotFoundException.class);
    }
}
