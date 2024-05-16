package org.example.test.service;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.atLeast;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.example.test.cache.EntityCache;
import org.example.test.entity.Tag;
import org.example.test.exception.BadRequestErrorException;
import org.example.test.exception.ResourceNotFoundException;
import org.example.test.repository.TagRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.aot.DisabledInAotMode;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ContextConfiguration(classes = {TagService.class})
@ExtendWith(SpringExtension.class)
@DisabledInAotMode
class TagServiceTest {
    @MockBean
    private EntityCache<Integer, Object> entityCache;

    @MockBean
    private TagRepository tagRepository;

    @Autowired
    private TagService tagService;

    @MockBean
    private TrackService trackService;

    /**
     * Method under test: {@link TagService#postTag(Tag)}
     */
    @Test
    void testPostTag() {
        // Arrange
        Tag tag = new Tag();
        tag.setId(1);
        tag.setText("Text");
        when(tagRepository.save(Mockito.<Tag>any())).thenReturn(tag);
        doNothing().when(entityCache).put(Mockito.<Integer>any(), Mockito.<Object>any());

        Tag tag2 = new Tag();
        tag2.setId(1);
        tag2.setText("Text");

        // Act
        Tag actualPostTagResult = tagService.postTag(tag2);

        // Assert
        verify(entityCache).put(eq(1), isA(Object.class));
        verify(tagRepository).save(isA(Tag.class));
        assertSame(tag2, actualPostTagResult);
    }

    /**
     * Method under test: {@link TagService#postTag(Tag)}
     */
    @Test
    void testPostTag2() {
        // Arrange
        Tag tag = new Tag();
        tag.setId(1);
        tag.setText("Text");
        when(tagRepository.save(Mockito.<Tag>any())).thenReturn(tag);
        doThrow(new BadRequestErrorException("An error occurred")).when(entityCache)
                .put(Mockito.<Integer>any(), Mockito.<Object>any());

        Tag tag2 = new Tag();
        tag2.setId(1);
        tag2.setText("Text");

        // Act and Assert
        assertThrows(BadRequestErrorException.class, () -> tagService.postTag(tag2));
        verify(entityCache).put(eq(1), isA(Object.class));
        verify(tagRepository).save(isA(Tag.class));
    }

    /**
     * Method under test: {@link TagService#deleteTagById(Integer)}
     */
    @Test
    void testDeleteTagById() {
        // Arrange
        Tag tag = new Tag();
        tag.setId(1);
        tag.setText("Text");
        Optional<Tag> ofResult = Optional.of(tag);
        when(tagRepository.findAll()).thenReturn(new ArrayList<>());
        doNothing().when(tagRepository).deleteById(Mockito.<Integer>any());
        when(tagRepository.findById(Mockito.<Integer>any())).thenReturn(ofResult);
        doNothing().when(entityCache).put(Mockito.<Integer>any(), Mockito.<Object>any());

        // Act
        tagService.deleteTagById(1);

        // Assert
        verify(entityCache).put(eq(1798262775), isA(Object.class));
        verify(tagRepository).deleteById(1);
        verify(tagRepository).findById(1);
        verify(tagRepository).findAll();
    }

    /**
     * Method under test: {@link TagService#deleteTagById(Integer)}
     */
    @Test
    void testDeleteTagById2() {
        // Arrange
        Tag tag = new Tag();
        tag.setId(1);
        tag.setText("Text");
        Optional<Tag> ofResult = Optional.of(tag);
        when(tagRepository.findAll()).thenReturn(new ArrayList<>());
        doNothing().when(tagRepository).deleteById(Mockito.<Integer>any());
        when(tagRepository.findById(Mockito.<Integer>any())).thenReturn(ofResult);
        doThrow(new BadRequestErrorException("An error occurred")).when(entityCache)
                .put(Mockito.<Integer>any(), Mockito.<Object>any());

        // Act and Assert
        assertThrows(BadRequestErrorException.class, () -> tagService.deleteTagById(1));
        verify(entityCache).put(eq(1798262775), isA(Object.class));
        verify(tagRepository).deleteById(1);
        verify(tagRepository).findById(1);
        verify(tagRepository).findAll();
    }

    /**
     * Method under test: {@link TagService#deleteTagById(Integer)}
     */
    @Test
    void testDeleteTagById3() {
        // Arrange
        Optional<Tag> emptyResult = Optional.empty();
        when(tagRepository.findById(Mockito.<Integer>any())).thenReturn(emptyResult);

        // Act and Assert
        assertThrows(ResourceNotFoundException.class, () -> tagService.deleteTagById(1));
        verify(tagRepository).findById(1);
    }

    /**
     * Method under test: {@link TagService#updateTagById(Integer, String)}
     */
    @Test
    void testUpdateTagById() {
        // Arrange
        Tag tag = new Tag();
        tag.setId(1);
        tag.setText("Text");
        Optional<Tag> ofResult = Optional.of(tag);

        Tag tag2 = new Tag();
        tag2.setId(1);
        tag2.setText("Text");
        when(tagRepository.save(Mockito.<Tag>any())).thenReturn(tag2);
        when(tagRepository.findById(Mockito.<Integer>any())).thenReturn(ofResult);
        doNothing().when(entityCache).put(Mockito.<Integer>any(), Mockito.<Object>any());

        // Act
        tagService.updateTagById(1, "New Text");

        // Assert
        verify(entityCache).put(eq(-95474076), isA(Object.class));
        verify(tagRepository, atLeast(1)).findById(1);
        verify(tagRepository).save(isA(Tag.class));
    }

    /**
     * Method under test: {@link TagService#updateTagById(Integer, String)}
     */
    @Test
    void testUpdateTagById2() {
        // Arrange
        Tag tag = new Tag();
        tag.setId(1);
        tag.setText("Text");
        Optional<Tag> ofResult = Optional.of(tag);

        Tag tag2 = new Tag();
        tag2.setId(1);
        tag2.setText("Text");
        when(tagRepository.save(Mockito.<Tag>any())).thenReturn(tag2);
        when(tagRepository.findById(Mockito.<Integer>any())).thenReturn(ofResult);
        doThrow(new BadRequestErrorException("An error occurred")).when(entityCache)
                .put(Mockito.<Integer>any(), Mockito.<Object>any());

        // Act and Assert
        assertThrows(BadRequestErrorException.class, () -> tagService.updateTagById(1, "New Text"));
        verify(entityCache).put(eq(-95474076), isA(Object.class));
        verify(tagRepository, atLeast(1)).findById(1);
        verify(tagRepository).save(isA(Tag.class));
    }

    /**
     * Method under test: {@link TagService#updateTagById(Integer, String)}
     */
    @Test
    void testUpdateTagById3() {
        // Arrange
        Optional<Tag> emptyResult = Optional.empty();
        when(tagRepository.findById(Mockito.<Integer>any())).thenReturn(emptyResult);

        // Act and Assert
        assertThrows(ResourceNotFoundException.class, () -> tagService.updateTagById(1, "New Text"));
        verify(tagRepository).findById(1);
    }

    /**
     * Method under test: {@link TagService#getAllTags()}
     */
    @Test
    void testGetAllTags() {
        // Arrange
        when(tagRepository.findAll()).thenReturn(new ArrayList<>());

        // Act and Assert
        assertThrows(ResourceNotFoundException.class, () -> tagService.getAllTags());
        verify(tagRepository).findAll();
    }

    /**
     * Method under test: {@link TagService#getAllTags()}
     */
    @Test
    void testGetAllTags2() {
        // Arrange
        Tag tag = new Tag();
        tag.setId(1);
        tag.setText("No tag has been created! :(");

        ArrayList<Tag> tagList = new ArrayList<>();
        tagList.add(tag);
        when(tagRepository.findAll()).thenReturn(tagList);
        doNothing().when(entityCache).put(Mockito.<Integer>any(), Mockito.<Object>any());

        // Act
        Optional<List<Tag>> actualAllTags = tagService.getAllTags();

        // Assert
        verify(entityCache).put(eq(1), isA(Object.class));
        verify(tagRepository).findAll();
        assertTrue(actualAllTags.isPresent());
    }

    /**
     * Method under test: {@link TagService#getAllTags()}
     */
    @Test
    void testGetAllTags3() {
        // Arrange
        Tag tag = new Tag();
        tag.setId(1);
        tag.setText("No tag has been created! :(");

        Tag tag2 = new Tag();
        tag2.setId(2);
        tag2.setText("42");

        ArrayList<Tag> tagList = new ArrayList<>();
        tagList.add(tag2);
        tagList.add(tag);
        when(tagRepository.findAll()).thenReturn(tagList);
        doNothing().when(entityCache).put(Mockito.<Integer>any(), Mockito.<Object>any());

        // Act
        Optional<List<Tag>> actualAllTags = tagService.getAllTags();

        // Assert
        verify(entityCache, atLeast(1)).put(Mockito.<Integer>any(), Mockito.<Object>any());
        verify(tagRepository).findAll();
        assertTrue(actualAllTags.isPresent());
    }

    /**
     * Method under test: {@link TagService#getAllTags()}
     */
    @Test
    void testGetAllTags4() {
        // Arrange
        Tag tag = new Tag();
        tag.setId(1);
        tag.setText("No tag has been created! :(");

        ArrayList<Tag> tagList = new ArrayList<>();
        tagList.add(tag);
        when(tagRepository.findAll()).thenReturn(tagList);
        doThrow(new BadRequestErrorException("An error occurred")).when(entityCache)
                .put(Mockito.<Integer>any(), Mockito.<Object>any());

        // Act and Assert
        assertThrows(BadRequestErrorException.class, () -> tagService.getAllTags());
        verify(entityCache).put(eq(1), isA(Object.class));
        verify(tagRepository).findAll();
    }

    /**
     * Method under test: {@link TagService#getTagById(Integer)}
     */
    @Test
    void testGetTagById() {
        // Arrange
        Tag tag = new Tag();
        tag.setId(1);
        tag.setText("Text");
        Optional<Tag> ofResult = Optional.of(tag);
        when(tagRepository.findById(Mockito.<Integer>any())).thenReturn(ofResult);
        doNothing().when(entityCache).put(Mockito.<Integer>any(), Mockito.<Object>any());

        // Act
        Optional<Tag> actualTagById = tagService.getTagById(1);

        // Assert
        verify(entityCache).put(eq(-95474076), isA(Object.class));
        verify(tagRepository).findById(1);
        assertSame(ofResult, actualTagById);
    }

    /**
     * Method under test: {@link TagService#getTagById(Integer)}
     */
    @Test
    void testGetTagById2() {
        // Arrange
        Tag tag = new Tag();
        tag.setId(1);
        tag.setText("Text");
        Optional<Tag> ofResult = Optional.of(tag);
        when(tagRepository.findById(Mockito.<Integer>any())).thenReturn(ofResult);
        doThrow(new BadRequestErrorException("An error occurred")).when(entityCache)
                .put(Mockito.<Integer>any(), Mockito.<Object>any());

        // Act and Assert
        assertThrows(BadRequestErrorException.class, () -> tagService.getTagById(1));
        verify(entityCache).put(eq(-95474076), isA(Object.class));
        verify(tagRepository).findById(1);
    }

    /**
     * Method under test: {@link TagService#getTagById(Integer)}
     */
    @Test
    void testGetTagById3() {
        // Arrange
        Optional<Tag> emptyResult = Optional.empty();
        when(tagRepository.findById(Mockito.<Integer>any())).thenReturn(emptyResult);

        // Act and Assert
        assertThrows(ResourceNotFoundException.class, () -> tagService.getTagById(1));
        verify(tagRepository).findById(1);
    }

    /**
     * Method under test: {@link TagService#updateCacheForAllTags()}
     */
    @Test
    void testUpdateCacheForAllTags() {
        // Arrange
        when(tagRepository.findAll()).thenReturn(new ArrayList<>());
        doNothing().when(entityCache).put(Mockito.<Integer>any(), Mockito.<Object>any());

        // Act
        tagService.updateCacheForAllTags();

        // Assert that nothing has changed
        verify(entityCache).put(eq(1798262775), isA(Object.class));
        verify(tagRepository).findAll();
    }

    /**
     * Method under test: {@link TagService#updateCacheForAllTags()}
     */
    @Test
    void testUpdateCacheForAllTags2() {
        // Arrange
        when(tagRepository.findAll()).thenReturn(new ArrayList<>());
        doThrow(new BadRequestErrorException("An error occurred")).when(entityCache)
                .put(Mockito.<Integer>any(), Mockito.<Object>any());

        // Act and Assert
        assertThrows(BadRequestErrorException.class, () -> tagService.updateCacheForAllTags());
        verify(entityCache).put(eq(1798262775), isA(Object.class));
        verify(tagRepository).findAll();
    }

    /**
     * Method under test: {@link TagService#updateCacheForAllTags()}
     */
    @Test
    void testUpdateCacheForAllTags3() {
        // Arrange
        when(tagRepository.findAll()).thenThrow(new BadRequestErrorException("An error occurred"));

        // Act and Assert
        assertThrows(BadRequestErrorException.class, () -> tagService.updateCacheForAllTags());
        verify(tagRepository).findAll();
    }

    /**
     * Method under test: {@link TagService#updateCacheForTagById(Integer)}
     */
    @Test
    void testUpdateCacheForTagById() {
        // Arrange
        Tag tag = new Tag();
        tag.setId(1);
        tag.setText("Text");
        Optional<Tag> ofResult = Optional.of(tag);
        when(tagRepository.findById(Mockito.<Integer>any())).thenReturn(ofResult);
        doNothing().when(entityCache).put(Mockito.<Integer>any(), Mockito.<Object>any());

        // Act
        tagService.updateCacheForTagById(1);

        // Assert
        verify(entityCache).put(eq(-95474076), isA(Object.class));
        verify(tagRepository).findById(1);
    }

    /**
     * Method under test: {@link TagService#updateCacheForTagById(Integer)}
     */
    @Test
    void testUpdateCacheForTagById2() {
        // Arrange
        Tag tag = new Tag();
        tag.setId(1);
        tag.setText("Text");
        Optional<Tag> ofResult = Optional.of(tag);
        when(tagRepository.findById(Mockito.<Integer>any())).thenReturn(ofResult);
        doThrow(new BadRequestErrorException("An error occurred")).when(entityCache)
                .put(Mockito.<Integer>any(), Mockito.<Object>any());

        // Act and Assert
        assertThrows(BadRequestErrorException.class, () -> tagService.updateCacheForTagById(1));
        verify(entityCache).put(eq(-95474076), isA(Object.class));
        verify(tagRepository).findById(1);
    }

    /**
     * Method under test: {@link TagService#updateCacheForTagById(Integer)}
     */
    @Test
    void testUpdateCacheForTagById3() {
        // Arrange
        Optional<Tag> emptyResult = Optional.empty();
        when(tagRepository.findById(Mockito.<Integer>any())).thenReturn(emptyResult);

        // Act
        tagService.updateCacheForTagById(1);

        // Assert
        verify(tagRepository).findById(1);
    }
}
