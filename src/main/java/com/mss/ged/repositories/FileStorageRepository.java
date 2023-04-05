package com.mss.ged.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.mss.ged.entities.FileData;


public interface FileStorageRepository extends JpaRepository<FileData, Long>{

	
}
