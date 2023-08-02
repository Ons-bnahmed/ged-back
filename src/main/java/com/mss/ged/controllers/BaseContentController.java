package com.mss.ged.controllers;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.mss.ged.config.Config;
import com.mss.ged.dtos.RenameRequest;
import com.mss.ged.entities.BaseContent;
import com.mss.ged.entities.Content;
import com.mss.ged.entities.Folder;
import com.mss.ged.entities.User;
import com.mss.ged.enums.ContentType;
import com.mss.ged.repositories.UserRepository;
import com.mss.ged.security.JwtUtils;
import com.mss.ged.services.BaseContentSevice;
import com.mss.ged.services.FolderService;

//@RestController
//@RequestMapping("/api/basecontents")
//@CrossOrigin(origins = "*", maxAge = 3600)

//public class BaseContentController {
//	
//	private final BaseContentSevice baseContentService;
//
//    public BaseContentController(BaseContentSevice baseContentService) {
//        this.baseContentService = baseContentService;
//    }
//
//    @GetMapping("/")
//    public List<? extends BaseContent> getBaseContents() {
//        return baseContentService.findByParentIsNull();
//    }	
//
//    @GetMapping("/{parentId}")
//    public List<? extends BaseContent> getBaseContentsByParentId(@PathVariable Long parentId) {
//        return baseContentService.findByParentId(parentId);
//    }	
//    
//    @GetMapping("/search")
//    public List<BaseContent> autocompleteBaseContent(@RequestParam("keyword") String keyword) {
//        return baseContentService.searchByName(keyword);
//    }
//}

@RestController
@RequestMapping("/api/basecontents")
@CrossOrigin(origins = "*", maxAge = 3600)
public class BaseContentController {

	@Autowired
	private BaseContentSevice baseContentService;
	
	@Autowired
	JwtUtils jwtUtils;
	

	@Autowired
	UserRepository userRepository;

	@GetMapping("/")
	public List<? extends BaseContent> getBaseContents() {
		return baseContentService.findByParentIsNull();
	}
	
	@GetMapping("/me")
	public List<? extends BaseContent> getBaseContents(@RequestHeader("Authorization") String token) {
		String jwtToken = token.substring(7);
        String userEmail = jwtUtils.getUserNameFromJwtToken(jwtToken);
        User user = userRepository.findUserByUsername(userEmail);

	    System.out.println("Logged-in user: " + user.getUsername());

	    // Assuming 'baseContentService' has a method to fetch BaseContent by the authenticated user
	    List<? extends BaseContent> baseContents = baseContentService.findByUser(user);
	    List<? extends BaseContent> activeContents = baseContents.stream()
	            .filter(content -> !content.getDeleted()) // Assuming isDeleted is the method that returns the 'deleted' status
	            .collect(Collectors.toList());
	    

	    return activeContents;
	}
	
	
	@GetMapping("/trash/me")
    public  List<? extends BaseContent> getTrashContent(@RequestHeader("Authorization") String token) {
		String jwtToken = token.substring(7);
        String userEmail = jwtUtils.getUserNameFromJwtToken(jwtToken);
        User user = userRepository.findUserByUsername(userEmail);

	    System.out.println("Logged-in user: " + user.getUsername());

	    // Assuming 'baseContentService' has a method to fetch BaseContent by the authenticated user
	    List<? extends BaseContent> baseContents = baseContentService.findByUser(user);
	    List<? extends BaseContent> activeContents = baseContents.stream()
	            .filter(content -> content.getDeleted()) // Assuming isDeleted is the method that returns the 'deleted' status
	            .collect(Collectors.toList());
	    

	    return activeContents;
    }
	
	@GetMapping("/{parentId}")
    public List<? extends BaseContent> getBaseContentsByParentId(@PathVariable Long parentId) {
        return baseContentService.findByParentId(parentId);
    }	
	
	 @GetMapping("/search")
	    public List<BaseContent> autocompleteBaseContent(@RequestParam("keyword") String keyword) {
	        return baseContentService.searchByName(keyword);
	    }
}
