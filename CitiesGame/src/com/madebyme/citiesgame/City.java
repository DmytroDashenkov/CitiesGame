package com.madebyme.citiesgame;

import java.io.Serializable;

public class City implements Serializable {
	
	String name;
	String firstLetter;
	String lastLetter;
	
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
