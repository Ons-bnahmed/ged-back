package com.mss.ged.controllers;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.mss.ged.entities.Content;
import com.mss.ged.services.ContentService;

@RestController
@RequestMapping("/api/contents")
public class FileUploadWS {

	@Autowired
	private ContentService contentService;

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

	@PutMapping("/{id}")
	public Content update(@PathVariable Long id, @RequestBody Content content) {
		return contentService.updatedContent(id, content);
	}

}
