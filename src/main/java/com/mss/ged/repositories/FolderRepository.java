package com.mss.ged.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.mss.ged.entities.Folder;


public interface FolderRepository extends JpaRepository <Folder, Long> {

}
