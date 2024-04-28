package org.example.test.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.atLeast;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.example.test.cache.EntityCache;
import org.example.test.entity.Playlist;
import org.example.test.entity.Tag;
import org.example.test.entity.Track;
import org.example.test.exception.BadRequestErrorException;
import org.example.test.exception.ResourceNotFoundException;
import org.example.test.repository.TagRepository;
import org.example.test.repository.TrackRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.aot.DisabledInAotMode;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ContextConfiguration(classes = {TrackService.class})
@ExtendWith(SpringExtension.class)
@DisabledInAotMode
class TrackServiceTest {
    @MockBean
    private EntityCache<Integer, Object> entityCache;

    @MockBean
    private TagRepository tagRepository;

    @MockBean
    private TrackRepository trackRepository;

    @Autowired
    private TrackService trackService;

    /**
     * Method under test: {@link TrackService#postTrack(Track)}
     */
    @Test
    void testPostTrack() {
        // Arrange
        Tag tag = new Tag();
        tag.setId(1);
        tag.setText("Text");

        Track track = new Track();
        track.addTag(tag);
        track.setArtist("Artist");
        track.setId(1);
        track.setName("Name");
        track.setPlaylists(new HashSet<>());
        when(trackRepository.save(Mockito.<Track>any())).thenReturn(track);
        doNothing().when(entityCache).put(Mockito.<Integer>any(), Mockito.<Object>any());

        Tag tag2 = new Tag();
        tag2.setId(1);
        tag2.setText("Text");

        Track track2 = new Track();
        track2.addTag(tag2);
        track2.setArtist("Artist");
        track2.setId(1);
        track2.setName("Name");
        track2.setPlaylists(new HashSet<>());

        // Act
        trackService.postTrack(track2);

        // Assert
        verify(entityCache).put(eq(1), isA(Object.class));
        verify(trackRepository).save(isA(Track.class));
    }

    /**
     * Method under test: {@link TrackService#postTrack(Track)}
     */
    @Test
    void testPostTrack2() {
        // Arrange
        Tag tag = new Tag();
        tag.setId(1);
        tag.setText("Text");

        Track track = new Track();
        track.addTag(tag);
        track.setArtist("Artist");
        track.setId(1);
        track.setName("Name");
        track.setPlaylists(new HashSet<>());
        when(trackRepository.save(Mockito.<Track>any())).thenReturn(track);
        doThrow(new BadRequestErrorException("An error occurred")).when(entityCache)
                .put(Mockito.<Integer>any(), Mockito.<Object>any());

        Tag tag2 = new Tag();
        tag2.setId(1);
        tag2.setText("Text");

        Track track2 = new Track();
        track2.addTag(tag2);
        track2.setArtist("Artist");
        track2.setId(1);
        track2.setName("Name");
        track2.setPlaylists(new HashSet<>());

        // Act and Assert
        assertThrows(BadRequestErrorException.class, () -> trackService.postTrack(track2));
        verify(entityCache).put(eq(1), isA(Object.class));
        verify(trackRepository).save(isA(Track.class));
    }

    /**
     * Method under test: {@link TrackService#postTrack(Track)}
     */
    @Test
    void testPostTrack3() {
        // Arrange
        Tag tag = new Tag();
        tag.setId(1);
        tag.setText("Text");
        Track track = mock(Track.class);
        when(track.getId()).thenReturn(1);
        doNothing().when(track).addTag(Mockito.<Tag>any());
        doNothing().when(track).setArtist(Mockito.<String>any());
        doNothing().when(track).setId(Mockito.<Integer>any());
        doNothing().when(track).setName(Mockito.<String>any());
        doNothing().when(track).setPlaylists(Mockito.<Set<Playlist>>any());
        track.addTag(tag);
        track.setArtist("Artist");
        track.setId(1);
        track.setName("Name");
        track.setPlaylists(new HashSet<>());
        when(trackRepository.save(Mockito.<Track>any())).thenReturn(track);
        doNothing().when(entityCache).put(Mockito.<Integer>any(), Mockito.<Object>any());

        Tag tag2 = new Tag();
        tag2.setId(1);
        tag2.setText("Text");

        Track track2 = new Track();
        track2.addTag(tag2);
        track2.setArtist("Artist");
        track2.setId(1);
        track2.setName("Name");
        track2.setPlaylists(new HashSet<>());

        // Act
        trackService.postTrack(track2);

        // Assert
        verify(entityCache).put(eq(1), isA(Object.class));
        verify(track).addTag(isA(Tag.class));
        verify(track).getId();
        verify(track).setArtist("Artist");
        verify(track).setId(1);
        verify(track).setName("Name");
        verify(track).setPlaylists(isA(Set.class));
        verify(trackRepository).save(isA(Track.class));
    }

    /**
     * Method under test: {@link TrackService#deleteTrackById(Integer)}
     */
    @Test
    void testDeleteTrackById() {
        // Arrange
        Tag tag = new Tag();
        tag.setId(1);
        tag.setText("Text");

        Track track = new Track();
        track.addTag(tag);
        track.setArtist("Artist");
        track.setId(1);
        track.setName("Name");
        track.setPlaylists(new HashSet<>());
        Optional<Track> ofResult = Optional.of(track);
        when(trackRepository.findAll()).thenReturn(new ArrayList<>());
        doNothing().when(trackRepository).deleteById(Mockito.<Integer>any());
        when(trackRepository.findById(Mockito.<Integer>any())).thenReturn(ofResult);
        doNothing().when(entityCache).put(Mockito.<Integer>any(), Mockito.<Object>any());

        // Act
        trackService.deleteTrackById(1);

        // Assert
        verify(entityCache).put(eq(1569182950), isA(Object.class));
        verify(trackRepository).deleteById(1);
        verify(trackRepository).findById(1);
        verify(trackRepository).findAll();
    }

    /**
     * Method under test: {@link TrackService#deleteTrackById(Integer)}
     */
    @Test
    void testDeleteTrackById2() {
        // Arrange
        Tag tag = new Tag();
        tag.setId(1);
        tag.setText("Text");

        Track track = new Track();
        track.addTag(tag);
        track.setArtist("Artist");
        track.setId(1);
        track.setName("Name");
        track.setPlaylists(new HashSet<>());
        Optional<Track> ofResult = Optional.of(track);
        when(trackRepository.findAll()).thenReturn(new ArrayList<>());
        doNothing().when(trackRepository).deleteById(Mockito.<Integer>any());
        when(trackRepository.findById(Mockito.<Integer>any())).thenReturn(ofResult);
        doThrow(new BadRequestErrorException("An error occurred")).when(entityCache)
                .put(Mockito.<Integer>any(), Mockito.<Object>any());

        // Act and Assert
        assertThrows(BadRequestErrorException.class, () -> trackService.deleteTrackById(1));
        verify(entityCache).put(eq(1569182950), isA(Object.class));
        verify(trackRepository).deleteById(1);
        verify(trackRepository).findById(1);
        verify(trackRepository).findAll();
    }

    /**
     * Method under test: {@link TrackService#deleteTrackById(Integer)}
     */
    @Test
    void testDeleteTrackById3() {
        // Arrange
        Tag tag = new Tag();
        tag.setId(1);
        tag.setText("Text");
        Track track = mock(Track.class);
        when(track.getTags()).thenReturn(new ArrayList<>());
        doNothing().when(track).addTag(Mockito.<Tag>any());
        doNothing().when(track).setArtist(Mockito.<String>any());
        doNothing().when(track).setId(Mockito.<Integer>any());
        doNothing().when(track).setName(Mockito.<String>any());
        doNothing().when(track).setPlaylists(Mockito.<Set<Playlist>>any());
        track.addTag(tag);
        track.setArtist("Artist");
        track.setId(1);
        track.setName("Name");
        track.setPlaylists(new HashSet<>());
        Optional<Track> ofResult = Optional.of(track);
        when(trackRepository.findAll()).thenReturn(new ArrayList<>());
        doNothing().when(trackRepository).deleteById(Mockito.<Integer>any());
        when(trackRepository.findById(Mockito.<Integer>any())).thenReturn(ofResult);
        doNothing().when(entityCache).put(Mockito.<Integer>any(), Mockito.<Object>any());

        // Act
        trackService.deleteTrackById(1);

        // Assert
        verify(entityCache).put(eq(1569182950), isA(Object.class));
        verify(track).addTag(isA(Tag.class));
        verify(track, atLeast(1)).getTags();
        verify(track).setArtist("Artist");
        verify(track).setId(1);
        verify(track).setName("Name");
        verify(track).setPlaylists(isA(Set.class));
        verify(trackRepository).deleteById(1);
        verify(trackRepository).findById(1);
        verify(trackRepository).findAll();
    }

    /**
     * Method under test: {@link TrackService#deleteTrackById(Integer)}
     */
    @Test
    void testDeleteTrackById4() {
        // Arrange
        Optional<Track> emptyResult = Optional.empty();
        when(trackRepository.findById(Mockito.<Integer>any())).thenReturn(emptyResult);

        // Act and Assert
        assertThrows(ResourceNotFoundException.class, () -> trackService.deleteTrackById(1));
        verify(trackRepository).findById(1);
    }

    /**
     * Method under test: {@link TrackService#deleteTrackById(Integer)}
     */
    @Test
    void testDeleteTrackById5() {
        // Arrange
        Tag tag = new Tag();
        tag.setId(1);
        tag.setText("Text");

        Tag tag2 = new Tag();
        tag2.setId(1);
        tag2.setText("all_tracks");

        Tag tag3 = new Tag();
        tag3.setId(2);
        tag3.setText("42");

        ArrayList<Tag> tagList = new ArrayList<>();
        tagList.add(tag3);
        tagList.add(tag2);
        Track track = mock(Track.class);
        when(track.getTags()).thenReturn(tagList);
        doNothing().when(track).addTag(Mockito.<Tag>any());
        doNothing().when(track).setArtist(Mockito.<String>any());
        doNothing().when(track).setId(Mockito.<Integer>any());
        doNothing().when(track).setName(Mockito.<String>any());
        doNothing().when(track).setPlaylists(Mockito.<Set<Playlist>>any());
        track.addTag(tag);
        track.setArtist("Artist");
        track.setId(1);
        track.setName("Name");
        track.setPlaylists(new HashSet<>());
        Optional<Track> ofResult = Optional.of(track);
        when(trackRepository.findAll()).thenReturn(new ArrayList<>());
        doNothing().when(trackRepository).deleteById(Mockito.<Integer>any());
        when(trackRepository.findById(Mockito.<Integer>any())).thenReturn(ofResult);
        doNothing().when(entityCache).put(Mockito.<Integer>any(), Mockito.<Object>any());

        // Act
        trackService.deleteTrackById(1);

        // Assert
        verify(entityCache).put(eq(1569182950), isA(Object.class));
        verify(track).addTag(isA(Tag.class));
        verify(track, atLeast(1)).getTags();
        verify(track).setArtist("Artist");
        verify(track).setId(1);
        verify(track).setName("Name");
        verify(track).setPlaylists(isA(Set.class));
        verify(trackRepository).deleteById(1);
        verify(trackRepository).findById(1);
        verify(trackRepository).findAll();
    }

    /**
     * Method under test: {@link TrackService#getAllTracks()}
     */
    @Test
    void testGetAllTracks() {
        // Arrange
        when(trackRepository.findAll()).thenReturn(new ArrayList<>());

        // Act and Assert
        assertThrows(ResourceNotFoundException.class, () -> trackService.getAllTracks());
        verify(trackRepository).findAll();
    }

    /**
     * Method under test: {@link TrackService#getAllTracks()}
     */
    @Test
    void testGetAllTracks2() {
        // Arrange
        Tag tag = new Tag();
        tag.setId(1);
        tag.setText("No tracks has been created!");

        Track track = new Track();
        track.addTag(tag);
        track.setArtist("No tracks has been created!");
        track.setId(1);
        track.setName("No tracks has been created!");
        track.setPlaylists(new HashSet<>());

        ArrayList<Track> trackList = new ArrayList<>();
        trackList.add(track);
        when(trackRepository.findAll()).thenReturn(trackList);
        doNothing().when(entityCache).put(Mockito.<Integer>any(), Mockito.<Object>any());

        // Act
        Optional<List<Track>> actualAllTracks = trackService.getAllTracks();

        // Assert
        verify(entityCache).put(eq(1), isA(Object.class));
        verify(trackRepository).findAll();
        assertTrue(actualAllTracks.isPresent());
    }

    /**
     * Method under test: {@link TrackService#getAllTracks()}
     */
    @Test
    void testGetAllTracks3() {
        // Arrange
        Tag tag = new Tag();
        tag.setId(1);
        tag.setText("No tracks has been created!");

        Track track = new Track();
        track.addTag(tag);
        track.setArtist("No tracks has been created!");
        track.setId(1);
        track.setName("No tracks has been created!");
        track.setPlaylists(new HashSet<>());

        Tag tag2 = new Tag();
        tag2.setId(2);
        tag2.setText("42");

        Track track2 = new Track();
        track2.addTag(tag2);
        track2.setArtist("42");
        track2.setId(2);
        track2.setName("42");
        track2.setPlaylists(new HashSet<>());

        ArrayList<Track> trackList = new ArrayList<>();
        trackList.add(track2);
        trackList.add(track);
        when(trackRepository.findAll()).thenReturn(trackList);
        doNothing().when(entityCache).put(Mockito.<Integer>any(), Mockito.<Object>any());

        // Act
        Optional<List<Track>> actualAllTracks = trackService.getAllTracks();

        // Assert
        verify(entityCache, atLeast(1)).put(Mockito.<Integer>any(), Mockito.<Object>any());
        verify(trackRepository).findAll();
        assertTrue(actualAllTracks.isPresent());
    }

    /**
     * Method under test: {@link TrackService#getAllTracks()}
     */
    @Test
    void testGetAllTracks4() {
        // Arrange
        Tag tag = new Tag();
        tag.setId(1);
        tag.setText("No tracks has been created!");
        Track track = mock(Track.class);
        when(track.getId()).thenReturn(1);
        doNothing().when(track).addTag(Mockito.<Tag>any());
        doNothing().when(track).setArtist(Mockito.<String>any());
        doNothing().when(track).setId(Mockito.<Integer>any());
        doNothing().when(track).setName(Mockito.<String>any());
        doNothing().when(track).setPlaylists(Mockito.<Set<Playlist>>any());
        track.addTag(tag);
        track.setArtist("No tracks has been created!");
        track.setId(1);
        track.setName("No tracks has been created!");
        track.setPlaylists(new HashSet<>());

        ArrayList<Track> trackList = new ArrayList<>();
        trackList.add(track);
        when(trackRepository.findAll()).thenReturn(trackList);
        doNothing().when(entityCache).put(Mockito.<Integer>any(), Mockito.<Object>any());

        // Act
        Optional<List<Track>> actualAllTracks = trackService.getAllTracks();

        // Assert
        verify(entityCache).put(eq(1), isA(Object.class));
        verify(track).addTag(isA(Tag.class));
        verify(track).getId();
        verify(track).setArtist("No tracks has been created!");
        verify(track).setId(1);
        verify(track).setName("No tracks has been created!");
        verify(track).setPlaylists(isA(Set.class));
        verify(trackRepository).findAll();
        assertTrue(actualAllTracks.isPresent());
    }

    /**
     * Method under test: {@link TrackService#getTrackById(Integer)}
     */
    @Test
    void testGetTrackById() {
        // Arrange
        when(entityCache.get(Mockito.<Integer>any())).thenThrow(new IllegalArgumentException("track_by_id"));

        // Act and Assert
        assertThrows(IllegalArgumentException.class, () -> trackService.getTrackById(1));
        verify(entityCache).get(1799349139);
    }

    /**
     * Method under test: {@link TrackService#getTrackById(Integer)}
     */
    @Test
    void testGetTrackById2() {
        // Arrange
        Tag tag = mock(Tag.class);
        doNothing().when(tag).setId(Mockito.<Integer>any());
        doNothing().when(tag).setText(Mockito.<String>any());
        tag.setId(1);
        tag.setText("Text");
        Track track = mock(Track.class);
        doNothing().when(track).addTag(Mockito.<Tag>any());
        doNothing().when(track).setArtist(Mockito.<String>any());
        doNothing().when(track).setId(Mockito.<Integer>any());
        doNothing().when(track).setName(Mockito.<String>any());
        doNothing().when(track).setPlaylists(Mockito.<Set<Playlist>>any());
        track.addTag(tag);
        track.setArtist("Artist");
        track.setId(1);
        track.setName("Name");
        track.setPlaylists(new HashSet<>());
        Optional.of(track);

        Tag tag2 = new Tag();
        tag2.setId(1);
        tag2.setText("track_by_id");

        Track track2 = new Track();
        track2.addTag(tag2);
        track2.setArtist("track_by_id");
        track2.setId(1);
        track2.setName("track_by_id");
        track2.setPlaylists(new HashSet<>());
        when(entityCache.get(Mockito.<Integer>any())).thenReturn(track2);

        // Act
        Optional<Track> actualTrackById = trackService.getTrackById(1);

        // Assert
        verify(entityCache).get(1799349139);
        verify(tag).setId(1);
        verify(tag).setText("Text");
        verify(track).addTag(isA(Tag.class));
        verify(track).setArtist("Artist");
        verify(track).setId(1);
        verify(track).setName("Name");
        verify(track).setPlaylists(isA(Set.class));
        assertTrue(actualTrackById.isPresent());
    }

    /**
     * Method under test: {@link TrackService#getTrackById(Integer)}
     */
    @Test
    void testGetTrackById3() {
        // Arrange
        Tag tag = mock(Tag.class);
        doNothing().when(tag).setId(Mockito.<Integer>any());
        doNothing().when(tag).setText(Mockito.<String>any());
        tag.setId(1);
        tag.setText("Text");
        Track track = mock(Track.class);
        doNothing().when(track).addTag(Mockito.<Tag>any());
        doNothing().when(track).setArtist(Mockito.<String>any());
        doNothing().when(track).setId(Mockito.<Integer>any());
        doNothing().when(track).setName(Mockito.<String>any());
        doNothing().when(track).setPlaylists(Mockito.<Set<Playlist>>any());
        track.addTag(tag);
        track.setArtist("Artist");
        track.setId(1);
        track.setName("Name");
        track.setPlaylists(new HashSet<>());
        Optional<Track> ofResult = Optional.of(track);
        when(trackRepository.findById(Mockito.<Integer>any())).thenReturn(ofResult);
        when(entityCache.get(Mockito.<Integer>any())).thenReturn(null);
        doNothing().when(entityCache).put(Mockito.<Integer>any(), Mockito.<Object>any());

        // Act
        Optional<Track> actualTrackById = trackService.getTrackById(1);

        // Assert
        verify(entityCache).get(1799349139);
        verify(entityCache).put(eq(1799349139), isA(Object.class));
        verify(tag).setId(1);
        verify(tag).setText("Text");
        verify(track).addTag(isA(Tag.class));
        verify(track).setArtist("Artist");
        verify(track).setId(1);
        verify(track).setName("Name");
        verify(track).setPlaylists(isA(Set.class));
        verify(trackRepository).findById(1);
        assertTrue(actualTrackById.isPresent());
        assertEquals(ofResult, actualTrackById);
    }

    /**
     * Method under test: {@link TrackService#addTagToTrack(Integer, Integer)}
     */
    @Test
    void testAddTagToTrack() {
        // Arrange
        Tag tag = new Tag();
        tag.setId(1);
        tag.setText("Text");

        Track track = new Track();
        track.addTag(tag);
        track.setArtist("Artist");
        track.setId(1);
        track.setName("Name");
        track.setPlaylists(new HashSet<>());
        Optional<Track> ofResult = Optional.of(track);

        Tag tag2 = new Tag();
        tag2.setId(1);
        tag2.setText("Text");

        Track track2 = new Track();
        track2.addTag(tag2);
        track2.setArtist("Artist");
        track2.setId(1);
        track2.setName("Name");
        track2.setPlaylists(new HashSet<>());
        when(trackRepository.save(Mockito.<Track>any())).thenReturn(track2);
        when(trackRepository.findById(Mockito.<Integer>any())).thenReturn(ofResult);
        doNothing().when(entityCache).put(Mockito.<Integer>any(), Mockito.<Object>any());

        Tag tag3 = new Tag();
        tag3.setId(1);
        tag3.setText("Text");
        Optional<Tag> ofResult2 = Optional.of(tag3);
        when(tagRepository.findById(Mockito.<Integer>any())).thenReturn(ofResult2);

        // Act
        trackService.addTagToTrack(1, 1);

        // Assert
        verify(entityCache).put(eq(1799349139), isA(Object.class));
        verify(tagRepository).findById(1);
        verify(trackRepository, atLeast(1)).findById(1);
        verify(trackRepository).save(isA(Track.class));
    }

    /**
     * Method under test: {@link TrackService#addTagToTrack(Integer, Integer)}
     */
    @Test
    void testAddTagToTrack2() {
        // Arrange
        Tag tag = new Tag();
        tag.setId(1);
        tag.setText("Text");

        Track track = new Track();
        track.addTag(tag);
        track.setArtist("Artist");
        track.setId(1);
        track.setName("Name");
        track.setPlaylists(new HashSet<>());
        Optional<Track> ofResult = Optional.of(track);

        Tag tag2 = new Tag();
        tag2.setId(1);
        tag2.setText("Text");

        Track track2 = new Track();
        track2.addTag(tag2);
        track2.setArtist("Artist");
        track2.setId(1);
        track2.setName("Name");
        track2.setPlaylists(new HashSet<>());
        when(trackRepository.save(Mockito.<Track>any())).thenReturn(track2);
        when(trackRepository.findById(Mockito.<Integer>any())).thenReturn(ofResult);
        doThrow(new BadRequestErrorException("An error occurred")).when(entityCache)
                .put(Mockito.<Integer>any(), Mockito.<Object>any());

        Tag tag3 = new Tag();
        tag3.setId(1);
        tag3.setText("Text");
        Optional<Tag> ofResult2 = Optional.of(tag3);
        when(tagRepository.findById(Mockito.<Integer>any())).thenReturn(ofResult2);

        // Act and Assert
        assertThrows(BadRequestErrorException.class, () -> trackService.addTagToTrack(1, 1));
        verify(entityCache).put(eq(1799349139), isA(Object.class));
        verify(tagRepository).findById(1);
        verify(trackRepository, atLeast(1)).findById(1);
        verify(trackRepository).save(isA(Track.class));
    }

    /**
     * Method under test: {@link TrackService#addTagToTrack(Integer, Integer)}
     */
    @Test
    void testAddTagToTrack3() {
        // Arrange
        Tag tag = new Tag();
        tag.setId(1);
        tag.setText("Text");
        Track track = mock(Track.class);
        doNothing().when(track).addTag(Mockito.<Tag>any());
        doNothing().when(track).setArtist(Mockito.<String>any());
        doNothing().when(track).setId(Mockito.<Integer>any());
        doNothing().when(track).setName(Mockito.<String>any());
        doNothing().when(track).setPlaylists(Mockito.<Set<Playlist>>any());
        track.addTag(tag);
        track.setArtist("Artist");
        track.setId(1);
        track.setName("Name");
        track.setPlaylists(new HashSet<>());
        Optional<Track> ofResult = Optional.of(track);

        Tag tag2 = new Tag();
        tag2.setId(1);
        tag2.setText("Text");

        Track track2 = new Track();
        track2.addTag(tag2);
        track2.setArtist("Artist");
        track2.setId(1);
        track2.setName("Name");
        track2.setPlaylists(new HashSet<>());
        when(trackRepository.save(Mockito.<Track>any())).thenReturn(track2);
        when(trackRepository.findById(Mockito.<Integer>any())).thenReturn(ofResult);
        doNothing().when(entityCache).put(Mockito.<Integer>any(), Mockito.<Object>any());

        Tag tag3 = new Tag();
        tag3.setId(1);
        tag3.setText("Text");
        Optional<Tag> ofResult2 = Optional.of(tag3);
        when(tagRepository.findById(Mockito.<Integer>any())).thenReturn(ofResult2);

        // Act
        trackService.addTagToTrack(1, 1);

        // Assert
        verify(entityCache).put(eq(1799349139), isA(Object.class));
        verify(track, atLeast(1)).addTag(Mockito.<Tag>any());
        verify(track).setArtist("Artist");
        verify(track).setId(1);
        verify(track).setName("Name");
        verify(track).setPlaylists(isA(Set.class));
        verify(tagRepository).findById(1);
        verify(trackRepository, atLeast(1)).findById(1);
        verify(trackRepository).save(isA(Track.class));
    }

    /**
     * Method under test: {@link TrackService#addTagToTrack(Integer, Integer)}
     */
    @Test
    void testAddTagToTrack4() {
        // Arrange
        Optional<Track> emptyResult = Optional.empty();
        when(trackRepository.findById(Mockito.<Integer>any())).thenReturn(emptyResult);

        Tag tag = new Tag();
        tag.setId(1);
        tag.setText("Text");
        Optional<Tag> ofResult = Optional.of(tag);
        when(tagRepository.findById(Mockito.<Integer>any())).thenReturn(ofResult);

        // Act and Assert
        assertThrows(ResourceNotFoundException.class, () -> trackService.addTagToTrack(1, 1));
        verify(trackRepository).findById(1);
    }

    /**
     * Method under test: {@link TrackService#addTagToTrack(Integer, Integer)}
     */
    @Test
    void testAddTagToTrack5() {
        // Arrange
        Tag tag = new Tag();
        tag.setId(1);
        tag.setText("Text");
        Track track = mock(Track.class);
        doNothing().when(track).addTag(Mockito.<Tag>any());
        doNothing().when(track).setArtist(Mockito.<String>any());
        doNothing().when(track).setId(Mockito.<Integer>any());
        doNothing().when(track).setName(Mockito.<String>any());
        doNothing().when(track).setPlaylists(Mockito.<Set<Playlist>>any());
        track.addTag(tag);
        track.setArtist("Artist");
        track.setId(1);
        track.setName("Name");
        track.setPlaylists(new HashSet<>());
        Optional<Track> ofResult = Optional.of(track);
        when(trackRepository.findById(Mockito.<Integer>any())).thenReturn(ofResult);
        Optional<Tag> emptyResult = Optional.empty();
        when(tagRepository.findById(Mockito.<Integer>any())).thenReturn(emptyResult);

        // Act and Assert
        assertThrows(ResourceNotFoundException.class, () -> trackService.addTagToTrack(1, 1));
        verify(track).addTag(isA(Tag.class));
        verify(track).setArtist("Artist");
        verify(track).setId(1);
        verify(track).setName("Name");
        verify(track).setPlaylists(isA(Set.class));
        verify(tagRepository).findById(1);
        verify(trackRepository).findById(1);
    }

    /**
     * Method under test: {@link TrackService#removeTagFromTrack(Integer, Integer)}
     */
    @Test
    void testRemoveTagFromTrack() {
        // Arrange
        Tag tag = new Tag();
        tag.setId(1);
        tag.setText("Text");

        Track track = new Track();
        track.addTag(tag);
        track.setArtist("Artist");
        track.setId(1);
        track.setName("Name");
        track.setPlaylists(new HashSet<>());
        Optional<Track> ofResult = Optional.of(track);
        when(trackRepository.findById(Mockito.<Integer>any())).thenReturn(ofResult);

        Tag tag2 = new Tag();
        tag2.setId(1);
        tag2.setText("Text");
        Optional<Tag> ofResult2 = Optional.of(tag2);
        when(tagRepository.findById(Mockito.<Integer>any())).thenReturn(ofResult2);

        // Act and Assert
        assertThrows(IllegalArgumentException.class, () -> trackService.removeTagFromTrack(1, 1));
        verify(tagRepository).findById(1);
        verify(trackRepository).findById(1);
    }

    /**
     * Method under test: {@link TrackService#removeTagFromTrack(Integer, Integer)}
     */
    @Test
    void testRemoveTagFromTrack2() {
        // Arrange
        Tag tag = new Tag();
        tag.setId(1);
        tag.setText("Text");

        Track track = new Track();
        track.addTag(tag);
        track.setArtist("Artist");
        track.setId(1);
        track.setName("Name");
        track.setPlaylists(new HashSet<>());
        Optional<Track> ofResult = Optional.of(track);
        when(trackRepository.findById(Mockito.<Integer>any())).thenReturn(ofResult);
        when(tagRepository.findById(Mockito.<Integer>any())).thenThrow(new BadRequestErrorException("An error occurred"));

        // Act and Assert
        assertThrows(BadRequestErrorException.class, () -> trackService.removeTagFromTrack(1, 1));
        verify(tagRepository).findById(1);
        verify(trackRepository).findById(1);
    }

    /**
     * Method under test: {@link TrackService#removeTagFromTrack(Integer, Integer)}
     */
    @Test
    void testRemoveTagFromTrack3() {
        // Arrange
        Tag tag = new Tag();
        tag.setId(1);
        tag.setText("Text");
        Track track = mock(Track.class);
        when(track.getTags()).thenReturn(new ArrayList<>());
        doNothing().when(track).addTag(Mockito.<Tag>any());
        doNothing().when(track).setArtist(Mockito.<String>any());
        doNothing().when(track).setId(Mockito.<Integer>any());
        doNothing().when(track).setName(Mockito.<String>any());
        doNothing().when(track).setPlaylists(Mockito.<Set<Playlist>>any());
        track.addTag(tag);
        track.setArtist("Artist");
        track.setId(1);
        track.setName("Name");
        track.setPlaylists(new HashSet<>());
        Optional<Track> ofResult = Optional.of(track);
        when(trackRepository.findById(Mockito.<Integer>any())).thenReturn(ofResult);

        Tag tag2 = new Tag();
        tag2.setId(1);
        tag2.setText("Text");
        Optional<Tag> ofResult2 = Optional.of(tag2);
        when(tagRepository.findById(Mockito.<Integer>any())).thenReturn(ofResult2);

        // Act and Assert
        assertThrows(IllegalArgumentException.class, () -> trackService.removeTagFromTrack(1, 1));
        verify(track).addTag(isA(Tag.class));
        verify(track).getTags();
        verify(track).setArtist("Artist");
        verify(track).setId(1);
        verify(track).setName("Name");
        verify(track).setPlaylists(isA(Set.class));
        verify(tagRepository).findById(1);
        verify(trackRepository).findById(1);
    }

    /**
     * Method under test: {@link TrackService#removeTagFromTrack(Integer, Integer)}
     */
    @Test
    void testRemoveTagFromTrack4() {
        // Arrange
        Optional<Track> emptyResult = Optional.empty();
        when(trackRepository.findById(Mockito.<Integer>any())).thenReturn(emptyResult);

        Tag tag = new Tag();
        tag.setId(1);
        tag.setText("Text");
        Optional<Tag> ofResult = Optional.of(tag);
        when(tagRepository.findById(Mockito.<Integer>any())).thenReturn(ofResult);

        // Act and Assert
        assertThrows(ResourceNotFoundException.class, () -> trackService.removeTagFromTrack(1, 1));
        verify(trackRepository).findById(1);
    }

    /**
     * Method under test: {@link TrackService#removeTagFromTrack(Integer, Integer)}
     */
    @Test
    void testRemoveTagFromTrack5() {
        // Arrange
        Tag tag = new Tag();
        tag.setId(1);
        tag.setText("Text");
        Track track = mock(Track.class);
        doNothing().when(track).addTag(Mockito.<Tag>any());
        doNothing().when(track).setArtist(Mockito.<String>any());
        doNothing().when(track).setId(Mockito.<Integer>any());
        doNothing().when(track).setName(Mockito.<String>any());
        doNothing().when(track).setPlaylists(Mockito.<Set<Playlist>>any());
        track.addTag(tag);
        track.setArtist("Artist");
        track.setId(1);
        track.setName("Name");
        track.setPlaylists(new HashSet<>());
        Optional<Track> ofResult = Optional.of(track);
        when(trackRepository.findById(Mockito.<Integer>any())).thenReturn(ofResult);
        Optional<Tag> emptyResult = Optional.empty();
        when(tagRepository.findById(Mockito.<Integer>any())).thenReturn(emptyResult);

        // Act and Assert
        assertThrows(ResourceNotFoundException.class, () -> trackService.removeTagFromTrack(1, 1));
        verify(track).addTag(isA(Tag.class));
        verify(track).setArtist("Artist");
        verify(track).setId(1);
        verify(track).setName("Name");
        verify(track).setPlaylists(isA(Set.class));
        verify(tagRepository).findById(1);
        verify(trackRepository).findById(1);
    }
}
