package com.mss.ged.entities;

import java.util.ArrayList;
import java.util.List;
import jakarta.persistence.Entity;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
public class BaseContent extends BaseEntity<Long>{

	protected static final long serialVersionUID = 1L;
	
	@OneToMany (mappedBy = "parent")
	private List<BaseContent> childs = new ArrayList<>();

	@ManyToOne
	private BaseContent parent;


	public List<BaseContent> getChilds() {
		return childs;
	}

	public void setChilds(List<BaseContent> childs) {
		this.childs = childs;
	}

	public BaseContent getParent() {
		return parent;
	}

	public void setParent(BaseContent parent) {
		this.parent = parent;
	}

	

}
