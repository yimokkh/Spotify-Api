package org.example.test.service;

import org.example.test.entity.User;
import org.example.test.repository.UserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public void createUser(User user) {
        userRepository.save(user);
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public Optional<User> getUserById(Integer id) {
        return userRepository.findById(id);
    }

    public void deleteUserById(Integer id) {
        userRepository.deleteById(id);
    }

    public ResponseEntity<Object> updateUserNameById(Integer id, String newName) {
        Optional<User> optionalUser = userRepository.findById(id);
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            user.setName(newName);
            userRepository.save(user);
        } else {
            return ResponseEntity.notFound().build();
        }
        return null;
    }


}

