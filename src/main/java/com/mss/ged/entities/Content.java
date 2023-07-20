package com.mss.ged.entities;

import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity
public class Content extends BaseContent {

//	public Content() {
//		super();
//	}
//	public Content(String path) {
//		super(path);
//	}
//
//	private String originalName;
//
//	private static final long serialVersionUID = 1L;
//
//	private Long size;
//	
//	private String fileUrl;
//
//	public Content(String path, String originalName, Long size) {
//		super(path);
//		this.originalName = originalName;
//		this.size = size;
//	}
//
//	public Long getSize() {
//		return size;
//	}
//
//	public void setSize(Long size) {
//		this.size = size;
//	}
//
//	public String getFileUrl() {
//		return fileUrl;
//	}
//	public void setFileUrl(String fileUrl) {
//		this.fileUrl = fileUrl;
//	}
//	public String getOriginalName() {
//		return originalName;
//	}
//
//	public void setOriginalName(String originalName) {
//		this.originalName = originalName;
//	}
//
//	@Override
//	public int hashCode() {
//		return Objects.hash(getName(), size);
//	}
//
//	@Override
//	public boolean equals(Object obj) {
//		if (this == obj)
//			return true;
//		if (obj == null)
//			return false;
//		if (getClass() != obj.getClass())
//			return false;
//		Content other = (Content) obj;
//		return Objects.equals(getName(), other.getName()) && Objects.equals(size, other.size);
//	}
//
//	@Override
//	public String toString() {
//		return "Content [name=" + getName() + ", size=" + size + "]";
//	}
	@JsonIgnore
	@ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "folder_id")
    private Folder folder;

    private String originalName;
    private Long size;
    private String fileUrl;
    

    public Content() {
        super();
    }

    public Content(String path) {
        super(path);
    }

    public Content(String path, String originalName, Long size) {
        super(path);
        this.originalName = originalName;
        this.size = size;
    }

    public Folder getFolder() {
        return folder;
    }

    public void setFolder(Folder folder) {
        this.folder = folder;
    }

    public String getOriginalName() {
        return originalName;
    }

    public void setOriginalName(String originalName) {
        this.originalName = originalName;
    }

    public Long getSize() {
        return size;
    }

    public void setSize(Long size) {
        this.size = size;
    }

    public String getFileUrl() {
        return fileUrl;
    }

    public void setFileUrl(String fileUrl) {
        this.fileUrl = fileUrl;
    }

    @Override
    public int hashCode() {
        return Objects.hash(getName(), size);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Content other = (Content) obj;
        return Objects.equals(getName(), other.getName()) && Objects.equals(size, other.size);
    }

    @Override
    public String toString() {
        return "Content [name=" + getName() + ", size=" + size + "]";
    }
}