package com.mss.ged.repositories;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import com.mss.ged.entities.BaseContent;

public interface BaseContentRepository extends JpaRepository <BaseContent, Long> {
	
	List<? extends BaseContent> findByParentId(Long parentId);
	
	List<? extends BaseContent> findByParentIsNull();

}
