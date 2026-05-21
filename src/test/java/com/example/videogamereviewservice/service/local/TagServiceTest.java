package com.example.videogamereviewservice.service.local;

import com.example.videogamereviewservice.dto.request.base.TagRequestDto;
import com.example.videogamereviewservice.dto.response.TagResponseDto;
import com.example.videogamereviewservice.entity.Tag;
import com.example.videogamereviewservice.error.NotFoundException;
import com.example.videogamereviewservice.mapper.TagMapper;
import com.example.videogamereviewservice.repository.TagRepository;
import org.assertj.core.api.AssertionsForClassTypes;
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

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class TagServiceTest {
    @Mock
    private TagMapper tagMapper;
    @Mock
    private TagRepository tagRepository;
    @InjectMocks
    private TagServiceLocal tagServiceLocal;

    private Tag tag;
    private Tag tag2;

    private TagResponseDto tagResponseDto;
    private TagResponseDto tagResponseDto2;

    private TagRequestDto tagRequestDto;
    private TagRequestDto tagRequestDto2;

    private final Long tagId = 1L;
    private final Long tagId2 = 2L;

    private final String name = "cat";
    private final String name2 = "dog";

    @BeforeEach // ИТОГО 9 тестов
    public void init(){
        tagResponseDto = TagResponseDto.builder().id(tagId).name(name).build();
        tagResponseDto2 = TagResponseDto.builder().id(tagId2).name(name2).build();

        tagRequestDto = TagRequestDto.builder().name(name).build();
        tagRequestDto2 = TagRequestDto.builder().name(name2).build();

        tag = Tag.builder().id(tagId).name(name).build();
        tag2 = Tag.builder().id(tagId2).name(name2).build();
    }

    @Test
    public void createTag_whenValidRequest_thenReturnsSavedTag(){
        when(tagMapper.toEntity(tagRequestDto)).thenReturn(tag);
        when(tagRepository.save(any(Tag.class))).thenReturn(tag);
        when(tagMapper.toDto(tag)).thenReturn(tagResponseDto);

        TagResponseDto savedTag = tagServiceLocal.createTag(tagRequestDto);

        assertThat(savedTag).isNotNull();
        assertThat(savedTag.getName()).isEqualTo(name);
    }

    @Test
    public void getTagById_whenExists_thenReturnsTag(){
        when(tagRepository.findById(tagId)).thenReturn(Optional.of(tag));
        when(tagMapper.toDto(any(Tag.class))).thenReturn(tagResponseDto);

        TagResponseDto savedTag = tagServiceLocal.getTagById(tagId);

        assertThat(savedTag).isNotNull();
        assertThat(savedTag.getName()).isEqualTo(name);
    }

    @Test
    public void getTagById_whenNotFound_thenThrowNotFoundException(){
        when(tagRepository.findById(999L)).thenReturn(Optional.empty());

        NotFoundException ex = assertThrows(NotFoundException.class,  () -> tagServiceLocal.getTagById(999L));

        AssertionsForClassTypes.assertThat(ex.getMessage()).contains("999");
    }

    @Test
    public void getTags_whenListNotEmpty_thenReturnsTags(){
        List<Tag> tags = List.of(tag, tag2);

        when(tagRepository.findAll()).thenReturn(tags);
        when(tagMapper.toDto(any(Tag.class))).thenAnswer(invocation -> {
            Tag t = invocation.getArgument(0);
            return switch ((int) (long) t.getId()) {
                case 1 -> tagResponseDto;
                case 2 -> tagResponseDto2;
                default -> throw new RuntimeException("Unknown tag id: " + t.getId());
            };
        });

        List<TagResponseDto> result = tagServiceLocal.getTags();

        assertThat(result).isNotNull();
        assertThat(result).hasSize(2);
        assertThat(result).containsExactlyInAnyOrder(tagResponseDto, tagResponseDto2);
    }

    @Test
    public void getTags_whenListIsEmpty_thenReturnsEmptyList(){
        when(tagRepository.findAll()).thenReturn(List.of());

        List<TagResponseDto> result = tagServiceLocal.getTags();

        AssertionsForInterfaceTypes.assertThat(result).isEmpty();
    }

    @Test
    public void deleteTagById_whenExists_thenReturnsDoesNotThrow(){
        when(tagRepository.findById(tagId)).thenReturn(Optional.of(tag));
        
        tagServiceLocal.deleteTagById(tagId);

        verify(tagRepository).deleteById(tagId);
    }

    @Test
    public void deleteTagById_whenNotFound_thenReturnsDoesNotThrow(){
        when(tagRepository.findById(tagId)).thenReturn(Optional.of(tag));

        tagServiceLocal.deleteTagById(tagId);

        verify(tagRepository).deleteById(tagId);
    }

    @Test
    public void updateTagById_whenValidRequest_thenReturnsUpdatedTag(){
        when(tagRepository.findById(tagId)).thenReturn(Optional.of(tag));

        Mockito.lenient().doNothing().when(tagMapper).updateTagFromDto(any(TagRequestDto.class), any(Tag.class));

        Mockito.lenient().when(tagMapper.toDto(any(Tag.class))).thenReturn(tagResponseDto);

        TagResponseDto savedTag = tagServiceLocal.updateTagById(tagRequestDto, tagId);

        assertThat(savedTag).isNotNull();
        assertThat(savedTag.getName()).isEqualTo(name);

        verify(tagMapper).updateTagFromDto(any(TagRequestDto.class), any(Tag.class));
        verify(tagMapper).toDto(any(Tag.class));
    }

    @Test
    public void updateTagById_whenNotFound_thenThrowNotFoundException(){
        when(tagRepository.findById(999L)).thenReturn(Optional.empty());

        NotFoundException ex = assertThrows(NotFoundException.class,  () -> tagServiceLocal.updateTagById(tagRequestDto, 999L));

        assertThat(ex.getMessage()).contains("999");
    }
}
