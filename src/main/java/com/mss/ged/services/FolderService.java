package com.mss.ged.services;

import java.util.List;
import com.mss.ged.dtos.RenameRequest;
import com.mss.ged.entities.Folder;

public interface FolderService {

	Folder save(Folder folder);

	List<Folder> getAllFolders();

	Folder findFolderById(Long id);

	void deleteFolder(Long id);

	Folder updatedFolder(Long id, Folder folder);

	Folder findFolderByUuid(String uuid);

	Folder renameFolder(RenameRequest data, Long id);
	 Folder getOrCreateFolder(String folderPath);
	
	 

}
