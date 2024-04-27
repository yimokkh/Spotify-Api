package org.example.test.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
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
import org.example.test.entity.Track;
import org.example.test.exception.BadRequestErrorException;
import org.example.test.exception.ResourceNotFoundException;
import org.example.test.repository.TrackRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.ResponseEntity;
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
    private TrackRepository trackRepository;

    @Autowired
    private TrackService trackService;

    /**
     * Method under test: {@link TrackService#postTrack(Track)}
     */
    @Test
    void testPostTrack() {
        // Arrange
        Track track = new Track();
        track.setArtist("Artist");
        track.setId(1);
        track.setName("Name");
        track.setPlaylists(new HashSet<>());
        when(trackRepository.save(Mockito.<Track>any())).thenReturn(track);
        doNothing().when(entityCache).put(Mockito.<Integer>any(), Mockito.<Object>any());

        Track track2 = new Track();
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
        Track track = new Track();
        track.setArtist("Artist");
        track.setId(1);
        track.setName("Name");
        track.setPlaylists(new HashSet<>());
        when(trackRepository.save(Mockito.<Track>any())).thenReturn(track);
        doThrow(new BadRequestErrorException("An error occurred")).when(entityCache)
                .put(Mockito.<Integer>any(), Mockito.<Object>any());

        Track track2 = new Track();
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
     * Method under test: {@link TrackService#deleteTrackById(Integer)}
     */
    @Test
    void testDeleteTrackById() {
        // Arrange
        Track track = new Track();
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

        // Assert that nothing has changed
        verify(entityCache).put(eq(1569182950), isA(Object.class));
        verify(trackRepository).deleteById(eq(1));
        verify(trackRepository).findById(eq(1));
        verify(trackRepository).findAll();
    }

    /**
     * Method under test: {@link TrackService#deleteTrackById(Integer)}
     */
    @Test
    void testDeleteTrackById2() {
        // Arrange
        Track track = new Track();
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
        verify(trackRepository).deleteById(eq(1));
        verify(trackRepository).findById(eq(1));
        verify(trackRepository).findAll();
    }

    /**
     * Method under test: {@link TrackService#deleteTrackById(Integer)}
     */
    @Test
    void testDeleteTrackById3() {
        // Arrange
        Optional<Track> emptyResult = Optional.empty();
        when(trackRepository.findById(Mockito.<Integer>any())).thenReturn(emptyResult);

        // Act and Assert
        assertThrows(ResourceNotFoundException.class, () -> trackService.deleteTrackById(1));
        verify(trackRepository).findById(eq(1));
    }

    /**
     * Method under test: {@link TrackService#updateTrackNameById(Integer, String)}
     */
    @Test
    void testUpdateTrackNameById() {
        // Arrange
        Track track = new Track();
        track.setArtist("Artist");
        track.setId(1);
        track.setName("Name");
        track.setPlaylists(new HashSet<>());
        Optional<Track> ofResult = Optional.of(track);

        Track track2 = new Track();
        track2.setArtist("Artist");
        track2.setId(1);
        track2.setName("Name");
        track2.setPlaylists(new HashSet<>());
        when(trackRepository.save(Mockito.<Track>any())).thenReturn(track2);
        when(trackRepository.findById(Mockito.<Integer>any())).thenReturn(ofResult);
        doNothing().when(entityCache).put(Mockito.<Integer>any(), Mockito.<Object>any());

        // Act
        ResponseEntity<Object> actualUpdateTrackNameByIdResult = trackService.updateTrackNameById(1, "New Name");

        // Assert
        verify(entityCache).put(eq(1799349139), isA(Object.class));
        verify(trackRepository, atLeast(1)).findById(eq(1));
        verify(trackRepository).save(isA(Track.class));
        assertNull(actualUpdateTrackNameByIdResult.getBody());
        assertEquals(200, actualUpdateTrackNameByIdResult.getStatusCodeValue());
        assertTrue(actualUpdateTrackNameByIdResult.getHeaders().isEmpty());
    }

    /**
     * Method under test: {@link TrackService#updateTrackNameById(Integer, String)}
     */
    @Test
    void testUpdateTrackNameById2() {
        // Arrange
        Track track = new Track();
        track.setArtist("Artist");
        track.setId(1);
        track.setName("Name");
        track.setPlaylists(new HashSet<>());
        Optional<Track> ofResult = Optional.of(track);

        Track track2 = new Track();
        track2.setArtist("Artist");
        track2.setId(1);
        track2.setName("Name");
        track2.setPlaylists(new HashSet<>());
        when(trackRepository.save(Mockito.<Track>any())).thenReturn(track2);
        when(trackRepository.findById(Mockito.<Integer>any())).thenReturn(ofResult);
        doThrow(new BadRequestErrorException("An error occurred")).when(entityCache)
                .put(Mockito.<Integer>any(), Mockito.<Object>any());

        // Act and Assert
        assertThrows(BadRequestErrorException.class, () -> trackService.updateTrackNameById(1, "New Name"));
        verify(entityCache).put(eq(1799349139), isA(Object.class));
        verify(trackRepository, atLeast(1)).findById(eq(1));
        verify(trackRepository).save(isA(Track.class));
    }

    /**
     * Method under test: {@link TrackService#updateTrackNameById(Integer, String)}
     */
    @Test
    void testUpdateTrackNameById3() {
        // Arrange
        Optional<Track> emptyResult = Optional.empty();
        when(trackRepository.findById(Mockito.<Integer>any())).thenReturn(emptyResult);

        // Act and Assert
        assertThrows(ResourceNotFoundException.class, () -> trackService.updateTrackNameById(1, "New Name"));
        verify(trackRepository).findById(eq(1));
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
        Track track = new Track();
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
        Track track = new Track();
        track.setArtist("No tracks has been created!");
        track.setId(1);
        track.setName("No tracks has been created!");
        track.setPlaylists(new HashSet<>());

        Track track2 = new Track();
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
        Track track = new Track();
        track.setArtist("No tracks has been created!");
        track.setId(1);
        track.setName("No tracks has been created!");
        track.setPlaylists(new HashSet<>());

        ArrayList<Track> trackList = new ArrayList<>();
        trackList.add(track);
        when(trackRepository.findAll()).thenReturn(trackList);
        doThrow(new BadRequestErrorException("An error occurred")).when(entityCache)
                .put(Mockito.<Integer>any(), Mockito.<Object>any());

        // Act and Assert
        assertThrows(BadRequestErrorException.class, () -> trackService.getAllTracks());
        verify(entityCache).put(eq(1), isA(Object.class));
        verify(trackRepository).findAll();
    }

    /**
     * Method under test: {@link TrackService#getTrackById(Integer)}
     */
    @Test
    void testGetTrackById() {
        // Arrange
        when(entityCache.get(Mockito.<Integer>any())).thenThrow(new ResourceNotFoundException("An error occurred"));

        // Act and Assert
        assertThrows(ResourceNotFoundException.class, () -> trackService.getTrackById(1));
        verify(entityCache).get(eq(1799349139));
    }

    /**
     * Method under test: {@link TrackService#getTrackById(Integer)}
     */
    @Test
    void testGetTrackById2() {
        // Arrange
        Track track = mock(Track.class);
        doNothing().when(track).setArtist(Mockito.<String>any());
        doNothing().when(track).setId(Mockito.<Integer>any());
        doNothing().when(track).setName(Mockito.<String>any());
        doNothing().when(track).setPlaylists(Mockito.<Set<Playlist>>any());
        track.setArtist("Artist");
        track.setId(1);
        track.setName("Name");
        track.setPlaylists(new HashSet<>());
        Optional.of(track);

        Track track2 = new Track();
        track2.setArtist("track_by_id");
        track2.setId(1);
        track2.setName("track_by_id");
        track2.setPlaylists(new HashSet<>());
        when(entityCache.get(Mockito.<Integer>any())).thenReturn(track2);

        // Act
        Optional<Track> actualTrackById = trackService.getTrackById(1);

        // Assert
        verify(entityCache).get(eq(1799349139));
        verify(track).setArtist(eq("Artist"));
        verify(track).setId(eq(1));
        verify(track).setName(eq("Name"));
        verify(track).setPlaylists(isA(Set.class));
        assertTrue(actualTrackById.isPresent());
    }

    /**
     * Method under test: {@link TrackService#getTrackById(Integer)}
     */
    @Test
    void testGetTrackById3() {
        // Arrange
        Track track = mock(Track.class);
        doNothing().when(track).setArtist(Mockito.<String>any());
        doNothing().when(track).setId(Mockito.<Integer>any());
        doNothing().when(track).setName(Mockito.<String>any());
        doNothing().when(track).setPlaylists(Mockito.<Set<Playlist>>any());
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
        verify(entityCache).get(eq(1799349139));
        verify(entityCache).put(eq(1799349139), isA(Object.class));
        verify(track).setArtist(eq("Artist"));
        verify(track).setId(eq(1));
        verify(track).setName(eq("Name"));
        verify(track).setPlaylists(isA(Set.class));
        verify(trackRepository).findById(eq(1));
        assertTrue(actualTrackById.isPresent());
        assertEquals(ofResult, actualTrackById);
    }
}
