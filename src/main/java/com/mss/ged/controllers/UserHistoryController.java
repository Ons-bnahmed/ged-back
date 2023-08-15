package com.mss.ged.controllers;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.mss.ged.entities.BaseContent;
import com.mss.ged.entities.User;
import com.mss.ged.entities.userFolderHistory;
import com.mss.ged.entities.userHistory;
import com.mss.ged.repositories.UserRepository;
import com.mss.ged.security.JwtUtils;
import com.mss.ged.services.UserHistoryService;

import java.util.stream.Collectors;
import java.util.stream.Stream;

@RestController
public class UserHistoryController {
    private final UserHistoryService userHistoryService;
    
	@Autowired
	JwtUtils jwtUtils;
	

	@Autowired
	UserRepository userRepository;
    
    @Autowired
    public UserHistoryController(UserHistoryService userHistoryService) {
        this.userHistoryService = userHistoryService;
    }
    
    @GetMapping("/userHistories")
    public List<userHistory> getAllUserHistories() {
        return userHistoryService.getAllUserHistories();
    }
    
    @GetMapping("/allHistories")
    public ResponseEntity<List<Object>> getCombinedData() {
        List<Object> combinedData = new ArrayList<>();
        
        List<userHistory> table1Data = userHistoryService.getAllUserHistories();
        List<userFolderHistory> table2Data = userHistoryService.getAllUserFolderHistories();
        
        for (userHistory userHistoryItem : table1Data) {
        	 combinedData.add(userHistoryItem);
        }
        
        for (userFolderHistory userFolderHistoryItem : table2Data) {
       	 combinedData.add(userFolderHistoryItem);
       }
        
        return ResponseEntity.ok(combinedData);
    }
    
    
    @GetMapping("/allHistories/me")
    public ResponseEntity<List<Object>> getCurrentUserHistory(@RequestHeader("Authorization") String token) {
        List<Object> combinedData = new ArrayList<>();
        String jwtToken = token.substring(7);
        String userEmail = jwtUtils.getUserNameFromJwtToken(jwtToken);
        User user = userRepository.findUserByUsername(userEmail);
        
        List<userHistory> table1Data = userHistoryService.getAllUserHistoriesByUser(user);
        List<userFolderHistory> table2Data = userHistoryService.getAllUserFolderHistoriesByUser(user);
        
        for (userHistory userHistoryItem : table1Data) {
        	 combinedData.add(userHistoryItem);
        }
        
        for (userFolderHistory userFolderHistoryItem : table2Data) {
       	 combinedData.add(userFolderHistoryItem);
       }
        
        return ResponseEntity.ok(combinedData);
    }
    
    
    @GetMapping("/allHistories/{itemId}")
    public ResponseEntity<Object> getItemById(@PathVariable Long itemId) {
        List<userHistory> table1Data = userHistoryService.getAllUserHistories();
        List<userFolderHistory> table2Data = userHistoryService.getAllUserFolderHistories();

        List<Object> foundItems = Stream.concat(table1Data.stream(), table2Data.stream())
                .filter(item -> {
                    if (item instanceof userHistory) {
                        return ((userHistory) item).getUser().getId().equals(itemId);
                    } else if (item instanceof userFolderHistory) {
                        return ((userFolderHistory) item).getUser().getId().equals(itemId);
                    }
                    return false;
                })
                .collect(Collectors.toList());
        

        if (!foundItems.isEmpty()) {
            return ResponseEntity.ok(foundItems);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    
	 @GetMapping("/search/{itemId}/{keyword}")
	    public ResponseEntity<Object> autocompleteBaseContent(@PathVariable String keyword, @PathVariable Long itemId) {
		 List<userHistory> table1Data = userHistoryService.getAllUserHistories();
	        List<userFolderHistory> table2Data = userHistoryService.getAllUserFolderHistories();

	        List<Object> foundItems = Stream.concat(table1Data.stream(), table2Data.stream())
	                .filter(item -> {
	                    if (item instanceof userHistory) {
	                        return ((userHistory) item).getUser().getId().equals(itemId) && ((userHistory) item).getHistoryContents().getName().contains(keyword);
	                    } else if (item instanceof userFolderHistory) {
	                        return ((userFolderHistory) item).getUser().getId().equals(itemId) && ((userFolderHistory) item).getHistoryFolder().getName().contains(keyword);
	                    }
	                    return false;
	                })
	                .collect(Collectors.toList());
	        

	        return ResponseEntity.ok(foundItems);
	    }
}
