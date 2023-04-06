package com.mss.ged.servicees.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.mss.ged.entities.Content;
import com.mss.ged.repositories.ContentRepository;
import com.mss.ged.services.ContentService;

@Service
public class ContentServiceImpl implements ContentService {

	@Autowired
	private ContentRepository contentRepository;

	@Override
	public List<Content> getAllContents() {
		return contentRepository.findAll();
	}

	@Override
	public Content findContentById(Long id) {
		return contentRepository.findById(id).orElseThrow();
	}

	@Override
	public void deleteContent(Long id) {
		contentRepository.deleteById(id);
	}

	@Override
	public Content updatedContent(Long id, Content content) {
		Content contentUpdate = contentRepository.findById(id).get();
		contentUpdate.setName(content.getName());		
		return contentRepository.save(contentUpdate);
	}

	@Override
	public Content findContentByUuid(String uuid) {
		return contentRepository.findByUuid(uuid);
	}

}
