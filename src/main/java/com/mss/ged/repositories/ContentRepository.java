package com.mss.ged.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.mss.ged.entities.Content;

@Repository
public interface ContentRepository extends JpaRepository <Content, Long>{



}
