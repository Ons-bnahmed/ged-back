package com.mss.ged.entities;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
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
	
	
	@JsonIgnoreProperties("parent")
	 @OneToMany(mappedBy = "parent", cascade = CascadeType.ALL, orphanRemoval = true)
	 private List<Folder> subfolders = new ArrayList<>();
	
	 @Column(name = "folder_url")
	 private String folderUrl;
	 
	 @Column(name = "folder_path")
	 private String folderPath;

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
    
    public void setFolderPath(String folderPath) {
        this.folderPath = folderPath;
    }
    
    public String getFolderPath() {
        return folderPath;
    }
    
    public String getPath() {
        return this.getName(); // Assuming the name property represents the folder path
    }
    
    public List<Folder> getSubfolders() {
        return subfolders;
    }

    public void setSubfolders(List<Folder> subfolders) {
        this.subfolders = subfolders;
    }

    public void addSubfolder(Folder folder, Folder parentFolder) {
        subfolders.add(folder);
        updateFolderPath(folder, parentFolder);
        folder.setParent(this);
    }

    public void removeSubfolder(Folder folder) {
        subfolders.remove(folder);
        folder.setParent(null);
    }
    
    private void updateFolderPath(Folder folderData, Folder parentFolder) {
    	String folderFullPath ="";
    	if(parentFolder.getFolderPath() != null) {
    		folderFullPath = parentFolder.getFolderPath() +" > "+ folderData.getName();
    	}else {
    		System.out.println("else"
    				+ " ----------> " + parentFolder.getFolderPath());
    		folderFullPath = parentFolder.getPath() +" > "+ folderData.getName();
    	}
    	folderData.setFolderPath(folderFullPath);
//    	
//    	
//        if (folderPath != null) {
//            folderPath = folderPath + ">" + folderName;
//        } else {
//            folderPath = folderName;
//        }
//        System.out.print("folderPath after ******* " +folderPath);
    }

}
