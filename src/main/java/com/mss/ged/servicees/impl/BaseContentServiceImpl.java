package com.mss.ged.servicees.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.mss.ged.entities.BaseContent;
import com.mss.ged.repositories.BaseContentRepository;
import com.mss.ged.services.BaseContentSevice;

@Service
public class BaseContentServiceImpl implements BaseContentSevice {

	@Autowired
	private BaseContentRepository repository;

	public BaseContent create(BaseContent baseContent) {
		return null;
	}

	@Override
	public BaseContent save(BaseContent baseContent) {
		return repository.saveAndFlush(baseContent);
	}

	@Override
	public boolean removeById(BaseContent baseContent) {
		try {
			repository.delete(baseContent);
		} catch (Exception e) {
			return false;
		}
		return true;
	}

	@Override
	public BaseContent findById(Long id) {
		return repository.findById(id).orElse(null);
	}

	public BaseContent updateById(BaseContent baseContent) {
		return repository.save(baseContent);
	}

}
