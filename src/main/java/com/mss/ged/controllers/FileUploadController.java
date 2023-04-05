package com.mss.ged.controllers;

import java.io.IOException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import com.mss.ged.entities.Content;
import com.mss.ged.services.BaseContentSevice;
import com.mss.ged.services.FileStorageService;
import jakarta.transaction.Transactional;

@RestController
@RequestMapping("/files")
@CrossOrigin("*")
public class FileUploadController {

	@Autowired
	private FileStorageService fileStorageService;

	@Autowired
	private BaseContentSevice contentService;
	
	@Transactional
	@PostMapping("/uploadFile")
	public ResponseEntity<FileUploadResponse> uploadFile(@RequestParam("file") MultipartFile multipartFile)
			throws IOException {
		String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
		Content content = new Content();
		content.setName(fileName);
		content.setSize(multipartFile.getSize());				
		Content saved = (Content) contentService.save(content);
		fileStorageService.uploadFile(saved, multipartFile);
		return new ResponseEntity<>(HttpStatus.CREATED);
	}
}
