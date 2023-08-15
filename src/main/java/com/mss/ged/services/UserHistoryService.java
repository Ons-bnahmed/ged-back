package com.mss.ged.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mss.ged.entities.userHistory;
import com.mss.ged.repositories.UserFolderHistory;
import com.mss.ged.repositories.UserHistoryRepository;
import com.mss.ged.entities.User;
import com.mss.ged.entities.userFolderHistory;

@Service
public class UserHistoryService {

    private final UserHistoryRepository userHistoryRepository;
    
    private final UserFolderHistory userFolderHistory;

    @Autowired
    public UserHistoryService(UserHistoryRepository userHistoryRepository,UserFolderHistory userFolderHistory) {
        this.userHistoryRepository = userHistoryRepository;
		this.userFolderHistory = userFolderHistory;
    }

    public userHistory saveUserHistory(userHistory history) {
        // You can perform any additional logic/validation here before saving
        return userHistoryRepository.save(history);
    }
    
    public List<userHistory> getAllUserHistories() {
        return userHistoryRepository.findAll();
    }
    
    public List<userFolderHistory> getAllUserFolderHistories() {
        return userFolderHistory.findAll();
    }
    
    public List<userFolderHistory> getAllUserFolderHistoriesByUser(User user) {
        return userFolderHistory.findByUser(user);
    }
    
    public List<userHistory> getAllUserHistoriesByUser(User user) {
        return userHistoryRepository.findByUser(user);
    }
}
