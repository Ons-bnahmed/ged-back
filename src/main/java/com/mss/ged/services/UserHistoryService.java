package com.mss.ged.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mss.ged.entities.userHistory;
import com.mss.ged.repositories.UserHistoryRepository;

@Service
public class UserHistoryService {

    private final UserHistoryRepository userHistoryRepository;

    @Autowired
    public UserHistoryService(UserHistoryRepository userHistoryRepository) {
        this.userHistoryRepository = userHistoryRepository;
    }

    public userHistory saveUserHistory(userHistory history) {
        // You can perform any additional logic/validation here before saving
        return userHistoryRepository.save(history);
    }
    
    public List<userHistory> getAllUserHistories() {
        return userHistoryRepository.findAll();
    }
}
