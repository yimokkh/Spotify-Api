package org.example.test.service;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.example.test.entity.Tag;
import org.example.test.exception.BadRequestErrorException;
import org.example.test.exception.ResourceNotFoundException;
import org.example.test.repository.TagRepository;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;

import java.util.*;

public class TagServiceTest {

    @Mock
    private TagRepository tagRepository;

    @InjectMocks
    private TagService tagService;

    @InjectMocks
    private TrackService trackService;

    @Mock
    private Map<Integer, Tag> cacheMap;

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
    }

    @Test(expected = BadRequestErrorException.class)
    public void testPostTagThrowsException() {
        // Arrange
        Tag tag = new Tag();
        tag.setText("Existing Tag");
        when(tagRepository.save(any(Tag.class))).thenThrow(new BadRequestErrorException("tag already exists"));

        // Act
        tagService.postTag(tag);
    }

    @Test
    public void testPostTagWithDuplicateText() {
        // Arrange
        Tag tag = new Tag("Duplicate Tag");
        when(tagRepository.save(any(Tag.class))).thenThrow(new DataIntegrityViolationException("Tag with text already exists"));

        // Act & Assert
        assertThrows(BadRequestErrorException.class, () -> {
            tagService.postTag(tag);
        });
    }

    @Test
    public void testPostTagWithCachePutFailure() {
        // Arrange
        Tag tag = new Tag("Valid Tag");
        when(tagRepository.save(any(Tag.class))).thenReturn(tag);
        doThrow(new RuntimeException("Failed to put in cache")).when(cacheMap).put(tag.getId(), tag);

        // Act & Assert
        assertThrows(RuntimeException.class, () -> {
            tagService.postTag(tag);
        });
    }

    @Test(expected = ResourceNotFoundException.class)
    public void testDeleteTagByIdNotFound() {
        // Arrange
        Integer id = 1;
        when(tagRepository.findById(id)).thenReturn(Optional.empty());

        // Act
        tagService.deleteTagById(id);
    }

    @Test
    public void testDeleteTagByIdWithNonExistentId() {
        // Arrange
        Integer nonExistentId = 999;
        when(tagRepository.findById(nonExistentId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> {
            tagService.deleteTagById(nonExistentId);
        });
    }

    @Test(expected = ResourceNotFoundException.class)
    public void testUpdateTagByIdNotFound() {
        // Arrange
        Integer id = 1;
        String newText = "Updated Text";
        when(tagRepository.findById(id)).thenReturn(Optional.empty());

        // Act
        tagService.updateTagById(id, newText);
    }

    @Test
    public void testUpdateTagByIdRepositoryError() {
        // Arrange
        Integer id = 1;
        String newText = "Valid Text";
        when(tagRepository.findById(id)).thenReturn(Optional.of(new Tag()));
        doThrow(new DataAccessException("...") {}).when(tagRepository).save(any(Tag.class));

        // Act & Assert
        assertThrows(DataAccessException.class, () -> {
            tagService.updateTagById(id, newText);
        });
    }

    @Test(expected = ResourceNotFoundException.class)
    public void testGetAllTagsWhenNoTagsExist() {
        // Arrange
        when(tagRepository.findAll()).thenReturn(Collections.emptyList());

        // Act
        tagService.getAllTags();
    }

    @Test
    public void testGetAllTagsRepositoryError() {
        // Arrange
        when(tagRepository.findAll()).thenThrow(new DataAccessException("...") {});

        // Act & Assert
        assertThrows(DataAccessException.class, () -> {
            tagService.getAllTags();
        });
    }

    @Test
    public void testUpdateCacheForTagByIdWhenTagNotFound() {
        // Arrange
        Integer id = 1;
        int hashCode = Objects.hash("tag_by_id", id);
        when(tagRepository.findById(id)).thenReturn(Optional.empty());

        // Act
        tagService.updateCacheForTagById(id);

        // Assert
        verify(cacheMap, never()).put(eq(hashCode), any());
    }

}

