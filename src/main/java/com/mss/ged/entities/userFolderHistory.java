package com.mss.ged.entities;

import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;

@Entity
public class userFolderHistory {
	  @Id
	  @GeneratedValue(strategy = GenerationType.IDENTITY)
	  private Long id;


	  @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
	  @JoinTable(name = "user__folder_history_contents",
	             joinColumns = @JoinColumn(name = "user_history_id"),
	             inverseJoinColumns = @JoinColumn(name = "folder_id"))
	private Folder historyFolder;
	

	public Folder getHistoryFolder() {
		return historyFolder;
	}

	public void setHistoryFolder(Folder historyFolder) {
		this.historyFolder = historyFolder;
	}

	private String action;

	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}

	@ManyToOne(cascade = {CascadeType.ALL, CascadeType.REMOVE})
  @JoinColumn(name = "user_id")
  private User user;

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}


}