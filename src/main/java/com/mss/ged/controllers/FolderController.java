package com.mss.ged.controllers;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.mss.ged.config.Config;
import com.mss.ged.dtos.RenameRequest;
import com.mss.ged.entities.BaseContent;
import com.mss.ged.entities.Content;
import com.mss.ged.entities.Folder;
import com.mss.ged.entities.User;
import com.mss.ged.entities.userFolderHistory;
import com.mss.ged.entities.userHistory;
import com.mss.ged.enums.ContentType;
import com.mss.ged.repositories.UserFolderHistory;
import com.mss.ged.repositories.UserHistoryRepository;
import com.mss.ged.repositories.UserRepository;
import com.mss.ged.security.JwtUtils;
import com.mss.ged.services.FolderService;

@RestController
@RequestMapping("/api/folders")
@CrossOrigin(origins = "*", maxAge = 3600)

public class FolderController {
	
	@Autowired
	UserFolderHistory UserFolderHistoryRepository;
	
	
	@Autowired
	JwtUtils jwtUtils;
	
	@Autowired
	UserRepository userRepository;

	@Autowired
	private FolderService folderService;
	
	 @Autowired
	    private Config fileStorageConfig;

	@GetMapping
	public List<Folder> getAllFolders() {
		return folderService.getAllFolders();
	}

	@GetMapping("/{id}")
	public Folder findFolderById(@PathVariable Long id) {
		return folderService.findFolderById(id);
	}

	@DeleteMapping("/{id}")
	public void deleteFolderById(@PathVariable Long id) {
		folderService.deleteFolder(id);
	}

	@PutMapping("/{id}")
	public Folder update(@PathVariable Long id, @RequestBody Folder folder) {
		return folderService.updatedFolder(id, folder);
	}

	@PatchMapping("/rename/{id}")
	public Folder renameFolder(@PathVariable("id") Long id, @RequestBody RenameRequest body) {
		return folderService.renameFolder(body, id);
	}
	
	@PostMapping("/create")
	public String createFolder(@RequestBody Folder folder, @RequestHeader("Authorization") String token) {
	    folder.setType(ContentType.FOLDER);
	    final String uploadDirectory =  fileStorageConfig.getUploadDir();
	    String folderUrl = uploadDirectory + File.separator + folder.getPath();
	    
	    String jwtToken = token.substring(7);
        String userEmail = jwtUtils.getUserNameFromJwtToken(jwtToken);
        User user = userRepository.findUserByUsername(userEmail);
        userFolderHistory userFolderHistoryInstance = new userFolderHistory();
	    // Set the folder URL based on the folder path
        // Create the directory if it doesn't exist
        File folderDirectory = new File(uploadDirectory + File.separator + folder.getName());
        if (!folderDirectory.exists()) {
            folderDirectory.mkdirs();
        }

        // Find or create the corresponding folder
        findOrCreateFolder(folder.getName());
        folder.setFolderUrl(folderUrl);
        folder.setUser(user);
        folder.setAction("Created");
	    
        userFolderHistoryInstance.setHistoryFolder(folder);
        userFolderHistoryInstance.setAction("Created");
        userFolderHistoryInstance.setUser(user);
        UserFolderHistoryRepository.save(userFolderHistoryInstance);
        folderService.save(folder);
	    return "Folder created successfully.";
	}
	
	private Folder findOrCreateFolder(String folderPath) {
        final String uploadDirectory = "/C:/xamp2/htdocs/uploads";
		String fullPath = uploadDirectory + File.separator + folderPath;

	    // Create a File object for the full path
	    File folder = new File(fullPath);

	    if (folder.exists()) {
	        // If the folder already exists, return it as a Folder object
	        return new Folder(fullPath);
	    } else {
	        // If the folder does not exist, create it and return as a Folder object
	        if (folder.mkdirs()) {
	            return new Folder(fullPath);
	        } else {
	            // Handle the case when folder creation fails
	            throw new RuntimeException("Failed to create the folder.");
	        }
	    }
	}
	
	 @GetMapping("/folder/{path}")
	    public List<String> getFolderContents(@PathVariable String path) {
	        BaseContent folder = new BaseContent(path);
	        BaseContent[] baseContent = folder.listBaseContents();
	        if (baseContent == null) {
	            throw new IllegalArgumentException("Invalid folder or empty folder");
	        }
	        return Arrays.stream(baseContent)
	                .map(BaseContent::getName)
	                .collect(Collectors.toList());
	    }
	

	 @PostMapping("/{parentId}/subfolders")
	    public ResponseEntity<Folder> addSubfolder(@PathVariable Long parentId, @RequestBody Folder subfolder) {
		 subfolder.setType(ContentType.FOLDER);
		    final String uploadDirectory =  fileStorageConfig.getUploadDir();
		    String folderUrl = uploadDirectory + File.separator + subfolder.getPath();

		    // Set the folder URL based on the folder path
	        // Create the directory if it doesn't exist
	        File folderDirectory = new File(uploadDirectory + File.separator + subfolder.getName());
	        if (!folderDirectory.exists()) {
	            folderDirectory.mkdirs();
	        }

	        // Find or create the corresponding folder
	        findOrCreateFolder(subfolder.getName());
	        subfolder.setFolderUrl(folderUrl);
	        Folder createdSubfolder = folderService.addSubfolder(parentId, subfolder);
	        return ResponseEntity.status(HttpStatus.CREATED).body(createdSubfolder);
	    }
	 
	 @GetMapping("/search/folders")
	    public List<?> autocompleteBaseContent(@RequestParam("keyword") String keyword, @RequestParam("id") Long id) {
		 Folder folderData = folderService.findFolderById(id);
		 List<BaseContent> searchResults = new ArrayList<>();

	        List<Folder> subfolders = folderData.getSubfolders();
	        for (Folder subfolder : subfolders) {
	            if (subfolder.getName().contains(keyword)) {
	                searchResults.add(subfolder);
	            }
	        }

	        // Search within the files
	        List<Content> files = folderData.getFiles();
	        for (Content file : files) {
	            if (file.getName().contains(keyword)) {
	                searchResults.add(file);
	            }
	        }

	        return searchResults;
	    }
	 
	 @PatchMapping("/moveToTrash/{id}")
	    public void moveToTrash(@PathVariable Long id) {
	        Folder folder = folderService.findFolderById(id);
	        // if (content == null) {
	        //    return new ResponseEntity<>("Content not found", HttpStatus.NOT_FOUND);
	        // }

	        // Perform safe delete by updating the "deleted" field to false
	        folder.setDeleted(true);
	        folder.setAction("Deleted");
	        folderService.save(folder);

	       // return new ResponseEntity<>("Content deleted successfully", HttpStatus.OK);
	    }
	 
	 @PatchMapping("/addToFavorite/{id}")
	    public void addToFavorite(@PathVariable Long id) {
		 Folder folder = folderService.findFolderById(id);
	        folder.setIsFavorite(true);
	        folder.setAction("Added to favorites");
	        folderService.save(folder);

	    }
		
		@PatchMapping("/removeFromFavorite/{id}")
	    public void removeFromFavorite(@PathVariable Long id) {
			Folder content = folderService.findFolderById(id);
	        content.setIsFavorite(false);
	        content.setAction("Removed from favorites");
	        folderService.save(content);
	    }
}
