package com.mss.ged.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mss.ged.entities.User;
import com.mss.ged.services.UserService;
import java.util.List;

@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins = "*", maxAge = 3600)
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/me")
    public ResponseEntity<User> getCurrentUser() {
        User authenticatedUser = userService.getAuthenticatedUser();
        if (authenticatedUser != null) {
            return ResponseEntity.ok(authenticatedUser);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

//    @PutMapping("/me")
//    public ResponseEntity<String> updateCurrentUser(@RequestBody User updatedUser) {
//        boolean isUpdated = userService.updateUserDetails(updatedUser.getUsername(), updatedUser.getEmail(), updatedUser.getFullName());
//        if (isUpdated) {
//            return ResponseEntity.ok("User details updated successfully.");
//        } else {
//            return ResponseEntity.badRequest().body("Failed to update user details.");
//        }
//    }
    
    @PutMapping("/update/me")
    public ResponseEntity<String> updateCurrentUser(@RequestBody User updatedUser) {
        boolean isUpdated = userService.updateUserDetails(updatedUser.getUsername(), updatedUser.getEmail(), updatedUser.getFullName());
        System.out.println("isUpdated isUpdated " + isUpdated);
        if (isUpdated) {
            return ResponseEntity.ok("User details updated successfully.");
        } else {
            return ResponseEntity.badRequest().body("Failed to update user details.");
        }
    }
    
    @GetMapping("/getAllUsers")
    public ResponseEntity<List<User>> getAllUsers() {
        List<User> users = userService.getAllUsers();
        return ResponseEntity.ok(users);
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable Long id) {
        return userService.getUserById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }
}

