package com.example.videogamereviewservice.controller;

import com.example.videogamereviewservice.dto.request.TagRequestDto;
import com.example.videogamereviewservice.dto.response.TagResponseDto;
import com.example.videogamereviewservice.service.local.TagServiceLocal;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "/tags")
public class TagController {
    private final TagServiceLocal tagServiceLocal;

    public TagController(TagServiceLocal tagServiceLocal) {
        this.tagServiceLocal = tagServiceLocal;
    }

    @PostMapping
    public ResponseEntity<TagResponseDto> createTag(@Valid @RequestBody TagRequestDto tagRequestDto){
        return ResponseEntity.status(HttpStatus.CREATED).body(tagServiceLocal.createTag(tagRequestDto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<TagResponseDto> updateTagById(@Valid @RequestBody TagRequestDto tagRequestDto, @PathVariable Long id){
        return ResponseEntity.status(HttpStatus.OK).body(tagServiceLocal.updateTagById(tagRequestDto, id));
    }
    @GetMapping("/{id}")
    public ResponseEntity<TagResponseDto> getTagById(@PathVariable Long id){
        return ResponseEntity.status(HttpStatus.OK).body(tagServiceLocal.getTagById(id));
    }
    @GetMapping
    public ResponseEntity<List<TagResponseDto>> getTags(){
        return ResponseEntity.status(HttpStatus.OK).body(tagServiceLocal.getTags());
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTagById(@PathVariable Long id){
        tagServiceLocal.deleteTagById(id);
        return ResponseEntity.noContent().build();
    }
}
