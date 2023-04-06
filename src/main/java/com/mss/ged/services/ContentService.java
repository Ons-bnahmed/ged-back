package com.mss.ged.services;

import java.util.List;

import com.mss.ged.entities.Content;


public interface ContentService {

	List<Content> getAllContents();

	Content findContentById(Long id);
	
	Content findContentByUuid(String uuid);
	
	void deleteContent(Long id);

	Content updatedContent(Long id, Content content);
}
