package com.mss.ged.controllers;

import java.util.List;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.mss.ged.entities.BaseContent;
import com.mss.ged.services.BaseContentSevice;

@RestController
@RequestMapping("/api/basecontents")
@CrossOrigin("*")
public class BaseContentController {
	
	private final BaseContentSevice baseContentService;

    public BaseContentController(BaseContentSevice baseContentService) {
        this.baseContentService = baseContentService;
    }

    @GetMapping("/")
    public List<? extends BaseContent> getBaseContents() {
        return baseContentService.findByParentIsNull();
    }	

    @GetMapping("/{parentId}")
    public List<? extends BaseContent> getBaseContentsByParentId(@PathVariable Long parentId) {
        return baseContentService.findByParentId(parentId);
    }	

}
