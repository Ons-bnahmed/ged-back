package com.mss.ged.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.mss.ged.entities.userHistory;

@Repository
public interface UserHistoryRepository extends JpaRepository<userHistory, Long> {
    // You can add custom query methods here if needed
    List<userHistory> findAll();
}

