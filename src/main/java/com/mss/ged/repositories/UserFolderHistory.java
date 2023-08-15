package com.mss.ged.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.mss.ged.entities.BaseContent;
import com.mss.ged.entities.User;
import com.mss.ged.entities.userFolderHistory;

@Repository
public interface UserFolderHistory  extends JpaRepository<userFolderHistory, Long>{
   // List<userFolderHistory> findAll();
	List<userFolderHistory> findByUser(User user);
}
