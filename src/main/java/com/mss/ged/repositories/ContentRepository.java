package com.mss.ged.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.mss.ged.entities.Content;
import com.mss.ged.entities.User;

@Repository
public interface ContentRepository extends JpaRepository <Content, Long>{

	Content findByUuid(String uuid);
	
    List<Content> findByDeletedAndUser(Boolean deleted, User user);
}
