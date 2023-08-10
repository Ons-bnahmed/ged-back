package com.mss.ged.entities;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;

@Entity
public class userHistory {
	
	  @Id
	  @GeneratedValue(strategy = GenerationType.IDENTITY)
	  private Long id;


	  @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
	    @JoinTable(name = "user_history_contents",
	               joinColumns = @JoinColumn(name = "user_history_id"),
	               inverseJoinColumns = @JoinColumn(name = "content_id"))
	    private List<Content> historyContents;
	

	public List<Content> getHistoryContents() {
		return historyContents;
	}

	public void setHistoryContents(List<Content> historyContents) {
		this.historyContents = historyContents;
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
