package org.example.test.service;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.atLeast;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Optional;

import org.example.test.cache.EntityCache;
import org.example.test.entity.Playlist;
import org.example.test.entity.User;
import org.example.test.exception.BadRequestErrorException;
import org.example.test.exception.ResourceNotFoundException;
import org.example.test.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.aot.DisabledInAotMode;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ContextConfiguration(classes = {UserService.class})
@ExtendWith(SpringExtension.class)
@DisabledInAotMode
class UserServiceDiffblueTest {
    @MockBean
    private EntityCache<Integer, Object> entityCache;

    @MockBean
    private PlaylistService playlistService;

    @MockBean
    private UserRepository userRepository;

    @Autowired
    private UserService userService;

    /**
     * Method under test: {@link UserService#createUser(User)}
     */
    @Test
    void testCreateUser() {
        // Arrange
        User user = new User();
        user.setId(1);
        user.setName("Name");
        user.setPlaylists(new ArrayList<>());
        when(userRepository.save(Mockito.<User>any())).thenReturn(user);
        doNothing().when(entityCache).put(Mockito.<Integer>any(), Mockito.<Object>any());

        User user2 = new User();
        user2.setId(1);
        user2.setName("Name");
        user2.setPlaylists(new ArrayList<>());

        // Act
        userService.createUser(user2);

        // Assert
        verify(entityCache).put(eq(1), isA(Object.class));
        verify(userRepository).save(isA(User.class));
    }

    /**
     * Method under test: {@link UserService#createUser(User)}
     */
    @Test
    void testCreateUser2() {
        // Arrange
        User user = new User();
        user.setId(1);
        user.setName("Name");
        user.setPlaylists(new ArrayList<>());
        when(userRepository.save(Mockito.<User>any())).thenReturn(user);
        doThrow(new BadRequestErrorException("An error occurred")).when(entityCache)
                .put(Mockito.<Integer>any(), Mockito.<Object>any());

        User user2 = new User();
        user2.setId(1);
        user2.setName("Name");
        user2.setPlaylists(new ArrayList<>());

        // Act and Assert
        assertThrows(BadRequestErrorException.class, () -> userService.createUser(user2));
        verify(entityCache).put(eq(1), isA(Object.class));
        verify(userRepository).save(isA(User.class));
    }

    /**
     * Method under test: {@link UserService#deleteUserById(Integer)}
     */
    @Test
    void testDeleteUserById() {
        // Arrange
        User user = new User();
        user.setId(1);
        user.setName("Name");
        user.setPlaylists(new ArrayList<>());
        Optional<User> ofResult = Optional.of(user);
        when(userRepository.findAll()).thenReturn(new ArrayList<>());
        doNothing().when(userRepository).deleteById(Mockito.<Integer>any());
        when(userRepository.findById(Mockito.<Integer>any())).thenReturn(ofResult);
        doNothing().when(entityCache).put(Mockito.<Integer>any(), Mockito.<Object>any());

        // Act
        userService.deleteUserById(1);

        // Assert
        verify(entityCache).put(eq(-86970902), isA(Object.class));
        verify(userRepository).deleteById(eq(1));
        verify(userRepository).findById(eq(1));
        verify(userRepository).findAll();
    }

    /**
     * Method under test: {@link UserService#deleteUserById(Integer)}
     */
    @Test
    void testDeleteUserById2() {
        // Arrange
        User user = new User();
        user.setId(1);
        user.setName("Name");
        user.setPlaylists(new ArrayList<>());
        Optional<User> ofResult = Optional.of(user);
        when(userRepository.findAll()).thenReturn(new ArrayList<>());
        doNothing().when(userRepository).deleteById(Mockito.<Integer>any());
        when(userRepository.findById(Mockito.<Integer>any())).thenReturn(ofResult);
        doThrow(new BadRequestErrorException("An error occurred")).when(entityCache)
                .put(Mockito.<Integer>any(), Mockito.<Object>any());

        // Act and Assert
        assertThrows(BadRequestErrorException.class, () -> userService.deleteUserById(1));
        verify(entityCache).put(eq(-86970902), isA(Object.class));
        verify(userRepository).deleteById(eq(1));
        verify(userRepository).findById(eq(1));
        verify(userRepository).findAll();
    }

    /**
     * Method under test: {@link UserService#deleteUserById(Integer)}
     */
    @Test
    void testDeleteUserById3() {
        // Arrange
        User user = new User();
        user.setId(1);
        user.setName("all_users");
        user.setPlaylists(new ArrayList<>());

        Playlist playlist = new Playlist();
        playlist.setId(1);
        playlist.setName("all_users");
        playlist.setTracks(new ArrayList<>());
        playlist.setUser(user);

        ArrayList<Playlist> playlists = new ArrayList<>();
        playlists.add(playlist);

        User user2 = new User();
        user2.setId(1);
        user2.setName("Name");
        user2.setPlaylists(playlists);
        Optional<User> ofResult = Optional.of(user2);
        when(userRepository.findAll()).thenReturn(new ArrayList<>());
        doNothing().when(userRepository).deleteById(Mockito.<Integer>any());
        when(userRepository.findById(Mockito.<Integer>any())).thenReturn(ofResult);
        doNothing().when(entityCache).put(Mockito.<Integer>any(), Mockito.<Object>any());
        doNothing().when(playlistService).deletePlaylistById(Mockito.<Integer>any());

        // Act
        userService.deleteUserById(1);

        // Assert
        verify(entityCache).put(eq(-86970902), isA(Object.class));
        verify(playlistService).deletePlaylistById(eq(1));
        verify(userRepository).deleteById(eq(1));
        verify(userRepository).findById(eq(1));
        verify(userRepository).findAll();
    }

    /**
     * Method under test: {@link UserService#deleteUserById(Integer)}
     */
    @Test
    void testDeleteUserById4() {
        // Arrange
        User user = new User();
        user.setId(1);
        user.setName("all_users");
        user.setPlaylists(new ArrayList<>());

        Playlist playlist = new Playlist();
        playlist.setId(1);
        playlist.setName("all_users");
        playlist.setTracks(new ArrayList<>());
        playlist.setUser(user);

        User user2 = new User();
        user2.setId(2);
        user2.setName("Name");
        user2.setPlaylists(new ArrayList<>());

        Playlist playlist2 = new Playlist();
        playlist2.setId(2);
        playlist2.setName("New Name");
        playlist2.setTracks(new ArrayList<>());
        playlist2.setUser(user2);

        ArrayList<Playlist> playlists = new ArrayList<>();
        playlists.add(playlist2);
        playlists.add(playlist);

        User user3 = new User();
        user3.setId(1);
        user3.setName("Name");
        user3.setPlaylists(playlists);
        Optional<User> ofResult = Optional.of(user3);
        when(userRepository.findAll()).thenReturn(new ArrayList<>());
        doNothing().when(userRepository).deleteById(Mockito.<Integer>any());
        when(userRepository.findById(Mockito.<Integer>any())).thenReturn(ofResult);
        doNothing().when(entityCache).put(Mockito.<Integer>any(), Mockito.<Object>any());
        doNothing().when(playlistService).deletePlaylistById(Mockito.<Integer>any());

        // Act
        userService.deleteUserById(1);

        // Assert
        verify(entityCache).put(eq(-86970902), isA(Object.class));
        verify(playlistService, atLeast(1)).deletePlaylistById(Mockito.<Integer>any());
        verify(userRepository).deleteById(eq(1));
        verify(userRepository).findById(eq(1));
        verify(userRepository).findAll();
    }

    /**
     * Method under test: {@link UserService#deleteUserById(Integer)}
     */
    @Test
    void testDeleteUserById5() {
        // Arrange
        Optional<User> emptyResult = Optional.empty();
        when(userRepository.findById(Mockito.<Integer>any())).thenReturn(emptyResult);

        // Act and Assert
        assertThrows(ResourceNotFoundException.class, () -> userService.deleteUserById(1));
        verify(userRepository).findById(eq(1));
    }

    /**
     * Method under test: {@link UserService#deleteUserById(Integer)}
     */
    @Test
    void testDeleteUserById6() {
        // Arrange
        User user = new User();
        user.setId(1);
        user.setName("all_users");
        user.setPlaylists(new ArrayList<>());

        Playlist playlist = new Playlist();
        playlist.setId(1);
        playlist.setName("all_users");
        playlist.setTracks(new ArrayList<>());
        playlist.setUser(user);

        ArrayList<Playlist> playlists = new ArrayList<>();
        playlists.add(playlist);

        User user2 = new User();
        user2.setId(1);
        user2.setName("Name");
        user2.setPlaylists(playlists);
        Optional<User> ofResult = Optional.of(user2);
        when(userRepository.findById(Mockito.<Integer>any())).thenReturn(ofResult);
        doThrow(new BadRequestErrorException("An error occurred")).when(playlistService)
                .deletePlaylistById(Mockito.<Integer>any());

        // Act and Assert
        assertThrows(BadRequestErrorException.class, () -> userService.deleteUserById(1));
        verify(playlistService).deletePlaylistById(eq(1));
        verify(userRepository).findById(eq(1));
    }
}
