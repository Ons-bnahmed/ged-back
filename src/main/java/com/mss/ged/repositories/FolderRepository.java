package com.mss.ged.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.mss.ged.entities.Folder;

public interface FolderRepository extends JpaRepository <Folder, Long> {

	Folder findByUuid(String uuid);

}
