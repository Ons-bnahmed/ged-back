package com.mss.ged.controllers;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
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
import org.springframework.web.multipart.MultipartFile;
import com.mss.ged.config.Config;
import com.mss.ged.dtos.RenameRequest;
import com.mss.ged.entities.Content;
import com.mss.ged.entities.Folder;
import com.mss.ged.entities.User;
import com.mss.ged.enums.ContentType;
import com.mss.ged.repositories.UserRepository;
import com.mss.ged.security.JwtUtils;
import com.mss.ged.services.BaseContentSevice;
import com.mss.ged.services.ContentService;
import com.mss.ged.services.FileStorageService;
import com.mss.ged.services.FolderService;

import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.TypedQuery;
import jakarta.transaction.Transactional;

@CrossOrigin(origins = "http://localhost:4200", maxAge = 3600, allowCredentials = "true")
@RestController
@RequestMapping("/api/contents")

public class ContentController {
	
	@Autowired
	UserRepository userRepository;
	
	@Autowired
	private ContentService contentService;

	@Autowired
	private FileStorageService fileStorageService;

	@Autowired
	private BaseContentSevice baseContentService;
	
	@Autowired
	private Config fileStorageConfig;
	 
	@Autowired
	private FolderService folderService;
	
	
	@Autowired
	JwtUtils jwtUtils;

	@GetMapping
	public List<Content> getAllContents() {
		return contentService.getAllContents();
	}
 
	@GetMapping("/{id}")
	public Content findContentById(@PathVariable Long id) {
		return contentService.findContentById(id);
    }

	@DeleteMapping("/{id}")
	public void deleteContentById(@PathVariable Long id) {
		contentService.deleteContent(id);
	}
	

	
	
	// @GetMapping("/trash")
	// public ResponseEntity<List<Content>> getTrashContent(@RequestHeader("Authorization") String token) {
	//	String jwtToken = token.substring(7);
	//  String userEmail = jwtUtils.getUserNameFromJwtToken(jwtToken);
	//  User user = userRepository.findUserByUsername(userEmail);  
	//	List<Content> nonDeletedContent = contentService.getDeletedContentForCurrentUser(user);
		//       return ResponseEntity.ok(nonDeletedContent);
	//}
	
	@GetMapping("/deleted-content-for-current-user")
    public List<Content> getDeletedContentForCurrentUser(@RequestHeader("Authorization") String token) {
		String jwtToken = token.substring(7);
        String userEmail = jwtUtils.getUserNameFromJwtToken(jwtToken);
        User user = userRepository.findUserByUsername(userEmail); // Implement this method to get the current user from your authentication mechanism
        return contentService.getDeletedContentForCurrentUser(user);
    }
	
	@PatchMapping("/moveToTrash/{id}")
    public void moveToTrash(@PathVariable Long id) {
        Content content = contentService.findContentById(id);
        // if (content == null) {
        //    return new ResponseEntity<>("Content not found", HttpStatus.NOT_FOUND);
        // }

        // Perform safe delete by updating the "deleted" field to false
        content.setDeleted(true);
        baseContentService.save(content);

       // return new ResponseEntity<>("Content deleted successfully", HttpStatus.OK);
    }

	@PutMapping("/{id}")
	public Content update(@PathVariable Long id, @RequestBody Content content) {
		return contentService.updatedContent(id, content);
	}

	@PatchMapping("/rename/{id}")
	public Content renameFile(@PathVariable("id") Long id, @RequestBody RenameRequest body) {
		return contentService.renameContent(body, id);
	}

//	@Transactional
	@PostMapping("/upload")
	public ResponseEntity<?> uploadFile(@RequestParam("file") MultipartFile multipartFile,
			@RequestHeader("Authorization") String token) throws IOException {
		String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
		Content content = new Content(fileName);
		String nameWithoutExtension = fileName.substring(0, fileName.lastIndexOf('.'));
		String jwtToken = token.substring(7);
        String userEmail = jwtUtils.getUserNameFromJwtToken(jwtToken);
        User user = userRepository.findUserByUsername(userEmail);
        System.out.println("user logged in username " + user.getUsername());
        System.out.println("user logged in fullname " + user.getFullName());
        System.out.println("user logged in email " + user.getEmail());
		content.setOriginalName(fileName);
		content.setName(nameWithoutExtension);
		content.setSize(multipartFile.getSize());
		content.setType(ContentType.FILE);
		
		System.out.println("content.getUser() " + user.getUsername());
        
		Content saved = (Content) baseContentService.save(content);
		
		fileStorageService.uploadFile(saved, multipartFile);
		String filePath = fileStorageConfig.getUploadDir() + File.separator + fileName;
		System.out.print("filePath filePath " + filePath);
        try {
        	multipartFile.transferTo(new File(filePath));
        } catch (IOException e) {
            // Handle file transfer error
        }
        content.setFileUrl(filePath);
        content.setUser(user);
        baseContentService.save(content);
		return new ResponseEntity<>(HttpStatus.CREATED);
	}

//	@GetMapping("/download/{uuid}")
//	public ResponseEntity<InputStreamResource> downloadFile(@PathVariable("uuid") String uuid) throws IOException {
//		Content content = contentService.findContentByUuid(uuid);
//		if (content == null) {
//			return ResponseEntity.notFound().build();
//		}
//
//		LocalDateTime currentDate = content.getCreatedAt();
//		String year = String.valueOf(currentDate.getYear());
//		String month = String.format("%02d", currentDate.getMonthValue());
//		String day = String.format("%02d", currentDate.getDayOfMonth());
//		String hour = String.format("%02d", currentDate.getHour());
//		String minute = String.format("%02d", currentDate.getMinute());
//		Path uploadDirectory = Paths.get("Files-upload", year, month, day, hour, minute);
//
//		String fileName = content.getUuid() + ".bin";
//
//		Path filePath = uploadDirectory.resolve(fileName);
//
//		File file = filePath.toFile();
//		InputStreamResource resource = new InputStreamResource(new FileInputStream(file));
//
//		HttpHeaders headers = new HttpHeaders();
//		headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + content.getOriginalName());
//		headers.add(HttpHeaders.CONTENT_TYPE, "application/octet-stream");
//
//		return ResponseEntity.ok().headers(headers).contentLength(file.length())
//				.contentType(MediaType.APPLICATION_OCTET_STREAM).body(resource);
//	}
	
	@GetMapping("/download/{uuid}")
	public ResponseEntity<InputStreamResource> downloadFile(@PathVariable("uuid") String uuid) throws IOException {
	    Content content = contentService.findContentByUuid(uuid);
	    if (content == null) {
	        return ResponseEntity.notFound().build();
	    }

//	    String filePath = "C:/xamp2/htdocs/uploads/" + content.getName();

	    File file = new File(content.getFileUrl());
	    
	    System.out.print("********* content.getFileUrl() ************ " + content.getFileUrl());
	    if (!file.exists()) {
	        return ResponseEntity.notFound().build();
	    }

	    InputStreamResource resource = new InputStreamResource(new FileInputStream(file));

	    HttpHeaders headers = new HttpHeaders();
	    headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + file.getName());
	    headers.add(HttpHeaders.CONTENT_TYPE, "application/pdf");

	    return ResponseEntity.ok().headers(headers).contentLength(file.length())
	            .contentType(MediaType.APPLICATION_OCTET_STREAM).body(resource);
	}

	
	
//	@PostMapping("/upload/file-folder")
//	public ResponseEntity<String> uploadFile(@RequestParam("file") MultipartFile file, @RequestParam("folder") String folderPath) {
//		
//		try {
//	        if (file.isEmpty()) {
//	            return ResponseEntity.badRequest().body("Please select a file to upload.");
//	        }
//	        
//	        Folder folderToFind = getFolderByName(folderPath);
//	        
//	        System.out.print("********** folder ******* " + folderToFind);
//
//	        // Create the directory if it doesn't exist
//	        File folderDirectory = new File(fileStorageConfig.getUploadDir() + File.separator + folderPath);
//	        if (!folderDirectory.exists()) {
//	            if (!folderDirectory.mkdirs()) {
//	                throw new RuntimeException("Failed to create the folder.");
//	            }
//	        }
//
//	        // Save the file in the folder directory
//	        String fileName = file.getOriginalFilename();
//	        Path filePath = Paths.get(folderDirectory.getAbsolutePath(), fileName);
//	        Files.write(filePath, file.getBytes());
//
//	        // Create a new Content object and set its properties
//	        Content content = new Content(folderPath + File.separator + fileName, fileName, file.getSize());
//	        content.setFileUrl(filePath.toString());
//	        String nameWithoutExtension = fileName.substring(0, fileName.lastIndexOf('.'));
//			content.setOriginalName(fileName);
//			content.setName(nameWithoutExtension);
//			content.setSize(file.getSize());
//			content.setType(ContentType.FILE);
//	        // Retrieve the corresponding folder or create a new one
//	        Folder folder = findOrCreateFolder(folderPath);
//	        // Add the file to the folder
//	        folder.addFile(content);
//	        folder.setName(folderPath);
////	        content.setParent(folderPath);
//		    folderService.save(folder);
//	        // Save the folder (which will cascade to save the files)
//
//	        return ResponseEntity.ok("File uploaded successfully.");
//	    } catch (IOException e) {
//	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to upload the file.");
//	    }
//	     
//	}
	
	@PostMapping("/upload/file-folder")
	public ResponseEntity<String> uploadFileFolder(@RequestParam("file") MultipartFile file, 
			@RequestParam("folder") String folderPath,
			@RequestHeader("Authorization") String token) {

	    try {
	        if (file.isEmpty()) {
	            return ResponseEntity.badRequest().body("Please select a file to upload.");
	        }

	        Folder folder = getFolderByName(folderPath);
	        
	        String jwtToken = token.substring(7);
	        String userEmail = jwtUtils.getUserNameFromJwtToken(jwtToken);
	        User user = userRepository.findUserByUsername(userEmail);

	        System.out.println("user getEmail " + user.getEmail());
	        
	        if (folder == null) {
	            // Folder does not exist, create a new one
	            folder = new Folder();
	            folder.setName(folderPath);
	            // Set other properties of the folder if needed
	            folderService.save(folder);
	        }

	        // Create the directory if it doesn't exist
	        File folderDirectory = new File(fileStorageConfig.getUploadDir() + File.separator + folderPath);
	        System.out.println("folderDirectory folderDirectory " + folderDirectory);
	        System.out.println("!folderDirectory.exists() " + !folderDirectory.exists());
	        System.out.println("!folderDirectory.mkdirs() " + !folderDirectory.mkdirs());
	        if (!folderDirectory.exists()) {
	            if (!folderDirectory.mkdirs()) {
	                throw new RuntimeException("Failed to create the folder.");
	            }
	        }

	        // Save the file in the folder directory
	        String fileName = file.getOriginalFilename();
	        System.out.println("fileName fileName " + fileName);
	        
	        Path filePath = Paths.get(folderDirectory.getAbsolutePath(), fileName);
	        Files.write(filePath, file.getBytes());

	        // Create a new Content object and set its properties
	        Content content = new Content(folderPath + File.separator + fileName, fileName, file.getSize());
	        content.setFileUrl(filePath.toString());
	        String nameWithoutExtension = fileName.substring(0, fileName.lastIndexOf('.'));
	        content.setOriginalName(fileName);
	        content.setName(nameWithoutExtension);
	        content.setSize(file.getSize());
	        content.setType(ContentType.FILE);
	        content.setUser(user);

	        // Add the file to the folder
	        folder.addFile(content);
	        folderService.save(folder);

	        return ResponseEntity.ok("File uploaded successfully.");
	    } catch (IOException e) {
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to upload the file.");
	    }
	}

	
	 @PostMapping("/files/{fileId}/assign-folder/{folderId}")
	    public ResponseEntity<?> assignFileToFolder(@PathVariable Long fileId, @PathVariable Long folderId) {
	        Content file = contentService.findContentById(fileId);
	        Folder folder = folderService.findFolderById(folderId);

	        if (file == null || folder == null) {
	            return ResponseEntity.notFound().build();
	        }

	        folder.addFile(file);
	        folderService.save(folder);
	        file.setFolder(folder);
	        contentService.updatedContent(fileId, file);

	        return ResponseEntity.ok().build();
	    }

	
	public Folder getFolderByName(String folderName) {
	    // Implement the logic to retrieve the folder from the database based on the folder name
	    // You may use your preferred method of interacting with the database or any other data storage mechanism
	    
	    // Assuming you have a collection of folders or a list in memory
	    List<Folder> folders = folderService.getAllFolders();
	    
	    // Iterate over the list and find the folder by name
	    for (Folder folder : folders) {
	        if (folder.getName().equals(folderName)) {
	            return folder;
	        }
	    }
	    
	    return null; // Folder not found
	}

	private Folder findOrCreateFolder(String folderPath) {
        final String uploadDirectory =  fileStorageConfig.getUploadDir();
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

}
