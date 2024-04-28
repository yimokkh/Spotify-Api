package org.example.test.service;

import jakarta.transaction.Transactional;
import org.example.test.aop.annotation.Logging;
import org.example.test.cache.EntityCache;
import org.example.test.entity.User;
import org.example.test.exception.BadRequestErrorException;
import org.example.test.exception.ResourceNotFoundException;
import org.example.test.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Logging
@Service
public class    UserService {

    private final PlaylistService playlistService;
    private final UserRepository userRepository;
    private final EntityCache<Integer, Object> cacheMap;

    private static final String USER_NOT_FOUND_MESSAGE = "User not found!";

    private static final String USER_ALREADY_EXISTS_MESSAGE = "User already exists!";

    private static final String NO_USERS_CREATED_MESSAGE = "No users has been created!";

    public UserService(UserRepository userRepository, EntityCache<Integer, Object> cacheMap,  PlaylistService playlistService) {
        this.userRepository = userRepository;
        this.cacheMap = cacheMap;
        this.playlistService = playlistService;
    }

    @Transactional
    public ResponseEntity<Object> createUsers(List<User> users) {
        if (users == null || users.isEmpty()) {
            throw new ResourceNotFoundException("COUNTRY_NOT_FOUND_MESSAGE");
        }

        List<String> errors = users.stream()
                .map(user -> {
                    try {
                        createUser(user);
                        return null;
                    } catch (Exception e) {
                        return e.getMessage();
                    }
                })
                .filter(Objects::nonNull)
                .toList();

        cacheMap.clear();
        if (!errors.isEmpty()) {
            throw new IllegalArgumentException(
                    "Errors occurred during bulk creation: " + String.join("   ||||   ", errors));
        }
        return new ResponseEntity<>(users, HttpStatus.OK);
    }

    public void createUser(User user) {
        try {
            User savedUser = userRepository.save(user);
            Integer userId = savedUser.getId();
            cacheMap.put(userId, savedUser);
            ResponseEntity.ok(savedUser);
        } catch (Exception e) {
            throw new BadRequestErrorException(USER_ALREADY_EXISTS_MESSAGE);
        }
    }

    public void deleteUserById(Integer id) {
        Optional<User> optionalUser = userRepository.findById(id);
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            user.getPlaylists().forEach(playlist -> playlistService.deletePlaylistById(playlist.id));
            user.getPlaylists().clear();
            userRepository.deleteById(id);
            updateCacheForAllUsers();
        } else {
            throw new ResourceNotFoundException(USER_NOT_FOUND_MESSAGE);
        }
    }

    public ResponseEntity<Object> updateUserNameById(Integer id, String newName) {
        Optional<User> optionalUser = userRepository.findById(id);
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            user.setName(newName);
            userRepository.save(user);
            updateCacheForUserById(id);
            return ResponseEntity.ok().build();
        } else {
            throw new ResourceNotFoundException(USER_NOT_FOUND_MESSAGE);
        }
    }

    public Optional<List<User>> getAllUsers() {
        List<User> userList = userRepository.findAll();
        if (!userList.isEmpty()) {
            userList.forEach(user -> cacheMap.put(user.getId(), user));
            return Optional.of(userList);
        } else {
            throw new ResourceNotFoundException(NO_USERS_CREATED_MESSAGE);
        }
    }

    public Optional<User> getUserById(Integer id) {
        int hashCode = Objects.hash("user_by_id", id);
        Object cachedData = cacheMap.get(hashCode);

        if (cachedData != null) {
            return Optional.of((User) cachedData);
        } else {
            Optional<User> userOptional = Optional.ofNullable(userRepository.findById(id)
                    .orElseThrow(() -> new ResourceNotFoundException(USER_NOT_FOUND_MESSAGE)));
            userOptional.ifPresent(user -> cacheMap.put(hashCode, user));
            return userOptional;
        }
    }

    private void updateCacheForAllUsers() {
        String cacheKey = "all_users";
        Integer hashCode = cacheKey.hashCode();
        List<User> userList = userRepository.findAll();
        cacheMap.put(hashCode, userList);
    }

    private void updateCacheForUserById(Integer id) {
        int hashCode = Objects.hash("user_by_id", id);
        Optional<User> userOptional = userRepository.findById(id);
        userOptional.ifPresent(user -> cacheMap.put(hashCode, user));
    }
}

