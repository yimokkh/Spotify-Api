package org.example.test.controller;

import org.example.test.entity.User;
import org.example.test.service.RequestCounterService;
import org.example.test.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@CrossOrigin(origins = {"http://localhost:5173"},
        methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PATCH, RequestMethod.DELETE},
        allowedHeaders = {"Authorization", "Content-Type"})
@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    private final RequestCounterService counterService;

    public UserController(UserService userService, RequestCounterService counterService) {
        this.userService = userService;
        this.counterService = counterService;
    }

    @GetMapping("/get-request-count")
    public String getRequestCount() {
        int totalRequestCount = counterService.getRequestCount();
        return "Requests count: " + totalRequestCount;
    }

    @PostMapping()
    public User createUser(@RequestParam String name) {
        counterService.requestIncrement();
        return userService.createUser(new User(name));
    }

    @PostMapping("/bulk")
    public ResponseEntity<Object> createUsers(@RequestBody List<User> users) {
        counterService.requestIncrement();
        return userService.createUsers(users);
    }

    @GetMapping()
    public Optional<List<User>> getAllUsers() {
        counterService.requestIncrement();
        return userService.getAllUsers();
    }

    @GetMapping("/{id}")
    public Optional<User> getUserById(@PathVariable Integer id) {
        counterService.requestIncrement();
        return userService.getUserById(id);
    }

    @DeleteMapping("/{id}")
    public void deleteUserById(@PathVariable Integer id) {
        counterService.requestIncrement();
        userService.deleteUserById(id);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Object> updateUserNameById(@PathVariable Integer id, @RequestParam String newName) {
        counterService.requestIncrement();
        return userService.updateUserNameById(id, newName);
    }

}
