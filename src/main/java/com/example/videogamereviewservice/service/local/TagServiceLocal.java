package com.example.videogamereviewservice.service.local;

import com.example.videogamereviewservice.dto.request.TagRequestDto;
import com.example.videogamereviewservice.dto.response.TagResponseDto;
import com.example.videogamereviewservice.entity.Tag;
import com.example.videogamereviewservice.error.NotFoundException;
import com.example.videogamereviewservice.mapper.TagMapper;
import com.example.videogamereviewservice.repository.TagRepository;
import com.example.videogamereviewservice.service.noImp.TagService;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class TagServiceLocal implements TagService {
    private final TagRepository tagRepository;
    private final TagMapper tagMapper;

    public TagServiceLocal(TagMapper tagMapper, TagRepository tagRepository) {
        this.tagMapper = tagMapper;
        this.tagRepository = tagRepository;
    }

    @Override
    @Transactional
    public TagResponseDto createTag(TagRequestDto tagRequestDto) {
        Tag tag = tagRepository.save(tagMapper.toEntity(tagRequestDto));

        log.info("Tag is created with name {}", tagRequestDto.getName());

        return tagMapper.toDto(tag);
    }

    @Override
    @Transactional
    public TagResponseDto updateTagById(TagRequestDto tagRequestDto, Long id) {
        log.debug("Tag is being updated with id {}", id);

        Tag tag = tagRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Tag not found with id " + id));

        tagMapper.updateTagFromDto(tagRequestDto, tag);

        log.info("Tag was updated with name {} and id {}", tagRequestDto.getName(), id);

        return tagMapper.toDto(tag);
    }

    @Override
    @Transactional
    public TagResponseDto getTagById(Long id) {
        Tag tag = tagRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Tag not found with id " + id));

        log.debug("Tag is received with name {}", tag.getName());

        return tagMapper.toDto(tag);
    }

    @Override
    @Transactional
    public List<TagResponseDto> getTags() {
        log.debug("Receiving all tags...");

        List<Tag> tags = tagRepository.findAll();

        log.info("All tags is received! Count {}", tags.size());

        return tags.stream()
                .map(tagMapper::toDto)
                .toList();
    }

    @Override
    @Transactional
    public void deleteTagById(Long id) {
        Tag tag = tagRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Tag not found with id " + id));
        String name = tag.getName();

        log.debug("Tag is being deleted with name {}", name);

        tagRepository.deleteById(id);

        log.info("Tag was deleted with name {}", name);
    }
}
