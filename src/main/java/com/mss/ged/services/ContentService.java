package com.mss.ged.services;

import java.util.List;

import com.mss.ged.dtos.RenameRequest;
import com.mss.ged.entities.Content;
import com.mss.ged.entities.User;


public interface ContentService {

	List<Content> getAllContents();

	Content findContentById(Long id);
	
	Content findContentByUuid(String uuid);
	
	void deleteContent(Long id);

	Content updatedContent(Long id, Content content, String actionName);

	Content renameContent(RenameRequest data, Long id);
	
	List<Content> getDeletedContentForCurrentUser(User currentUser);
}
