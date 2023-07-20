package com.mss.ged.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mss.ged.entities.BaseContent;
import com.mss.ged.services.BaseContentSevice;

@RestController
@RequestMapping("/api/base")
@CrossOrigin(origins = "*", maxAge = 3600)
public class BaseContentV2 {

	@Autowired
	private BaseContentSevice baseContentService;

	@GetMapping("/")
	public List<? extends BaseContent> getBaseContents() {
		return baseContentService.findByParentIsNull();
	}
}
