package com.madebyme.citiesgame;

import android.os.Parcel;
import android.os.Parcelable;

public class City implements Parcelable {
	
	private String name;
	private String firstLetter;
	private String lastLetter;
	
    public City(String name, String firstLetter) {
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
		
    }

}
