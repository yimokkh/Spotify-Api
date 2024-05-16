package org.example.test.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
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

import org.example.test.cache.EntityCache;
import org.example.test.entity.Playlist;
import org.example.test.entity.Tag;
import org.example.test.entity.Track;
import org.example.test.entity.User;
import org.example.test.exception.ResourceNotFoundException;
import org.example.test.repository.PlaylistRepository;
import org.example.test.repository.TrackRepository;
import org.example.test.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.aot.DisabledInAotMode;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ContextConfiguration(classes = {PlaylistService.class})
@ExtendWith(SpringExtension.class)
@DisabledInAotMode
class PlaylistServiceTest {
    @MockBean
    private EntityCache<Integer, Object> entityCache;

    @MockBean
    private PlaylistRepository playlistRepository;

    @Autowired
    private PlaylistService playlistService;

    @MockBean
    private TrackRepository trackRepository;

    @MockBean
    private UserRepository userRepository;

    /**
     * Method under test: {@link PlaylistService#findPlaylistsByTrackName(String)}
     */
    @Test
    void testFindPlaylistsByTrackName() {
        // Arrange
        ArrayList<Playlist> playlistList = new ArrayList<>();
        when(playlistRepository.findPlaylistsByTrackName(Mockito.<String>any())).thenReturn(playlistList);

        // Act
        List<Playlist> actualFindPlaylistsByTrackNameResult = playlistService.findPlaylistsByTrackName("Name");

        // Assert
        verify(playlistRepository).findPlaylistsByTrackName("Name");
        assertTrue(actualFindPlaylistsByTrackNameResult.isEmpty());
        assertSame(playlistList, actualFindPlaylistsByTrackNameResult);
    }

    /**
     * Method under test: {@link PlaylistService#findPlaylistsByTrackName(String)}
     */
    @Test
    void testFindPlaylistsByTrackName2() {
        // Arrange
        when(playlistRepository.findPlaylistsByTrackName(Mockito.<String>any()))
                .thenThrow(new ResourceNotFoundException("An error occurred"));

        // Act and Assert
        assertThrows(ResourceNotFoundException.class, () -> playlistService.findPlaylistsByTrackName("Name"));
        verify(playlistRepository).findPlaylistsByTrackName("Name");
    }

    /**
     * Method under test: {@link PlaylistService#deletePlaylistById(Integer)}
     */
    @Test
    void testDeletePlaylistById() {
        // Arrange
        User user = new User();
        user.setId(1);
        user.setName("Name");
        user.setPlaylists(new ArrayList<>());

        Playlist playlist = new Playlist();
        playlist.setId(1);
        playlist.setName("Name");
        playlist.setTracks(new ArrayList<>());
        playlist.setUser(user);
        Optional<Playlist> ofResult = Optional.of(playlist);
        when(playlistRepository.findAll()).thenReturn(new ArrayList<>());
        doNothing().when(playlistRepository).deleteById(Mockito.<Integer>any());
        when(playlistRepository.findById(Mockito.<Integer>any())).thenReturn(ofResult);
        doNothing().when(entityCache).put(Mockito.<Integer>any(), Mockito.<Object>any());

        // Act
        playlistService.deletePlaylistById(1);

        // Assert
        verify(entityCache).put(eq(-1491249213), isA(Object.class));
        verify(playlistRepository).deleteById(1);
        verify(playlistRepository).findById(1);
        verify(playlistRepository).findAll();
    }

    /**
     * Method under test: {@link PlaylistService#deletePlaylistById(Integer)}
     */
    @Test
    void testDeletePlaylistById2() {
        // Arrange
        User user = new User();
        user.setId(1);
        user.setName("Name");
        user.setPlaylists(new ArrayList<>());

        Playlist playlist = new Playlist();
        playlist.setId(1);
        playlist.setName("Name");
        playlist.setTracks(new ArrayList<>());
        playlist.setUser(user);
        Optional<Playlist> ofResult = Optional.of(playlist);
        when(playlistRepository.findAll()).thenReturn(new ArrayList<>());
        doNothing().when(playlistRepository).deleteById(Mockito.<Integer>any());
        when(playlistRepository.findById(Mockito.<Integer>any())).thenReturn(ofResult);
        doThrow(new ResourceNotFoundException("An error occurred")).when(entityCache)
                .put(Mockito.<Integer>any(), Mockito.<Object>any());

        // Act and Assert
        assertThrows(ResourceNotFoundException.class, () -> playlistService.deletePlaylistById(1));
        verify(entityCache).put(eq(-1491249213), isA(Object.class));
        verify(playlistRepository).deleteById(1);
        verify(playlistRepository).findById(1);
        verify(playlistRepository).findAll();
    }

    /**
     * Method under test: {@link PlaylistService#deletePlaylistById(Integer)}
     */
    @Test
    void testDeletePlaylistById3() {
        // Arrange
        User user = new User();
        user.setId(1);
        user.setName("Name");
        user.setPlaylists(new ArrayList<>());

        Playlist playlist = new Playlist();
        playlist.setId(1);
        playlist.setName("Name");
        playlist.setTracks(new ArrayList<>());
        playlist.setUser(user);
        Optional<Playlist> ofResult = Optional.of(playlist);

        User user2 = new User();
        user2.setId(1);
        user2.setName("all_playlists");
        user2.setPlaylists(new ArrayList<>());

        Playlist playlist2 = new Playlist();
        playlist2.setId(1);
        playlist2.setName("all_playlists");
        playlist2.setTracks(new ArrayList<>());
        playlist2.setUser(user2);

        ArrayList<Playlist> playlistList = new ArrayList<>();
        playlistList.add(playlist2);
        when(playlistRepository.findAll()).thenReturn(playlistList);
        doNothing().when(playlistRepository).deleteById(Mockito.<Integer>any());
        when(playlistRepository.findById(Mockito.<Integer>any())).thenReturn(ofResult);
        doNothing().when(entityCache).put(Mockito.<Integer>any(), Mockito.<Object>any());

        // Act
        playlistService.deletePlaylistById(1);

        // Assert
        verify(entityCache, atLeast(1)).put(Mockito.<Integer>any(), Mockito.<Object>any());
        verify(playlistRepository).deleteById(1);
        verify(playlistRepository).findById(1);
        verify(playlistRepository).findAll();
    }

    /**
     * Method under test: {@link PlaylistService#deletePlaylistById(Integer)}
     */
    @Test
    void testDeletePlaylistById4() {
        // Arrange
        User user = new User();
        user.setId(1);
        user.setName("Name");
        user.setPlaylists(new ArrayList<>());

        Playlist playlist = new Playlist();
        playlist.setId(1);
        playlist.setName("Name");
        playlist.setTracks(new ArrayList<>());
        playlist.setUser(user);
        Optional<Playlist> ofResult = Optional.of(playlist);

        User user2 = new User();
        user2.setId(1);
        user2.setName("all_playlists");
        user2.setPlaylists(new ArrayList<>());

        Playlist playlist2 = new Playlist();
        playlist2.setId(1);
        playlist2.setName("all_playlists");
        playlist2.setTracks(new ArrayList<>());
        playlist2.setUser(user2);

        User user3 = new User();
        user3.setId(2);
        user3.setName("Name");
        user3.setPlaylists(new ArrayList<>());

        Playlist playlist3 = new Playlist();
        playlist3.setId(2);
        playlist3.setName("Name");
        playlist3.setTracks(new ArrayList<>());
        playlist3.setUser(user3);

        ArrayList<Playlist> playlistList = new ArrayList<>();
        playlistList.add(playlist3);
        playlistList.add(playlist2);
        when(playlistRepository.findAll()).thenReturn(playlistList);
        doNothing().when(playlistRepository).deleteById(Mockito.<Integer>any());
        when(playlistRepository.findById(Mockito.<Integer>any())).thenReturn(ofResult);
        doNothing().when(entityCache).put(Mockito.<Integer>any(), Mockito.<Object>any());

        // Act
        playlistService.deletePlaylistById(1);

        // Assert
        verify(entityCache, atLeast(1)).put(Mockito.<Integer>any(), Mockito.<Object>any());
        verify(playlistRepository).deleteById(1);
        verify(playlistRepository).findById(1);
        verify(playlistRepository).findAll();
    }

    /**
     * Method under test: {@link PlaylistService#deletePlaylistById(Integer)}
     */
    @Test
    void testDeletePlaylistById5() {
        // Arrange
        User user = new User();
        user.setId(1);
        user.setName("Name");
        user.setPlaylists(new ArrayList<>());
        Playlist playlist = mock(Playlist.class);
        when(playlist.getTracks()).thenReturn(new ArrayList<>());
        doNothing().when(playlist).setId(Mockito.<Integer>any());
        doNothing().when(playlist).setName(Mockito.<String>any());
        doNothing().when(playlist).setTracks(Mockito.<List<Track>>any());
        doNothing().when(playlist).setUser(Mockito.<User>any());
        playlist.setId(1);
        playlist.setName("Name");
        playlist.setTracks(new ArrayList<>());
        playlist.setUser(user);
        Optional<Playlist> ofResult = Optional.of(playlist);
        when(playlistRepository.findAll()).thenReturn(new ArrayList<>());
        doNothing().when(playlistRepository).deleteById(Mockito.<Integer>any());
        when(playlistRepository.findById(Mockito.<Integer>any())).thenReturn(ofResult);
        doNothing().when(entityCache).put(Mockito.<Integer>any(), Mockito.<Object>any());

        // Act
        playlistService.deletePlaylistById(1);

        // Assert
        verify(entityCache).put(eq(-1491249213), isA(Object.class));
        verify(playlist, atLeast(1)).getTracks();
        verify(playlist).setId(1);
        verify(playlist).setName("Name");
        verify(playlist).setTracks(isA(List.class));
        verify(playlist).setUser(isA(User.class));
        verify(playlistRepository).deleteById(1);
        verify(playlistRepository).findById(1);
        verify(playlistRepository).findAll();
    }

    /**
     * Method under test: {@link PlaylistService#deletePlaylistById(Integer)}
     */
    @Test
    void testDeletePlaylistById6() {
        // Arrange
        User user = new User();
        user.setId(1);
        user.setName("Name");
        user.setPlaylists(new ArrayList<>());

        Tag tag = new Tag();
        tag.setId(1);
        tag.setText("all_playlists");

        Track track = new Track();
        track.addTag(tag);
        track.setArtist("all_playlists");
        track.setId(1);
        track.setName("all_playlists");
        track.setPlaylists(new HashSet<>());

        ArrayList<Track> trackList = new ArrayList<>();
        trackList.add(track);
        Playlist playlist = mock(Playlist.class);
        when(playlist.getTracks()).thenReturn(trackList);
        doNothing().when(playlist).setId(Mockito.<Integer>any());
        doNothing().when(playlist).setName(Mockito.<String>any());
        doNothing().when(playlist).setTracks(Mockito.<List<Track>>any());
        doNothing().when(playlist).setUser(Mockito.<User>any());
        playlist.setId(1);
        playlist.setName("Name");
        playlist.setTracks(new ArrayList<>());
        playlist.setUser(user);
        Optional<Playlist> ofResult = Optional.of(playlist);
        when(playlistRepository.findAll()).thenReturn(new ArrayList<>());
        doNothing().when(playlistRepository).deleteById(Mockito.<Integer>any());
        when(playlistRepository.findById(Mockito.<Integer>any())).thenReturn(ofResult);
        doNothing().when(entityCache).put(Mockito.<Integer>any(), Mockito.<Object>any());

        // Act
        playlistService.deletePlaylistById(1);

        // Assert
        verify(entityCache).put(eq(-1491249213), isA(Object.class));
        verify(playlist, atLeast(1)).getTracks();
        verify(playlist).setId(1);
        verify(playlist).setName("Name");
        verify(playlist).setTracks(isA(List.class));
        verify(playlist).setUser(isA(User.class));
        verify(playlistRepository).deleteById(1);
        verify(playlistRepository).findById(1);
        verify(playlistRepository).findAll();
    }

    /**
     * Method under test: {@link PlaylistService#deletePlaylistById(Integer)}
     */
    @Test
    void testDeletePlaylistById7() {
        // Arrange
        User user = new User();
        user.setId(1);
        user.setName("Name");
        user.setPlaylists(new ArrayList<>());

        Tag tag = new Tag();
        tag.setId(1);
        tag.setText("all_playlists");

        Track track = new Track();
        track.addTag(tag);
        track.setArtist("all_playlists");
        track.setId(1);
        track.setName("all_playlists");
        track.setPlaylists(new HashSet<>());

        Tag tag2 = new Tag();
        tag2.setId(2);
        tag2.setText("Text");

        Track track2 = new Track();
        track2.addTag(tag2);
        track2.setArtist("Artist");
        track2.setId(2);
        track2.setName("Name");
        track2.setPlaylists(new HashSet<>());

        ArrayList<Track> trackList = new ArrayList<>();
        trackList.add(track2);
        trackList.add(track);
        Playlist playlist = mock(Playlist.class);
        when(playlist.getTracks()).thenReturn(trackList);
        doNothing().when(playlist).setId(Mockito.<Integer>any());
        doNothing().when(playlist).setName(Mockito.<String>any());
        doNothing().when(playlist).setTracks(Mockito.<List<Track>>any());
        doNothing().when(playlist).setUser(Mockito.<User>any());
        playlist.setId(1);
        playlist.setName("Name");
        playlist.setTracks(new ArrayList<>());
        playlist.setUser(user);
        Optional<Playlist> ofResult = Optional.of(playlist);
        when(playlistRepository.findAll()).thenReturn(new ArrayList<>());
        doNothing().when(playlistRepository).deleteById(Mockito.<Integer>any());
        when(playlistRepository.findById(Mockito.<Integer>any())).thenReturn(ofResult);
        doNothing().when(entityCache).put(Mockito.<Integer>any(), Mockito.<Object>any());

        // Act
        playlistService.deletePlaylistById(1);

        // Assert
        verify(entityCache).put(eq(-1491249213), isA(Object.class));
        verify(playlist, atLeast(1)).getTracks();
        verify(playlist).setId(1);
        verify(playlist).setName("Name");
        verify(playlist).setTracks(isA(List.class));
        verify(playlist).setUser(isA(User.class));
        verify(playlistRepository).deleteById(1);
        verify(playlistRepository).findById(1);
        verify(playlistRepository).findAll();
    }

    /**
     * Method under test: {@link PlaylistService#deletePlaylistById(Integer)}
     */
    @Test
    void testDeletePlaylistById8() {
        // Arrange
        Optional<Playlist> emptyResult = Optional.empty();
        when(playlistRepository.findById(Mockito.<Integer>any())).thenReturn(emptyResult);

        // Act and Assert
        assertThrows(ResourceNotFoundException.class, () -> playlistService.deletePlaylistById(1));
        verify(playlistRepository).findById(1);
    }

    /**
     * Method under test:
     * {@link PlaylistService#updatePlaylistNameById(Integer, String)}
     */
    @Test
    void testUpdatePlaylistNameById() {
        // Arrange
        User user = new User();
        user.setId(1);
        user.setName("Name");
        user.setPlaylists(new ArrayList<>());

        Playlist playlist = new Playlist();
        playlist.setId(1);
        playlist.setName("Name");
        playlist.setTracks(new ArrayList<>());
        playlist.setUser(user);
        Optional<Playlist> ofResult = Optional.of(playlist);

        User user2 = new User();
        user2.setId(1);
        user2.setName("Name");
        user2.setPlaylists(new ArrayList<>());

        Playlist playlist2 = new Playlist();
        playlist2.setId(1);
        playlist2.setName("Name");
        playlist2.setTracks(new ArrayList<>());
        playlist2.setUser(user2);
        when(playlistRepository.save(Mockito.<Playlist>any())).thenReturn(playlist2);
        when(playlistRepository.findById(Mockito.<Integer>any())).thenReturn(ofResult);
        doNothing().when(entityCache).put(Mockito.<Integer>any(), Mockito.<Object>any());

        // Act
        ResponseEntity<Object> actualUpdatePlaylistNameByIdResult = playlistService.updatePlaylistNameById(1, "New Name");

        // Assert
        verify(entityCache).put(eq(-1693582036), isA(Object.class));
        verify(playlistRepository, atLeast(1)).findById(1);
        verify(playlistRepository).save(isA(Playlist.class));
        assertNull(actualUpdatePlaylistNameByIdResult.getBody());
        assertEquals(200, actualUpdatePlaylistNameByIdResult.getStatusCodeValue());
        assertTrue(actualUpdatePlaylistNameByIdResult.getHeaders().isEmpty());
    }

    /**
     * Method under test:
     * {@link PlaylistService#updatePlaylistNameById(Integer, String)}
     */
    @Test
    void testUpdatePlaylistNameById2() {
        // Arrange
        User user = new User();
        user.setId(1);
        user.setName("Name");
        user.setPlaylists(new ArrayList<>());

        Playlist playlist = new Playlist();
        playlist.setId(1);
        playlist.setName("Name");
        playlist.setTracks(new ArrayList<>());
        playlist.setUser(user);
        Optional<Playlist> ofResult = Optional.of(playlist);

        User user2 = new User();
        user2.setId(1);
        user2.setName("Name");
        user2.setPlaylists(new ArrayList<>());

        Playlist playlist2 = new Playlist();
        playlist2.setId(1);
        playlist2.setName("Name");
        playlist2.setTracks(new ArrayList<>());
        playlist2.setUser(user2);
        when(playlistRepository.save(Mockito.<Playlist>any())).thenReturn(playlist2);
        when(playlistRepository.findById(Mockito.<Integer>any())).thenReturn(ofResult);
        doThrow(new ResourceNotFoundException("An error occurred")).when(entityCache)
                .put(Mockito.<Integer>any(), Mockito.<Object>any());

        // Act and Assert
        assertThrows(ResourceNotFoundException.class, () -> playlistService.updatePlaylistNameById(1, "New Name"));
        verify(entityCache).put(eq(-1693582036), isA(Object.class));
        verify(playlistRepository, atLeast(1)).findById(1);
        verify(playlistRepository).save(isA(Playlist.class));
    }

    /**
     * Method under test:
     * {@link PlaylistService#updatePlaylistNameById(Integer, String)}
     */
    @Test
    void testUpdatePlaylistNameById3() {
        // Arrange
        User user = new User();
        user.setId(1);
        user.setName("Name");
        user.setPlaylists(new ArrayList<>());
        Playlist playlist = mock(Playlist.class);
        doNothing().when(playlist).setId(Mockito.<Integer>any());
        doNothing().when(playlist).setName(Mockito.<String>any());
        doNothing().when(playlist).setTracks(Mockito.<List<Track>>any());
        doNothing().when(playlist).setUser(Mockito.<User>any());
        playlist.setId(1);
        playlist.setName("Name");
        playlist.setTracks(new ArrayList<>());
        playlist.setUser(user);
        Optional<Playlist> ofResult = Optional.of(playlist);

        User user2 = new User();
        user2.setId(1);
        user2.setName("Name");
        user2.setPlaylists(new ArrayList<>());

        Playlist playlist2 = new Playlist();
        playlist2.setId(1);
        playlist2.setName("Name");
        playlist2.setTracks(new ArrayList<>());
        playlist2.setUser(user2);
        when(playlistRepository.save(Mockito.<Playlist>any())).thenReturn(playlist2);
        when(playlistRepository.findById(Mockito.<Integer>any())).thenReturn(ofResult);
        doNothing().when(entityCache).put(Mockito.<Integer>any(), Mockito.<Object>any());

        // Act
        ResponseEntity<Object> actualUpdatePlaylistNameByIdResult = playlistService.updatePlaylistNameById(1, "New Name");

        // Assert
        verify(entityCache).put(eq(-1693582036), isA(Object.class));
        verify(playlist).setId(1);
        verify(playlist, atLeast(1)).setName(Mockito.<String>any());
        verify(playlist).setTracks(isA(List.class));
        verify(playlist).setUser(isA(User.class));
        verify(playlistRepository, atLeast(1)).findById(1);
        verify(playlistRepository).save(isA(Playlist.class));
        assertNull(actualUpdatePlaylistNameByIdResult.getBody());
        assertEquals(200, actualUpdatePlaylistNameByIdResult.getStatusCodeValue());
        assertTrue(actualUpdatePlaylistNameByIdResult.getHeaders().isEmpty());
    }

    /**
     * Method under test:
     * {@link PlaylistService#updatePlaylistNameById(Integer, String)}
     */
    @Test
    void testUpdatePlaylistNameById4() {
        // Arrange
        Optional<Playlist> emptyResult = Optional.empty();
        when(playlistRepository.findById(Mockito.<Integer>any())).thenReturn(emptyResult);

        // Act and Assert
        assertThrows(ResourceNotFoundException.class, () -> playlistService.updatePlaylistNameById(1, "New Name"));
        verify(playlistRepository).findById(1);
    }

    /**
     * Method under test:
     * {@link PlaylistService#addTrackToPlaylist(Integer, Integer)}
     */
    @Test
    void testAddTrackToPlaylist() {
        // Arrange
        User user = new User();
        user.setId(1);
        user.setName("Name");
        user.setPlaylists(new ArrayList<>());

        Playlist playlist = new Playlist();
        playlist.setId(1);
        playlist.setName("Name");
        playlist.setTracks(new ArrayList<>());
        playlist.setUser(user);
        Optional<Playlist> ofResult = Optional.of(playlist);

        User user2 = new User();
        user2.setId(1);
        user2.setName("Name");
        user2.setPlaylists(new ArrayList<>());

        Playlist playlist2 = new Playlist();
        playlist2.setId(1);
        playlist2.setName("Name");
        playlist2.setTracks(new ArrayList<>());
        playlist2.setUser(user2);
        when(playlistRepository.save(Mockito.<Playlist>any())).thenReturn(playlist2);
        when(playlistRepository.findById(Mockito.<Integer>any())).thenReturn(ofResult);

        Tag tag = new Tag();
        tag.setId(1);
        tag.setText("Text");

        Track track = new Track();
        track.addTag(tag);
        track.setArtist("Artist");
        track.setId(1);
        track.setName("Name");
        track.setPlaylists(new HashSet<>());
        Optional<Track> ofResult2 = Optional.of(track);
        when(trackRepository.findById(Mockito.<Integer>any())).thenReturn(ofResult2);
        doNothing().when(entityCache).put(Mockito.<Integer>any(), Mockito.<Object>any());

        // Act
        Track actualAddTrackToPlaylistResult = playlistService.addTrackToPlaylist(1, 1);

        // Assert
        verify(entityCache).put(eq(-1693582036), isA(Object.class));
        verify(trackRepository).findById(1);
        verify(playlistRepository, atLeast(1)).findById(1);
        verify(playlistRepository).save(isA(Playlist.class));
        assertSame(track, actualAddTrackToPlaylistResult);
    }

    /**
     * Method under test:
     * {@link PlaylistService#addTrackToPlaylist(Integer, Integer)}
     */
    @Test
    void testAddTrackToPlaylist2() {
        // Arrange
        User user = new User();
        user.setId(1);
        user.setName("Name");
        user.setPlaylists(new ArrayList<>());

        Playlist playlist = new Playlist();
        playlist.setId(1);
        playlist.setName("Name");
        playlist.setTracks(new ArrayList<>());
        playlist.setUser(user);
        Optional<Playlist> ofResult = Optional.of(playlist);

        User user2 = new User();
        user2.setId(1);
        user2.setName("Name");
        user2.setPlaylists(new ArrayList<>());

        Playlist playlist2 = new Playlist();
        playlist2.setId(1);
        playlist2.setName("Name");
        playlist2.setTracks(new ArrayList<>());
        playlist2.setUser(user2);
        when(playlistRepository.save(Mockito.<Playlist>any())).thenReturn(playlist2);
        when(playlistRepository.findById(Mockito.<Integer>any())).thenReturn(ofResult);

        Tag tag = new Tag();
        tag.setId(1);
        tag.setText("Text");

        Track track = new Track();
        track.addTag(tag);
        track.setArtist("Artist");
        track.setId(1);
        track.setName("Name");
        track.setPlaylists(new HashSet<>());
        Optional<Track> ofResult2 = Optional.of(track);
        when(trackRepository.findById(Mockito.<Integer>any())).thenReturn(ofResult2);
        doThrow(new ResourceNotFoundException("An error occurred")).when(entityCache)
                .put(Mockito.<Integer>any(), Mockito.<Object>any());

        // Act and Assert
        assertThrows(ResourceNotFoundException.class, () -> playlistService.addTrackToPlaylist(1, 1));
        verify(entityCache).put(eq(-1693582036), isA(Object.class));
        verify(trackRepository).findById(1);
        verify(playlistRepository, atLeast(1)).findById(1);
        verify(playlistRepository).save(isA(Playlist.class));
    }

    /**
     * Method under test:
     * {@link PlaylistService#addTrackToPlaylist(Integer, Integer)}
     */
    @Test
    void testAddTrackToPlaylist3() {
        // Arrange
        User user = new User();
        user.setId(1);
        user.setName("Name");
        user.setPlaylists(new ArrayList<>());
        Playlist playlist = mock(Playlist.class);
        when(playlist.getTracks()).thenReturn(new ArrayList<>());
        doNothing().when(playlist).addTrack(Mockito.<Track>any());
        doNothing().when(playlist).setId(Mockito.<Integer>any());
        doNothing().when(playlist).setName(Mockito.<String>any());
        doNothing().when(playlist).setTracks(Mockito.<List<Track>>any());
        doNothing().when(playlist).setUser(Mockito.<User>any());
        playlist.setId(1);
        playlist.setName("Name");
        playlist.setTracks(new ArrayList<>());
        playlist.setUser(user);
        Optional<Playlist> ofResult = Optional.of(playlist);

        User user2 = new User();
        user2.setId(1);
        user2.setName("Name");
        user2.setPlaylists(new ArrayList<>());

        Playlist playlist2 = new Playlist();
        playlist2.setId(1);
        playlist2.setName("Name");
        playlist2.setTracks(new ArrayList<>());
        playlist2.setUser(user2);
        when(playlistRepository.save(Mockito.<Playlist>any())).thenReturn(playlist2);
        when(playlistRepository.findById(Mockito.<Integer>any())).thenReturn(ofResult);

        Tag tag = new Tag();
        tag.setId(1);
        tag.setText("Text");

        Track track = new Track();
        track.addTag(tag);
        track.setArtist("Artist");
        track.setId(1);
        track.setName("Name");
        track.setPlaylists(new HashSet<>());
        Optional<Track> ofResult2 = Optional.of(track);
        when(trackRepository.findById(Mockito.<Integer>any())).thenReturn(ofResult2);
        doNothing().when(entityCache).put(Mockito.<Integer>any(), Mockito.<Object>any());

        // Act
        Track actualAddTrackToPlaylistResult = playlistService.addTrackToPlaylist(1, 1);

        // Assert
        verify(entityCache).put(eq(-1693582036), isA(Object.class));
        verify(playlist).addTrack(isA(Track.class));
        verify(playlist).getTracks();
        verify(playlist).setId(1);
        verify(playlist).setName("Name");
        verify(playlist).setTracks(isA(List.class));
        verify(playlist).setUser(isA(User.class));
        verify(trackRepository).findById(1);
        verify(playlistRepository, atLeast(1)).findById(1);
        verify(playlistRepository).save(isA(Playlist.class));
        assertSame(track, actualAddTrackToPlaylistResult);
    }

    /**
     * Method under test:
     * {@link PlaylistService#addTrackToPlaylist(Integer, Integer)}
     */
    @Test
    void testAddTrackToPlaylist4() {
        // Arrange
        Optional<Playlist> emptyResult = Optional.empty();
        when(playlistRepository.findById(Mockito.<Integer>any())).thenReturn(emptyResult);

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

        // Act and Assert
        assertThrows(ResourceNotFoundException.class, () -> playlistService.addTrackToPlaylist(1, 1));
        verify(playlistRepository).findById(1);
    }

    /**
     * Method under test:
     * {@link PlaylistService#addTrackToPlaylist(Integer, Integer)}
     */
    @Test
    void testAddTrackToPlaylist5() {
        // Arrange
        User user = new User();
        user.setId(1);
        user.setName("Name");
        user.setPlaylists(new ArrayList<>());
        Playlist playlist = mock(Playlist.class);
        doNothing().when(playlist).setId(Mockito.<Integer>any());
        doNothing().when(playlist).setName(Mockito.<String>any());
        doNothing().when(playlist).setTracks(Mockito.<List<Track>>any());
        doNothing().when(playlist).setUser(Mockito.<User>any());
        playlist.setId(1);
        playlist.setName("Name");
        playlist.setTracks(new ArrayList<>());
        playlist.setUser(user);
        Optional<Playlist> ofResult = Optional.of(playlist);
        when(playlistRepository.findById(Mockito.<Integer>any())).thenReturn(ofResult);
        Optional<Track> emptyResult = Optional.empty();
        when(trackRepository.findById(Mockito.<Integer>any())).thenReturn(emptyResult);

        // Act and Assert
        assertThrows(ResourceNotFoundException.class, () -> playlistService.addTrackToPlaylist(1, 1));
        verify(playlist).setId(1);
        verify(playlist).setName("Name");
        verify(playlist).setTracks(isA(List.class));
        verify(playlist).setUser(isA(User.class));
        verify(playlistRepository).findById(1);
        verify(trackRepository).findById(1);
    }

    /**
     * Method under test:
     * {@link PlaylistService#removeTrackFromPlaylist(Integer, Integer)}
     */
    @Test
    void testRemoveTrackFromPlaylist() {
        // Arrange
        User user = new User();
        user.setId(1);
        user.setName("Name");
        user.setPlaylists(new ArrayList<>());

        Playlist playlist = new Playlist();
        playlist.setId(1);
        playlist.setName("Name");
        playlist.setTracks(new ArrayList<>());
        playlist.setUser(user);
        Optional<Playlist> ofResult = Optional.of(playlist);
        when(playlistRepository.findById(Mockito.<Integer>any())).thenReturn(ofResult);

        Tag tag = new Tag();
        tag.setId(1);
        tag.setText("Text");

        Track track = new Track();
        track.addTag(tag);
        track.setArtist("Artist");
        track.setId(1);
        track.setName("Name");
        track.setPlaylists(new HashSet<>());
        Optional<Track> ofResult2 = Optional.of(track);
        when(trackRepository.findById(Mockito.<Integer>any())).thenReturn(ofResult2);

        // Act and Assert
        assertThrows(IllegalArgumentException.class, () -> playlistService.removeTrackFromPlaylist(1, 1));
        verify(playlistRepository).findById(1);
        verify(trackRepository).findById(1);
    }

    /**
     * Method under test:
     * {@link PlaylistService#removeTrackFromPlaylist(Integer, Integer)}
     */
    @Test
    void testRemoveTrackFromPlaylist2() {
        // Arrange
        User user = new User();
        user.setId(1);
        user.setName("Name");
        user.setPlaylists(new ArrayList<>());

        Playlist playlist = new Playlist();
        playlist.setId(1);
        playlist.setName("Name");
        playlist.setTracks(new ArrayList<>());
        playlist.setUser(user);
        Optional<Playlist> ofResult = Optional.of(playlist);
        when(playlistRepository.findById(Mockito.<Integer>any())).thenReturn(ofResult);
        when(trackRepository.findById(Mockito.<Integer>any()))
                .thenThrow(new ResourceNotFoundException("An error occurred"));

        // Act and Assert
        assertThrows(ResourceNotFoundException.class, () -> playlistService.removeTrackFromPlaylist(1, 1));
        verify(playlistRepository).findById(1);
        verify(trackRepository).findById(1);
    }

    /**
     * Method under test:
     * {@link PlaylistService#removeTrackFromPlaylist(Integer, Integer)}
     */
    @Test
    void testRemoveTrackFromPlaylist3() {
        // Arrange
        User user = new User();
        user.setId(1);
        user.setName("Name");
        user.setPlaylists(new ArrayList<>());
        Playlist playlist = mock(Playlist.class);
        when(playlist.getTracks()).thenReturn(new ArrayList<>());
        doNothing().when(playlist).setId(Mockito.<Integer>any());
        doNothing().when(playlist).setName(Mockito.<String>any());
        doNothing().when(playlist).setTracks(Mockito.<List<Track>>any());
        doNothing().when(playlist).setUser(Mockito.<User>any());
        playlist.setId(1);
        playlist.setName("Name");
        playlist.setTracks(new ArrayList<>());
        playlist.setUser(user);
        Optional<Playlist> ofResult = Optional.of(playlist);
        when(playlistRepository.findById(Mockito.<Integer>any())).thenReturn(ofResult);

        Tag tag = new Tag();
        tag.setId(1);
        tag.setText("Text");

        Track track = new Track();
        track.addTag(tag);
        track.setArtist("Artist");
        track.setId(1);
        track.setName("Name");
        track.setPlaylists(new HashSet<>());
        Optional<Track> ofResult2 = Optional.of(track);
        when(trackRepository.findById(Mockito.<Integer>any())).thenReturn(ofResult2);

        // Act and Assert
        assertThrows(IllegalArgumentException.class, () -> playlistService.removeTrackFromPlaylist(1, 1));
        verify(playlist).getTracks();
        verify(playlist).setId(1);
        verify(playlist).setName("Name");
        verify(playlist).setTracks(isA(List.class));
        verify(playlist).setUser(isA(User.class));
        verify(playlistRepository).findById(1);
        verify(trackRepository).findById(1);
    }

    /**
     * Method under test:
     * {@link PlaylistService#removeTrackFromPlaylist(Integer, Integer)}
     */
    @Test
    void testRemoveTrackFromPlaylist4() {
        // Arrange
        Optional<Playlist> emptyResult = Optional.empty();
        when(playlistRepository.findById(Mockito.<Integer>any())).thenReturn(emptyResult);

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

        // Act and Assert
        assertThrows(ResourceNotFoundException.class, () -> playlistService.removeTrackFromPlaylist(1, 1));
        verify(playlistRepository).findById(1);
    }

    /**
     * Method under test:
     * {@link PlaylistService#removeTrackFromPlaylist(Integer, Integer)}
     */
    @Test
    void testRemoveTrackFromPlaylist5() {
        // Arrange
        User user = new User();
        user.setId(1);
        user.setName("Name");
        user.setPlaylists(new ArrayList<>());
        Playlist playlist = mock(Playlist.class);
        doNothing().when(playlist).setId(Mockito.<Integer>any());
        doNothing().when(playlist).setName(Mockito.<String>any());
        doNothing().when(playlist).setTracks(Mockito.<List<Track>>any());
        doNothing().when(playlist).setUser(Mockito.<User>any());
        playlist.setId(1);
        playlist.setName("Name");
        playlist.setTracks(new ArrayList<>());
        playlist.setUser(user);
        Optional<Playlist> ofResult = Optional.of(playlist);
        when(playlistRepository.findById(Mockito.<Integer>any())).thenReturn(ofResult);
        Optional<Track> emptyResult = Optional.empty();
        when(trackRepository.findById(Mockito.<Integer>any())).thenReturn(emptyResult);

        // Act and Assert
        assertThrows(ResourceNotFoundException.class, () -> playlistService.removeTrackFromPlaylist(1, 1));
        verify(playlist).setId(1);
        verify(playlist).setName("Name");
        verify(playlist).setTracks(isA(List.class));
        verify(playlist).setUser(isA(User.class));
        verify(playlistRepository).findById(1);
        verify(trackRepository).findById(1);
    }

    /**
     * Method under test: {@link PlaylistService#getAllPlaylists()}
     */
    @Test
    void testGetAllPlaylists() {
        // Arrange
        when(playlistRepository.findAll()).thenReturn(new ArrayList<>());

        // Act and Assert
        assertThrows(ResourceNotFoundException.class, () -> playlistService.getAllPlaylists());
        verify(playlistRepository).findAll();
    }

    /**
     * Method under test: {@link PlaylistService#getAllPlaylists()}
     */
    @Test
    void testGetAllPlaylists2() {
        // Arrange
        User user = new User();
        user.setId(1);
        user.setName("No playlists has been created!");
        user.setPlaylists(new ArrayList<>());

        Playlist playlist = new Playlist();
        playlist.setId(1);
        playlist.setName("No playlists has been created!");
        playlist.setTracks(new ArrayList<>());
        playlist.setUser(user);

        ArrayList<Playlist> playlistList = new ArrayList<>();
        playlistList.add(playlist);
        when(playlistRepository.findAll()).thenReturn(playlistList);
        doNothing().when(entityCache).put(Mockito.<Integer>any(), Mockito.<Object>any());

        // Act
        Optional<List<Playlist>> actualAllPlaylists = playlistService.getAllPlaylists();

        // Assert
        verify(entityCache).put(eq(1), isA(Object.class));
        verify(playlistRepository).findAll();
        assertTrue(actualAllPlaylists.isPresent());
    }

    /**
     * Method under test: {@link PlaylistService#getAllPlaylists()}
     */
    @Test
    void testGetAllPlaylists3() {
        // Arrange
        User user = new User();
        user.setId(1);
        user.setName("No playlists has been created!");
        user.setPlaylists(new ArrayList<>());

        Playlist playlist = new Playlist();
        playlist.setId(1);
        playlist.setName("No playlists has been created!");
        playlist.setTracks(new ArrayList<>());
        playlist.setUser(user);

        User user2 = new User();
        user2.setId(2);
        user2.setName("42");
        user2.setPlaylists(new ArrayList<>());

        Playlist playlist2 = new Playlist();
        playlist2.setId(2);
        playlist2.setName("42");
        playlist2.setTracks(new ArrayList<>());
        playlist2.setUser(user2);

        ArrayList<Playlist> playlistList = new ArrayList<>();
        playlistList.add(playlist2);
        playlistList.add(playlist);
        when(playlistRepository.findAll()).thenReturn(playlistList);
        doNothing().when(entityCache).put(Mockito.<Integer>any(), Mockito.<Object>any());

        // Act
        Optional<List<Playlist>> actualAllPlaylists = playlistService.getAllPlaylists();

        // Assert
        verify(entityCache, atLeast(1)).put(Mockito.<Integer>any(), Mockito.<Object>any());
        verify(playlistRepository).findAll();
        assertTrue(actualAllPlaylists.isPresent());
    }

    /**
     * Method under test: {@link PlaylistService#getAllPlaylists()}
     */
    @Test
    void testGetAllPlaylists4() {
        // Arrange
        User user = new User();
        user.setId(1);
        user.setName("No playlists has been created!");
        user.setPlaylists(new ArrayList<>());
        Playlist playlist = mock(Playlist.class);
        when(playlist.getId()).thenReturn(1);
        doNothing().when(playlist).setId(Mockito.<Integer>any());
        doNothing().when(playlist).setName(Mockito.<String>any());
        doNothing().when(playlist).setTracks(Mockito.<List<Track>>any());
        doNothing().when(playlist).setUser(Mockito.<User>any());
        playlist.setId(1);
        playlist.setName("No playlists has been created!");
        playlist.setTracks(new ArrayList<>());
        playlist.setUser(user);

        ArrayList<Playlist> playlistList = new ArrayList<>();
        playlistList.add(playlist);
        when(playlistRepository.findAll()).thenReturn(playlistList);
        doNothing().when(entityCache).put(Mockito.<Integer>any(), Mockito.<Object>any());

        // Act
        Optional<List<Playlist>> actualAllPlaylists = playlistService.getAllPlaylists();

        // Assert
        verify(entityCache).put(eq(1), isA(Object.class));
        verify(playlist).getId();
        verify(playlist).setId(1);
        verify(playlist).setName("No playlists has been created!");
        verify(playlist).setTracks(isA(List.class));
        verify(playlist).setUser(isA(User.class));
        verify(playlistRepository).findAll();
        assertTrue(actualAllPlaylists.isPresent());
    }

    /**
     * Method under test: {@link PlaylistService#getPlaylistById(Integer)}
     */
    @Test
    void testGetPlaylistById() {
        // Arrange
        User user = new User();
        user.setId(1);
        user.setName("Name");
        user.setPlaylists(new ArrayList<>());

        Playlist playlist = new Playlist();
        playlist.setId(1);
        playlist.setName("Name");
        playlist.setTracks(new ArrayList<>());
        playlist.setUser(user);
        Optional<Playlist> ofResult = Optional.of(playlist);
        when(playlistRepository.findById(Mockito.<Integer>any())).thenReturn(ofResult);
        doNothing().when(entityCache).put(Mockito.<Integer>any(), Mockito.<Object>any());

        // Act
        Optional<Playlist> actualPlaylistById = playlistService.getPlaylistById(1);

        // Assert
        verify(entityCache).put(eq(-1693582036), isA(Object.class));
        verify(playlistRepository).findById(1);
        assertSame(ofResult, actualPlaylistById);
    }

    /**
     * Method under test: {@link PlaylistService#getPlaylistById(Integer)}
     */
    @Test
    void testGetPlaylistById2() {
        // Arrange
        User user = new User();
        user.setId(1);
        user.setName("Name");
        user.setPlaylists(new ArrayList<>());

        Playlist playlist = new Playlist();
        playlist.setId(1);
        playlist.setName("Name");
        playlist.setTracks(new ArrayList<>());
        playlist.setUser(user);
        Optional<Playlist> ofResult = Optional.of(playlist);
        when(playlistRepository.findById(Mockito.<Integer>any())).thenReturn(ofResult);
        doThrow(new ResourceNotFoundException("An error occurred")).when(entityCache)
                .put(Mockito.<Integer>any(), Mockito.<Object>any());

        // Act and Assert
        assertThrows(ResourceNotFoundException.class, () -> playlistService.getPlaylistById(1));
        verify(entityCache).put(eq(-1693582036), isA(Object.class));
        verify(playlistRepository).findById(1);
    }

    /**
     * Method under test: {@link PlaylistService#getPlaylistById(Integer)}
     */
    @Test
    void testGetPlaylistById3() {
        // Arrange
        Optional<Playlist> emptyResult = Optional.empty();
        when(playlistRepository.findById(Mockito.<Integer>any())).thenReturn(emptyResult);

        // Act and Assert
        assertThrows(ResourceNotFoundException.class, () -> playlistService.getPlaylistById(1));
        verify(playlistRepository).findById(1);
    }

    /**
     * Method under test: {@link PlaylistService#postPlaylist(Integer, String)}
     */
    @Test
    void testPostPlaylist() {
        // Arrange
        User user = new User();
        user.setId(1);
        user.setName("Name");
        user.setPlaylists(new ArrayList<>());

        Playlist playlist = new Playlist();
        playlist.setId(1);
        playlist.setName("Name");
        ArrayList<Track> tracks = new ArrayList<>();
        playlist.setTracks(tracks);
        playlist.setUser(user);
        when(playlistRepository.save(Mockito.<Playlist>any())).thenReturn(playlist);
        when(playlistRepository.findAll()).thenReturn(new ArrayList<>());

        User user2 = new User();
        user2.setId(1);
        user2.setName("Name");
        user2.setPlaylists(new ArrayList<>());
        Optional<User> ofResult = Optional.of(user2);
        when(userRepository.findById(Mockito.<Integer>any())).thenReturn(ofResult);
        doNothing().when(entityCache).put(Mockito.<Integer>any(), Mockito.<Object>any());

        // Act
        Playlist actualPostPlaylistResult = playlistService.postPlaylist(1, "Name");

        // Assert
        verify(entityCache).put(eq(-1491249213), isA(Object.class));
        verify(userRepository).findById(1);
        verify(playlistRepository).save(isA(Playlist.class));
        verify(playlistRepository).findAll();
        assertEquals("Name", actualPostPlaylistResult.getName());
        assertEquals(tracks, actualPostPlaylistResult.getTracks());
    }

    /**
     * Method under test: {@link PlaylistService#postPlaylist(Integer, String)}
     */
    @Test
    void testPostPlaylist2() {
        // Arrange
        User user = new User();
        user.setId(1);
        user.setName("Name");
        user.setPlaylists(new ArrayList<>());

        Playlist playlist = new Playlist();
        playlist.setId(1);
        playlist.setName("Name");
        playlist.setTracks(new ArrayList<>());
        playlist.setUser(user);
        when(playlistRepository.save(Mockito.<Playlist>any())).thenReturn(playlist);
        when(playlistRepository.findAll()).thenReturn(new ArrayList<>());

        User user2 = new User();
        user2.setId(1);
        user2.setName("Name");
        user2.setPlaylists(new ArrayList<>());
        Optional<User> ofResult = Optional.of(user2);
        when(userRepository.findById(Mockito.<Integer>any())).thenReturn(ofResult);
        doThrow(new ResourceNotFoundException("An error occurred")).when(entityCache)
                .put(Mockito.<Integer>any(), Mockito.<Object>any());

        // Act and Assert
        assertThrows(ResourceNotFoundException.class, () -> playlistService.postPlaylist(1, "Name"));
        verify(entityCache).put(eq(-1491249213), isA(Object.class));
        verify(userRepository).findById(1);
        verify(playlistRepository).save(isA(Playlist.class));
        verify(playlistRepository).findAll();
    }

    /**
     * Method under test: {@link PlaylistService#postPlaylist(Integer, String)}
     */
    @Test
    void testPostPlaylist3() {
        // Arrange
        User user = new User();
        user.setId(1);
        user.setName("Name");
        user.setPlaylists(new ArrayList<>());

        Playlist playlist = new Playlist();
        playlist.setId(1);
        playlist.setName("Name");
        ArrayList<Track> tracks = new ArrayList<>();
        playlist.setTracks(tracks);
        playlist.setUser(user);

        User user2 = new User();
        user2.setId(1);
        user2.setName("all_playlists");
        user2.setPlaylists(new ArrayList<>());

        Playlist playlist2 = new Playlist();
        playlist2.setId(1);
        playlist2.setName("all_playlists");
        playlist2.setTracks(new ArrayList<>());
        playlist2.setUser(user2);

        ArrayList<Playlist> playlistList = new ArrayList<>();
        playlistList.add(playlist2);
        when(playlistRepository.save(Mockito.<Playlist>any())).thenReturn(playlist);
        when(playlistRepository.findAll()).thenReturn(playlistList);

        User user3 = new User();
        user3.setId(1);
        user3.setName("Name");
        user3.setPlaylists(new ArrayList<>());
        Optional<User> ofResult = Optional.of(user3);
        when(userRepository.findById(Mockito.<Integer>any())).thenReturn(ofResult);
        doNothing().when(entityCache).put(Mockito.<Integer>any(), Mockito.<Object>any());

        // Act
        Playlist actualPostPlaylistResult = playlistService.postPlaylist(1, "Name");

        // Assert
        verify(entityCache, atLeast(1)).put(Mockito.<Integer>any(), Mockito.<Object>any());
        verify(userRepository).findById(1);
        verify(playlistRepository).save(isA(Playlist.class));
        verify(playlistRepository).findAll();
        assertEquals("Name", actualPostPlaylistResult.getName());
        assertEquals(tracks, actualPostPlaylistResult.getTracks());
    }

    /**
     * Method under test: {@link PlaylistService#postPlaylist(Integer, String)}
     */
    @Test
    void testPostPlaylist4() {
        // Arrange
        User user = new User();
        user.setId(1);
        user.setName("Name");
        user.setPlaylists(new ArrayList<>());

        Playlist playlist = new Playlist();
        playlist.setId(1);
        playlist.setName("Name");
        ArrayList<Track> tracks = new ArrayList<>();
        playlist.setTracks(tracks);
        playlist.setUser(user);

        User user2 = new User();
        user2.setId(1);
        user2.setName("all_playlists");
        user2.setPlaylists(new ArrayList<>());

        Playlist playlist2 = new Playlist();
        playlist2.setId(1);
        playlist2.setName("all_playlists");
        playlist2.setTracks(new ArrayList<>());
        playlist2.setUser(user2);

        User user3 = new User();
        user3.setId(2);
        user3.setName("Name");
        user3.setPlaylists(new ArrayList<>());

        Playlist playlist3 = new Playlist();
        playlist3.setId(2);
        playlist3.setName("Name");
        playlist3.setTracks(new ArrayList<>());
        playlist3.setUser(user3);

        ArrayList<Playlist> playlistList = new ArrayList<>();
        playlistList.add(playlist3);
        playlistList.add(playlist2);
        when(playlistRepository.save(Mockito.<Playlist>any())).thenReturn(playlist);
        when(playlistRepository.findAll()).thenReturn(playlistList);

        User user4 = new User();
        user4.setId(1);
        user4.setName("Name");
        user4.setPlaylists(new ArrayList<>());
        Optional<User> ofResult = Optional.of(user4);
        when(userRepository.findById(Mockito.<Integer>any())).thenReturn(ofResult);
        doNothing().when(entityCache).put(Mockito.<Integer>any(), Mockito.<Object>any());

        // Act
        Playlist actualPostPlaylistResult = playlistService.postPlaylist(1, "Name");

        // Assert
        verify(entityCache, atLeast(1)).put(Mockito.<Integer>any(), Mockito.<Object>any());
        verify(userRepository).findById(1);
        verify(playlistRepository).save(isA(Playlist.class));
        verify(playlistRepository).findAll();
        assertEquals("Name", actualPostPlaylistResult.getName());
        assertEquals(tracks, actualPostPlaylistResult.getTracks());
    }

    /**
     * Method under test: {@link PlaylistService#postPlaylist(Integer, String)}
     */
    @Test
    void testPostPlaylist5() {
        // Arrange
        Optional<User> emptyResult = Optional.empty();
        when(userRepository.findById(Mockito.<Integer>any())).thenReturn(emptyResult);

        // Act and Assert
        assertThrows(ResourceNotFoundException.class, () -> playlistService.postPlaylist(1, "Name"));
        verify(userRepository).findById(1);
    }

    /**
     * Method under test: {@link PlaylistService#postPlaylist(Integer, String)}
     */
    @Test
    void testPostPlaylist6() {
        // Arrange
        User user = new User();
        user.setId(1);
        user.setName("Name");
        user.setPlaylists(new ArrayList<>());

        Playlist playlist = new Playlist();
        playlist.setId(1);
        playlist.setName("Name");
        playlist.setTracks(new ArrayList<>());
        playlist.setUser(user);

        User user2 = new User();
        user2.setId(3);
        user2.setName("42");
        user2.setPlaylists(new ArrayList<>());

        Playlist playlist2 = new Playlist();
        playlist2.setId(3);
        playlist2.setName("42");
        playlist2.setTracks(new ArrayList<>());
        playlist2.setUser(user2);

        ArrayList<Playlist> playlistList = new ArrayList<>();
        playlistList.add(playlist2);
        when(playlistRepository.save(Mockito.<Playlist>any())).thenReturn(playlist);
        when(playlistRepository.findAll()).thenReturn(playlistList);

        User user3 = new User();
        user3.setId(1);
        user3.setName("Name");
        user3.setPlaylists(new ArrayList<>());
        Optional<User> ofResult = Optional.of(user3);
        when(userRepository.findById(Mockito.<Integer>any())).thenReturn(ofResult);
        doThrow(new ResourceNotFoundException("An error occurred")).when(entityCache)
                .put(Mockito.<Integer>any(), Mockito.<Object>any());

        // Act and Assert
        assertThrows(ResourceNotFoundException.class, () -> playlistService.postPlaylist(1, "Name"));
        verify(entityCache).put(eq(3), isA(Object.class));
        verify(userRepository).findById(1);
        verify(playlistRepository).save(isA(Playlist.class));
        verify(playlistRepository).findAll();
    }
}
