package com.mss.ged.services;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestHeader;

import com.mss.ged.entities.BaseContent;
import com.mss.ged.entities.User;
import com.mss.ged.repositories.BaseContentRepository;
import com.mss.ged.repositories.UserRepository;
import com.mss.ged.security.JwtUtils;

import jakarta.persistence.EntityNotFoundException;

import java.util.List;


@Service
public class UserService {
	
	@Autowired
	JwtUtils jwtUtils;

	@Autowired
    private final UserRepository userRepository;
	
	@Autowired
	private BaseContentRepository baseContentService;
	
	@Autowired
    private PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User getAuthenticatedUser(String token) {
    	String jwtToken = token.substring(7);
        String userEmail = jwtUtils.getUserNameFromJwtToken(jwtToken);
        User user = userRepository.findUserByUsername(userEmail);
        System.out.println("user user " + user.getFullName());
    	
        return user;
    }
    
    
    
    public boolean updateUserDetails(String newUsername, String newEmail, String newFullName, String token) {
        User authenticatedUser = getAuthenticatedUser(token);
        if (authenticatedUser != null) {
             authenticatedUser.setPassword(authenticatedUser.getPassword());
        	 authenticatedUser.setEmail(newEmail);
        	 authenticatedUser.setUsername(authenticatedUser.getUsername());
        	 authenticatedUser.setFullName(newFullName);
             authenticatedUser.setPassword(authenticatedUser.getPassword());
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
    	User user = userRepository.findById(id).orElse(null);
        if (user == null) {
            throw new EntityNotFoundException("User not found");
        }
        
        List<? extends BaseContent> baseContents = baseContentService.findByUser(user);
        // Delete the user (and its associated content instances due to cascading)
        for (BaseContent content : baseContents) {
            // Delete each associated content instance
        	baseContentService.delete(content);
        }
        userRepository.delete(user);
        // userRepository.deleteById(id);
    }
    
    public void updateUserPassword(String username, String newPassword) {
        Optional<User> optionalUser = userRepository.findByUsername(username);
        
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            String hashedPassword = passwordEncoder.encode(newPassword);
            user.setPassword(hashedPassword);
            userRepository.save(user);
        } else {
            throw new EntityNotFoundException("User not found");
        }
    }
}
