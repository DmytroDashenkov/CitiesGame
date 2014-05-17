package com.madebyme.citiesgame.db;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.CursorIndexOutOfBoundsException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import com.madebyme.citiesgame.App;
import com.madebyme.citiesgame.models.City;
import com.madebyme.citiesgame.Constants;
import com.madebyme.citiesgame.models.GameSave;

import java.util.ArrayList;
import java.util.Random;

public class DBManager {

	private SQLiteDatabase allCities;
    private SQLiteDatabase usedCities;
    private SQLiteDatabase highScores;

	public DBManager() {
		allCities = App.getDBOpenHelper().getWritableDatabase();
        usedCities = App.getDBOpenHelper().getWritableDatabase();
        highScores = App.getDBOpenHelper().getWritableDatabase();
	}
	
	public void inputDBFeed(City model) {
		ContentValues cv = new ContentValues();
		cv.put(Constants.COLUMN_NAME, model.getName());
		cv.put(Constants.COLUMN_FIRST_LETTER, model.getFirstLetter());
		allCities.insert(Constants.MAIN_TABLE_NAME, null, cv);
	}

	public boolean checkCityExistence(City city) {
		Cursor c = allCities.query(Constants.MAIN_TABLE_NAME, null, "Name = ?",
				new String[] { city.getName() }, null, null, null);
		return c.moveToFirst();
	}

	public boolean initCursor() {
        Cursor cursor = allCities.query(Constants.MAIN_TABLE_NAME, null, null, null, null,
                null, null);
        return cursor.moveToFirst();
    }

    public City findCityByFirstLetter(String letter) {
        Cursor cursor = allCities.rawQuery(
                "SELECT * FROM " + Constants.MAIN_TABLE_NAME + " WHERE " + Constants.COLUMN_FIRST_LETTER + " = ?",
                new String[] { letter });
        Random r = new Random();
        cursor.moveToPosition(r.nextInt(cursor.getCount()));
        String cityName = null;
        try{
            cityName = cursor.getString(cursor
                    .getColumnIndex(Constants.COLUMN_NAME));
        }catch(CursorIndexOutOfBoundsException e){
            Log.e("error" ,e.getMessage());
        }
        return new City(cityName, letter);
    }

    public boolean compereTablesOfUsedAndGeneral(String letter){
        Cursor used = allCities.rawQuery(
                "SELECT * FROM " + Constants.MAIN_TABLE_NAME + " WHERE " + Constants.COLUMN_FIRST_LETTER + " = ?",
                new String[] { letter });
        Cursor general = getUsedCities().rawQuery(
                "SELECT * FROM " + Constants.SUPPORTING_TABLE_NAME + " WHERE " + Constants.COLUMN_FIRST_LETTER + " = ?",
                new String[] { letter });
        if (used.getCount() == general.getCount())
            return true;
        else
            return false;
    }

    public void inputUsedCity(City model){
        ContentValues cv = new ContentValues();
        cv.put(Constants.COLUMN_NAME, model.getName());
        cv.put(Constants.COLUMN_FIRST_LETTER, model.getFirstLetter());
        cv.put(Constants.COLUMN_LAST_LETTER, model.getLastLetter());
        usedCities.insert(Constants.SUPPORTING_TABLE_NAME, null, cv);
    }

    public boolean checkIfUsed(City city){
        Cursor c = usedCities.query(Constants.SUPPORTING_TABLE_NAME, null, "Name = ?",
                new String[] { city.getName() }, null, null, null);
        return c.moveToFirst();
    }

    public void deleteAllUsedCities(){
        usedCities.delete(Constants.SUPPORTING_TABLE_NAME, null, null);
    }

    public SQLiteDatabase getUsedCities(){
        return usedCities;
    }

    public void inputHighScore(GameSave save){
        ContentValues cv = new ContentValues();
        cv.put(Constants.COLUMN_DATE, save.getDate());
        cv.put(Constants.COLUMN_SCORE, save.getScore());
        cv.put(Constants.COLUMN_NAME, save.getName());
        highScores.insert(Constants.HIGH_SCORES_TABLE_NAME, null, cv);
    }

    public ArrayList<GameSave> getHighScores(){
        ArrayList<GameSave> saves = new ArrayList<GameSave>();
        Cursor c = highScores.query(Constants.HIGH_SCORES_TABLE_NAME, null, null, null, null, null, Constants.COLUMN_SCORE + " DESC");
        for(int i = 0; i < c.getCount(); i++){
            c.moveToPosition(i);
            saves.add(new GameSave(c.getString(c.getColumnIndex(Constants.COLUMN_NAME)),
                    c.getInt(c.getColumnIndex(Constants.COLUMN_SCORE)),
                    c.getString(c.getColumnIndex(Constants.COLUMN_DATE))));
        }

        return saves;
    }

    public int getHighScoresRowCount(){
        Cursor c = highScores.query(Constants.HIGH_SCORES_TABLE_NAME, null, null, null, null, null, Constants.COLUMN_SCORE);
        return c.getCount();
    }

    public void beginTransaction(){
        allCities.beginTransaction();
    }

    public void setTransactionSuccessful(){
        allCities.setTransactionSuccessful();
    }

    public void endTransaction(){
        allCities.endTransaction();
    }

}
