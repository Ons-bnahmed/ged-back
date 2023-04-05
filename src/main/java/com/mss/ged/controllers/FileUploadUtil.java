package com.mss.ged.controllers;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.OffsetDateTime;
import org.springframework.web.multipart.MultipartFile;
import com.mss.ged.entities.Content;

public class FileUploadUtil {
	
	public static void saveFile(Content content, MultipartFile multipartfile) throws IOException {
		
		
		OffsetDateTime currentDate = content.getCreatedAt();
		String year = String.valueOf(currentDate.getYear());
		String month = String.format("%02d", currentDate.getMonthValue());
		String day = String.format("%02d", currentDate.getDayOfMonth());
		String hour = String.format("%02d", currentDate.getHour());
		String minute = String.format("%02d", currentDate.getMinute());
		Path uploadDirectory = Paths.get("Files-upload", year, month, day, hour, minute);
		try (InputStream inputStream = multipartfile.getInputStream()){
			uploadDirectory.toFile().mkdirs();
			Path filePath = uploadDirectory.resolve(content.getUuid()+".bin");
			Files.copy(inputStream, filePath, StandardCopyOption.REPLACE_EXISTING);
		} catch (IOException ioe) {
			throw new IOException("Error saving uploaded file: " + content.getUuid()+".bin", ioe);
		}
	}
}
