package org.example.test.controller;


import jakarta.validation.constraints.NotBlank;
import org.example.test.entity.User;
import org.example.test.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping()
    public void createUser(@RequestParam String name) {
        userService.createUser(new User(name));
    }

    @GetMapping()
    public Optional<List<User>> getAllUsers() {
        return userService.getAllUsers();
    }

    @GetMapping("/{id}")
    public Optional<User> getUserById(@PathVariable Integer id) {
        return userService.getUserById(id);
    }

    @DeleteMapping("/{id}")
    public void deleteUserById(@PathVariable Integer id) {
        userService.deleteUserById(id);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Object> updateUserNameById(@PathVariable Integer id, @RequestParam String newName) {
        return userService.updateUserNameById(id, newName);
    }

}
