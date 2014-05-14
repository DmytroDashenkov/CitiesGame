package com.madebyme.citiesgame.models;


import android.os.Parcel;
import android.os.Parcelable;
import com.madebyme.citiesgame.listeners.UserNameCallBack;

public class GameSave implements Parcelable{

    private String date;
    private String name;
    private int score;

    public GameSave(String name, int score, String date) {
        setName(name);
        setScore(score);
        setDate(date);
    }

    public int getScore() {
        return score;
    }

    public String getDate() {
        return date;
    }

    public String getName() {
        return name;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {

    }
}
