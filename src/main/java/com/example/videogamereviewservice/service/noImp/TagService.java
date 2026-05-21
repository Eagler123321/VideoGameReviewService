package com.example.videogamereviewservice.service.noImp;

import com.example.videogamereviewservice.dto.request.base.TagRequestDto;
import com.example.videogamereviewservice.dto.response.TagResponseDto;

import java.util.List;

public interface TagService {
    TagResponseDto createTag(TagRequestDto tagRequestDto);

    TagResponseDto updateTagById(TagRequestDto tagRequestDto, Long id);

    TagResponseDto getTagById(Long id);

    List<TagResponseDto> getTags();

    void deleteTagById(Long id);
}
