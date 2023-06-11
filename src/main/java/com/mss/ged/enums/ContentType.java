package com.mss.ged.enums;

public enum ContentType {
	FOLDER(0),FILE(1);

	private int value;
	
	public int getValue() {
		return value;
	}

	ContentType(int value) {
		this.value = value;
	}
	
}
