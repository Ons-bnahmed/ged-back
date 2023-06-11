package com.mss.ged.entities;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;


@Entity
public class Folder extends BaseContent {

 //	public Folder() {
		//super();
	//}
	
	//public Folder(String path) {
	//	super(path);
	//}

	// private static final long serialVersionUID = 1L;
	@JsonIgnoreProperties("folder")
	@OneToMany(mappedBy = "folder", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Content> files;
	
	 @Column(name = "folder_url")
	    private String folderUrl;

    public Folder() {
        super();
        files = new ArrayList<>();
    }

    public Folder(String path) {
        super(path);
        files = new ArrayList<>();
    }

    public List<Content> getFiles() {
        return files;
    }

    public void setFiles(List<Content> files) {
        this.files = files;
    }

    public void addFile(Content content) {
        files.add(content);
        content.setFolder(this);
    }

    public void removeFile(Content content) {
        files.remove(content);
        content.setFolder(null);
    }
    
    public String getFolderUrl() {
        return folderUrl;
    }

    public void setFolderUrl(String folderUrl) {
        this.folderUrl = folderUrl;
    }
    
    public String getPath() {
        return this.getName(); // Assuming the name property represents the folder path
    }

}
