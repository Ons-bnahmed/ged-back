package com.mss.ged.servicees.impl;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.mss.ged.config.Config;
import com.mss.ged.dtos.RenameRequest;
import com.mss.ged.entities.Folder;
import com.mss.ged.enums.ContentType;
import com.mss.ged.repositories.FolderRepository;
import com.mss.ged.services.FolderService;

@Service
public class FolderServiceImpl implements FolderService {

	@Autowired
	private FolderRepository folderRepository;
	
	
	 @Autowired
	    private Config fileStorageConfig;

	@Override
	public Folder save(Folder folder) {
		return folderRepository.saveAndFlush(folder);
	}

	@Override
	public List<Folder> getAllFolders() {
		return folderRepository.findAll();
	}

	@Override
	public Folder findFolderById(Long id) {
		return folderRepository.findById(id).orElse(null);
	}

	@Override
	public void deleteFolder(Long id) {
		folderRepository.deleteById(id);
	}

	@Override
	public Folder updatedFolder(Long id, Folder folder) {
		Folder folderUpdate = folderRepository.findById(id).orElse(null);
		folderUpdate.setName(folder.getName());
		folderUpdate.setAction("Edited");
		return folderRepository.save(folderUpdate);
	}

	@Override
	public Folder findFolderByUuid(String uuid) {
		return folderRepository.findByUuid(uuid);
	}

	@Override
	public Folder renameFolder(RenameRequest data, Long id) {
		Optional<Folder> dataFromDb = this.folderRepository.findById(id);
		if (!dataFromDb.isPresent())
			return null;
		Folder folder = dataFromDb.get();
		folder.setName(data.getFileName());
		folder.setAction("Edited");
		return this.folderRepository.save(folder);
	}
	
	
	public Folder getOrCreateFolder(String folderPath) {
	    // Check if the folder already exists in the database
//		 File folder = new File(folderPath);
//	    
//	    if (folder.exists()) {
//	        return folder;
//	    }

	    // Create a new folder if it doesn't exist
	    Folder newFolder = new Folder(folderPath);
	    newFolder.setType(ContentType.FOLDER);
	    newFolder.setFiles(new ArrayList<>());

	    // Set the folder URL based on the folder path
	    String folderUrl = fileStorageConfig.getUploadDir() + File.separator + folderPath;
	    newFolder.setFolderUrl(folderUrl);

	    // Save the new folder in the database
	    return folderRepository.save(newFolder);
	}
	
	public Folder addSubfolder(Long parentId, Folder subfolder) {
        Folder parentFolder = folderRepository.findById(parentId)
                .orElseThrow(() -> new RuntimeException("Parent folder not found"));

        parentFolder.addSubfolder(subfolder, parentFolder);

        return folderRepository.save(parentFolder);
    }

}
