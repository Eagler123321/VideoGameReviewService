package com.example.videogamereviewservice.controller;

import com.example.videogamereviewservice.dto.request.TagRequestDto;
import com.example.videogamereviewservice.dto.response.TagResponseDto;
import com.example.videogamereviewservice.error.NotFoundException;
import com.example.videogamereviewservice.service.local.TagServiceLocal;
import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = TagController.class)
@AutoConfigureMockMvc(addFilters = false)
@ExtendWith(MockitoExtension.class)
public class TagControllerTest {
    @MockitoBean
    private TagServiceLocal tagServiceLocal;
    @Autowired
    private MockMvc mockMvc;
    
    private final ObjectMapper objectMapper = new ObjectMapper();

    private final Long tagId = 1L;
    private final String name = "cat";

    private TagRequestDto tagRequestDto;
    private TagResponseDto tagResponseDto;

    @BeforeEach // ИТОГО 11 тестов
    public void init(){
        tagRequestDto = TagRequestDto.builder()
                .name(name)
                .build();
        tagResponseDto = TagResponseDto.builder()
                .id(tagId)
                .name(name)
                .build();
    }

    @Test
    public void createTag_whenTagIsCreated_thenReturnsCreated() throws Exception{
        given(tagServiceLocal.createTag(any(TagRequestDto.class)))
                .willReturn(tagResponseDto);
        
        ResultActions response = mockMvc.perform(post("/tags")
                .contentType(org.springframework.http.MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(tagRequestDto)));

        response.andExpect(status().isCreated())
                .andExpect(jsonPath("$.name", CoreMatchers.is(tagRequestDto.getName())));
    }

    @Test
    public void createTag_whenNameMissing_thenReturnsBadRequest() throws Exception{
        String invalidJson = """
            {
            }
            """;

        ResultActions response = mockMvc.perform(post("/tags")
                .contentType(MediaType.APPLICATION_JSON)
                .content(invalidJson));
        response.andExpect(status().isBadRequest());
    }

    @Test
    public void getTagById_whenExists_thenReturnsOk() throws Exception{
        when(tagServiceLocal.getTagById(tagId)).thenReturn(tagResponseDto);

        ResultActions response = mockMvc.perform(get("/tags/{id}", tagId)
                .contentType(org.springframework.http.MediaType.APPLICATION_JSON));

        response.andExpect(status().isOk())
                .andExpect(jsonPath("$.name", CoreMatchers.is(tagRequestDto.getName())));

    }

    @Test // Проверка исключения и статуса
    public void getTagById_whenNotFound_thenReturnsNotFound() throws Exception{
        when(tagServiceLocal.getTagById(tagId))
                .thenThrow(new NotFoundException("Tag not found!"));

        ResultActions response = mockMvc.perform(get("/tags/{id}", tagId));

        response.andExpect(status().isNotFound());
    }

    @Test
    public void getTags_whenListNotEmpty_thenReturnsOk() throws Exception{
        List<TagResponseDto> tagResponseDtoList = new ArrayList<>(List.of(tagResponseDto));

        when(tagServiceLocal.getTags()).thenReturn(tagResponseDtoList);

        ResultActions response = mockMvc.perform(get("/tags")
                .contentType(MediaType.APPLICATION_JSON));

        response.andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(1))
                .andExpect(jsonPath("$[0].name").value(tagResponseDto.getName()));
    }

    @Test
    public void getTags_whenListIsEmpty_thenReturnsOk() throws Exception{
        given(tagServiceLocal.getTags()).willReturn(List.of());

        ResultActions response = mockMvc.perform(get("/tags")
                .contentType(MediaType.APPLICATION_JSON));

        response.andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)));
    }

    @Test
    public void deleteTagById_whenExists_thenReturnsNoContent() throws Exception{
        doNothing().when(tagServiceLocal).deleteTagById(tagId);

        ResultActions response = mockMvc.perform(delete("/tags/{id}", tagId));

        response.andExpect(status().isNoContent());
    }

    @Test // Проверка исключения и статуса
    public void deleteTagById_whenTagNotFound_thenReturnsNotFound() throws Exception {
        doThrow(new NotFoundException("Tag not found!"))
                .when(tagServiceLocal).deleteTagById(999L);

        ResultActions response = mockMvc.perform(delete("/tags/{id}", 999L));

        response.andExpect(status().isNotFound());
    }

    @Test
    public void updateTagById_whenValidRequest_thenReturnsOk() throws Exception{
        when(tagServiceLocal.updateTagById(any(TagRequestDto.class), eq(tagId)))
                .thenReturn(tagResponseDto);

        ResultActions response = mockMvc.perform(put("/tags/{id}", tagId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(tagRequestDto)));

        response.andExpect(status().isOk())
                .andExpect(jsonPath("$.name", CoreMatchers.is(tagRequestDto.getName())));
    }

    @Test // Проверка исключения и статуса
    public void updateTagById_whenTagIdIsNotFound_thenReturnsNotFound() throws Exception{
        given(tagServiceLocal.updateTagById(any(TagRequestDto.class), eq(tagId)))
                .willThrow(new NotFoundException("Tag not found!"));

        String validJson = """
            {
                "name":"android"
            }
            """;

        ResultActions response = mockMvc.perform(put("/tags/{id}", tagId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(validJson));

        response.andExpect(status().isNotFound());
    }

    @Test
    public void updateTagById_whenNameIsMissing_thenReturnsBadRequest() throws Exception{
        String invalidJson = """
            {
            }
            """;

        ResultActions response = mockMvc.perform(put("/tags/{id}", tagId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(invalidJson));


        response.andExpect(status().isBadRequest());
    }
}
