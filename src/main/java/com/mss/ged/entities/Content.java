package com.mss.ged.entities;

import java.util.Objects;

import jakarta.persistence.Entity;

@Entity
public class Content extends BaseContent {
	 
	private static final long serialVersionUID = 1L;
	
	private String name;
	private Long size;
	 
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Long getSize() {
		return size;
	}
	public void setSize(Long size) {
		this.size = size;
	}
	@Override
	public int hashCode() {
		return Objects.hash(name, size);
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
		return Objects.equals(name, other.name) && Objects.equals(size, other.size);
	}
	@Override
	public String toString() {
		return "Content [name=" + name + ", size=" + size + "]";
	}
	public static Content findByName(String fileName) {
		return null;
	}
	public Object getFile() {
		return null;
	}
	public Object getContentType() {
		return null;
	}
	public byte[] getData() {
		return null;
	}
	 
}