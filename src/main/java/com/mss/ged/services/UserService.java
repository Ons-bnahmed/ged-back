package com.mss.ged.services;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.mss.ged.entities.User;
import com.mss.ged.repositories.UserRepository;
import java.util.List;


@Service
public class UserService {

	@Autowired
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User getAuthenticatedUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        Optional<User> optionalUser = userRepository.findByUsername(username);
        return optionalUser.orElse(null);
    }
    
    
    
    public boolean updateUserDetails(String newUsername, String newEmail, String newFullName) {
        User authenticatedUser = getAuthenticatedUser();
        if (authenticatedUser != null) {
            authenticatedUser.setUsername(newUsername);
            authenticatedUser.setEmail(newEmail);
            authenticatedUser.setFullName(newFullName);
            userRepository.save(authenticatedUser);
            return true;
        }
        return false;
    }
    
    
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public Optional<User> getUserById(Long id) {
        return userRepository.findById(id);
    }

    public User getUserByUsername(String username) {
        return userRepository.findUserByUsername(username);
    }

    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }
}
