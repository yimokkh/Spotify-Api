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
import java.util.List;
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
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.aot.DisabledInAotMode;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ContextConfiguration(classes = {UserService.class})
@ExtendWith(SpringExtension.class)
@DisabledInAotMode
class UserServiceTest {
  @MockBean
  private EntityCache<Integer, Object> entityCache;

  @MockBean
  private PlaylistService playlistService;

  @MockBean
  private UserRepository userRepository;

  @Autowired
  private UserService userService;

  /**
   * Method under test: {@link UserService#createUsers(List)}
   */

  @Test
  void testCreateUsers() {
    ArrayList<User> arrayList = new ArrayList<>();
    assertThrows(ResourceNotFoundException.class,
            () -> userService.createUsers(arrayList));
  }


  /**
   * Method under test: {@link UserService#createUsers(List)}
   */
  @Test
  void testCreateUsers2() {
    // Arrange
    User user = new User();
    user.setId(1);
    user.setName("Name");
    user.setPlaylists(new ArrayList<>());
    when(userRepository.save(Mockito.<User>any())).thenReturn(user);
    doNothing().when(entityCache).clear();
    doNothing().when(entityCache).put(Mockito.<Integer>any(), Mockito.<Object>any());

    User user2 = new User();
    user2.setId(1);
    user2.setName("COUNTRY_NOT_FOUND_MESSAGE");
    user2.setPlaylists(new ArrayList<>());

    ArrayList<User> users = new ArrayList<>();
    users.add(user2);

    // Act
    ResponseEntity<Object> actualCreateUsersResult = userService.createUsers(users);

    // Assert
    verify(entityCache).clear();
    verify(entityCache).put(eq(1), isA(Object.class));
    verify(userRepository).save(isA(User.class));
    assertEquals(200, actualCreateUsersResult.getStatusCodeValue());
    assertTrue(actualCreateUsersResult.hasBody());
    assertTrue(actualCreateUsersResult.getHeaders().isEmpty());
  }

  /**
   * Method under test: {@link UserService#createUsers(List)}
   */
  @Test
  void testCreateUsers3() {
    // Arrange
    User user = new User();
    user.setId(1);
    user.setName("Name");
    user.setPlaylists(new ArrayList<>());
    when(userRepository.save(Mockito.<User>any())).thenReturn(user);
    doThrow(new BadRequestErrorException("An error occurred")).when(entityCache).clear();
    doThrow(new BadRequestErrorException("An error occurred")).when(entityCache)
        .put(Mockito.<Integer>any(), Mockito.<Object>any());

    User user2 = new User();
    user2.setId(1);
    user2.setName("COUNTRY_NOT_FOUND_MESSAGE");
    user2.setPlaylists(new ArrayList<>());

    ArrayList<User> users = new ArrayList<>();
    users.add(user2);

    // Act and Assert
    assertThrows(BadRequestErrorException.class, () -> userService.createUsers(users));
    verify(entityCache).clear();
    verify(entityCache).put(eq(1), isA(Object.class));
    verify(userRepository).save(isA(User.class));
  }

  /**
   * Method under test: {@link UserService#createUsers(List)}
   */
  @Test
  void testCreateUsers4() {
    // Arrange
    User user = new User();
    user.setId(1);
    user.setName("Name");
    user.setPlaylists(new ArrayList<>());
    when(userRepository.save(Mockito.<User>any())).thenReturn(user);
    doNothing().when(entityCache).clear();
    doNothing().when(entityCache).put(Mockito.<Integer>any(), Mockito.<Object>any());

    User user2 = new User();
    user2.setId(1);
    user2.setName("COUNTRY_NOT_FOUND_MESSAGE");
    user2.setPlaylists(new ArrayList<>());

    User user3 = new User();
    user3.setId(2);
    user3.setName("42");
    user3.setPlaylists(new ArrayList<>());

    ArrayList<User> users = new ArrayList<>();
    users.add(user3);
    users.add(user2);

    // Act
    ResponseEntity<Object> actualCreateUsersResult = userService.createUsers(users);

    // Assert
    verify(entityCache).clear();
    verify(entityCache, atLeast(1)).put(eq(1), isA(Object.class));
    verify(userRepository, atLeast(1)).save(Mockito.<User>any());
    assertEquals(200, actualCreateUsersResult.getStatusCodeValue());
    assertTrue(actualCreateUsersResult.hasBody());
    assertTrue(actualCreateUsersResult.getHeaders().isEmpty());
  }

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
    verify(userRepository).deleteById(1);
    verify(userRepository).findById(1);
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
    verify(userRepository).deleteById(1);
    verify(userRepository).findById(1);
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
    verify(playlistService).deletePlaylistById(1);
    verify(userRepository).deleteById(1);
    verify(userRepository).findById(1);
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
    verify(userRepository).deleteById(1);
    verify(userRepository).findById(1);
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
    verify(userRepository).findById(1);
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
    verify(playlistService).deletePlaylistById(1);
    verify(userRepository).findById(1);
  }

  /**
   * Method under test: {@link UserService#updateUserNameById(Integer, String)}
   */
  @Test
  void testUpdateUserNameById() {
    // Arrange
    User user = new User();
    user.setId(1);
    user.setName("Name");
    user.setPlaylists(new ArrayList<>());
    Optional<User> ofResult = Optional.of(user);

    User user2 = new User();
    user2.setId(1);
    user2.setName("Name");
    user2.setPlaylists(new ArrayList<>());
    when(userRepository.save(Mockito.<User>any())).thenReturn(user2);
    when(userRepository.findById(Mockito.<Integer>any())).thenReturn(ofResult);
    doNothing().when(entityCache).put(Mockito.<Integer>any(), Mockito.<Object>any());

    // Act
    ResponseEntity<Object> actualUpdateUserNameByIdResult =
        userService.updateUserNameById(1, "New Name");

    // Assert
    verify(entityCache).put(eq(-632678125), isA(Object.class));
    verify(userRepository, atLeast(1)).findById(1);
    verify(userRepository).save(isA(User.class));
    assertNull(actualUpdateUserNameByIdResult.getBody());
    assertEquals(200, actualUpdateUserNameByIdResult.getStatusCodeValue());
    assertTrue(actualUpdateUserNameByIdResult.getHeaders().isEmpty());
  }

  /**
   * Method under test: {@link UserService#updateUserNameById(Integer, String)}
   */
  @Test
  void testUpdateUserNameById2() {
    // Arrange
    User user = new User();
    user.setId(1);
    user.setName("Name");
    user.setPlaylists(new ArrayList<>());
    Optional<User> ofResult = Optional.of(user);

    User user2 = new User();
    user2.setId(1);
    user2.setName("Name");
    user2.setPlaylists(new ArrayList<>());
    when(userRepository.save(Mockito.<User>any())).thenReturn(user2);
    when(userRepository.findById(Mockito.<Integer>any())).thenReturn(ofResult);
    doThrow(new BadRequestErrorException("An error occurred")).when(entityCache)
        .put(Mockito.<Integer>any(), Mockito.<Object>any());

    // Act and Assert
    assertThrows(BadRequestErrorException.class,
        () -> userService.updateUserNameById(1, "New Name"));
    verify(entityCache).put(eq(-632678125), isA(Object.class));
    verify(userRepository, atLeast(1)).findById(1);
    verify(userRepository).save(isA(User.class));
  }

  /**
   * Method under test: {@link UserService#updateUserNameById(Integer, String)}
   */
  @Test
  void testUpdateUserNameById3() {
    // Arrange
    Optional<User> emptyResult = Optional.empty();
    when(userRepository.findById(Mockito.<Integer>any())).thenReturn(emptyResult);

    // Act and Assert
    assertThrows(ResourceNotFoundException.class,
        () -> userService.updateUserNameById(1, "New Name"));
    verify(userRepository).findById(1);
  }

  /**
   * Method under test: {@link UserService#getAllUsers()}
   */
  @Test
  void testGetAllUsers() {
    // Arrange
    when(userRepository.findAll()).thenReturn(new ArrayList<>());

    // Act and Assert
    assertThrows(ResourceNotFoundException.class, () -> userService.getAllUsers());
    verify(userRepository).findAll();
  }

  /**
   * Method under test: {@link UserService#getAllUsers()}
   */
  @Test
  void testGetAllUsers2() {
    // Arrange
    User user = new User();
    user.setId(1);
    user.setName("No users has been created!");
    user.setPlaylists(new ArrayList<>());

    ArrayList<User> userList = new ArrayList<>();
    userList.add(user);
    when(userRepository.findAll()).thenReturn(userList);
    doNothing().when(entityCache).put(Mockito.<Integer>any(), Mockito.<Object>any());

    // Act
    Optional<List<User>> actualAllUsers = userService.getAllUsers();

    // Assert
    verify(entityCache).put(eq(1), isA(Object.class));
    verify(userRepository).findAll();
    assertTrue(actualAllUsers.isPresent());
  }

  /**
   * Method under test: {@link UserService#getAllUsers()}
   */
  @Test
  void testGetAllUsers3() {
    // Arrange
    User user = new User();
    user.setId(1);
    user.setName("No users has been created!");
    user.setPlaylists(new ArrayList<>());

    User user2 = new User();
    user2.setId(2);
    user2.setName("42");
    user2.setPlaylists(new ArrayList<>());

    ArrayList<User> userList = new ArrayList<>();
    userList.add(user2);
    userList.add(user);
    when(userRepository.findAll()).thenReturn(userList);
    doNothing().when(entityCache).put(Mockito.<Integer>any(), Mockito.<Object>any());

    // Act
    Optional<List<User>> actualAllUsers = userService.getAllUsers();

    // Assert
    verify(entityCache, atLeast(1)).put(Mockito.<Integer>any(), Mockito.<Object>any());
    verify(userRepository).findAll();
    assertTrue(actualAllUsers.isPresent());
  }

  /**
   * Method under test: {@link UserService#getAllUsers()}
   */
  @Test
  void testGetAllUsers4() {
    // Arrange
    User user = new User();
    user.setId(1);
    user.setName("No users has been created!");
    user.setPlaylists(new ArrayList<>());

    ArrayList<User> userList = new ArrayList<>();
    userList.add(user);
    when(userRepository.findAll()).thenReturn(userList);
    doThrow(new BadRequestErrorException("An error occurred")).when(entityCache)
        .put(Mockito.<Integer>any(), Mockito.<Object>any());

    // Act and Assert
    assertThrows(BadRequestErrorException.class, () -> userService.getAllUsers());
    verify(entityCache).put(eq(1), isA(Object.class));
    verify(userRepository).findAll();
  }
}
