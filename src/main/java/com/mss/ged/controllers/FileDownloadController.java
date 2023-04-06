package com.mss.ged.controllers;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.mss.ged.entities.Content;
import com.mss.ged.services.ContentService;


@RestController
@RequestMapping("/api/download")
public class FileDownloadController {
	@Autowired
	private ContentService contentService;

	@GetMapping("/{uuid}")
	public ResponseEntity<InputStreamResource> downloadFile(@PathVariable("uuid") String uuid) throws IOException {
		Content content = contentService.findContentByUuid(uuid);
		if (content == null) {
			return ResponseEntity.notFound().build();
		}

		LocalDateTime currentDate = content.getCreatedAt();
		String year = String.valueOf(currentDate.getYear());
		String month = String.format("%02d", currentDate.getMonthValue());
		String day = String.format("%02d", currentDate.getDayOfMonth());
		String hour = String.format("%02d", currentDate.getHour());
		String minute = String.format("%02d", currentDate.getMinute());
		Path uploadDirectory = Paths.get("Files-upload", year, month, day, hour, minute);
		
		String fileName = content.getUuid() + ".bin";
		
		Path filePath = uploadDirectory.resolve(fileName);

		File file = filePath.toFile();
		InputStreamResource resource = new InputStreamResource(new FileInputStream(file));

		HttpHeaders headers = new HttpHeaders();
		headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + content.getName());
		headers.add(HttpHeaders.CONTENT_TYPE, "application/octet-stream");

		return ResponseEntity.ok().headers(headers).contentLength(file.length())
				.contentType(MediaType.APPLICATION_OCTET_STREAM).body(resource);
	}
}
