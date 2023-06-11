package com.mss.ged.servicees.impl;


import java.io.IOException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import com.mss.ged.tools.FileUploadUtil;
import com.mss.ged.entities.Content;
import com.mss.ged.services.FileStorageService;

@Service
public class FileStorageServiceImpl implements FileStorageService {

	@Override
	public boolean uploadFile(Content content, MultipartFile file) {
		try {
			FileUploadUtil.saveFile(content, file);
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

}
