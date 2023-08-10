package com.mss.ged.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mss.ged.entities.userHistory;
import com.mss.ged.services.UserHistoryService;

@RestController
public class UserHistoryController {
    private final UserHistoryService userHistoryService;
    
    @Autowired
    public UserHistoryController(UserHistoryService userHistoryService) {
        this.userHistoryService = userHistoryService;
    }
    
    @GetMapping("/userHistories")
    public List<userHistory> getAllUserHistories() {
        return userHistoryService.getAllUserHistories();
    }
}
