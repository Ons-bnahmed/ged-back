package com.mss.ged.services;

import org.springframework.web.multipart.MultipartFile;

import com.mss.ged.entities.Content;

public interface FileStorageService {
	boolean uploadFile(Content content, MultipartFile file) ;
}
