package com.mss.ged.entities;

import java.util.ArrayList;
import java.util.List;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.mss.ged.enums.ContentType;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
public class BaseContent extends BaseEntity<Long> {

	protected static final long serialVersionUID = 1L;

	private String name;
	
	@Column(updatable = false)
	private ContentType type;

	@JsonIgnore
	@OneToMany(mappedBy = "parent", fetch = FetchType.LAZY)
	private List<BaseContent> childs = new ArrayList<>();
	

	public Boolean getDeleted() {
		return deleted;
	}

	public void setDeleted(Boolean deleted) {
		this.deleted = deleted;
	}

	private Boolean deleted = false;
	
	
	@ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	@ManyToOne
	private BaseContent parent;
	
	public BaseContent() {
	}
	
	public BaseContent(String path) {
		System.out.print("this.getClass().isAssignableFrom(Folder.class) " + this.getClass().isAssignableFrom(Folder.class));
		if (this.getClass().isAssignableFrom(Folder.class)) {
			this.type = ContentType.FOLDER;
		} else if(this.getClass().isAssignableFrom(Content.class)) {
			this.type = ContentType.FILE;
		}
	}
	
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

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public ContentType getType() {
		return type;
	}

	public void setType(ContentType type) {
		this.type = type;
	}

	public BaseContent[] listBaseContents() {
		return null;
	}

}
