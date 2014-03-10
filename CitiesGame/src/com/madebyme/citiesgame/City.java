package com.madebyme.citiesgame;

import java.io.Serializable;

public class City implements Serializable {
	
	private String name;
	private String firstLetter;
	private String lastLetter;
	
	public City(String name, String firstLetter, String lastLetter) {
		this.setName(name);
		this.setFirstLetter(firstLetter);
		this.setLastLetter(lastLetter);
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public String getName() {
		return name;
	}
	
	public void setFirstLetter(String firstLetter) {
		this.firstLetter = firstLetter;
	}

	public String getFirstLetter() {
		return firstLetter;
	}
	
	public void setLastLetter(String lastLetter) {
		this.lastLetter = lastLetter;
	}
	
	public String getLastLetter() {
		return lastLetter;
	}
	
}
