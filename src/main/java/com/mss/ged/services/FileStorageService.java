//FilesStorageService helps us to initialize storage, save new file, load file, get list of Files’ info, delete all files.

package com.mss.ged.services;

import org.springframework.web.multipart.MultipartFile;

import com.mss.ged.entities.Content;

public interface FileStorageService {
	boolean uploadFile(Content content, MultipartFile file) ;
}
