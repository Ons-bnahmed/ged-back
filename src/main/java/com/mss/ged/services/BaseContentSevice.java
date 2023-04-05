package com.mss.ged.services;

import com.mss.ged.entities.BaseContent;

public interface BaseContentSevice {

	BaseContent create(BaseContent baseContent);

	BaseContent findById(Long id);

	boolean removeById(BaseContent baseContent);

	BaseContent updateById(BaseContent baseContent);

	BaseContent save(BaseContent content);

}
