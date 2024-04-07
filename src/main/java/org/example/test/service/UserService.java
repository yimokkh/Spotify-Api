package org.example.test.service;

import org.example.test.cache.EntityCache;
import org.example.test.entity.User;
import org.example.test.repository.UserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class UserService {

    private final PlaylistService playlistService;
    private final UserRepository userRepository;
    private final EntityCache<Integer, Object> cacheMap;

    public UserService(UserRepository userRepository, EntityCache<Integer, Object> cacheMap,  PlaylistService playlistService) {
        this.userRepository = userRepository;
        this.cacheMap = cacheMap;
        this.playlistService = playlistService;
    }

    public void createUser(User user) {
        User savedUser = userRepository.save(user);
        Integer userId = savedUser.getId();
        cacheMap.put(userId, savedUser);
    }

    public void deleteUserById(Integer id) {
        Optional<User> optionalUser = userRepository.findById(id);
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            user.getPlaylists().forEach(playlist -> playlistService.deletePlaylistById(playlist.id));
            user.getPlaylists().clear();
            userRepository.deleteById(id);
            updateCacheForAllUsers();
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
            return ResponseEntity.notFound().build();
        }
    }

    public Optional<List<User>> getAllUsers() {
        List<User> userList = userRepository.findAll();
        if (!userList.isEmpty()) {
            userList.forEach(user -> cacheMap.put(user.getId(), user));
            return Optional.of(userList);
        } else {
            return Optional.empty();
        }
    }

    public Optional<User> getUserById(Integer id) {
        int hashCode = Objects.hash("user_by_id", id);
        Object cachedData = cacheMap.get(hashCode);

        if (cachedData != null) {
            return Optional.ofNullable((User) cachedData);
        } else {
            Optional<User> userOptional = userRepository.findById(id);
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
