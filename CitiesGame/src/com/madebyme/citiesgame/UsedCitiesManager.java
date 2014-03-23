package com.madebyme.citiesgame;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class UsedCitiesManager {
	
	SQLiteDatabase database;
	
	public UsedCitiesManager(Context context){
		UsedCitiesOpenHelper usedCitiesOpenHelper = new UsedCitiesOpenHelper(context);
		database = usedCitiesOpenHelper.getWritableDatabase();
	}
	
	public UsedCitiesManager() {
	
	}
	
	public void inputDBFeed(City model){
		ContentValues cv = new ContentValues();
		cv.put(Constants.COLUMN_NAME, model.getName());
		cv.put(Constants.COLUMN_FIRST_LETTER, model.getFirstLetter());
		cv.put(Constants.COLUMN_LAST_LETTER, model.getLastLetter());
		database.insert(Constants.SUPPORTING_DB_NAME, null, cv);
	}
	
	public boolean checkIfUsed(City city){
		Cursor c = database.query(Constants.SUPPORTING_DB_NAME, null, "Name = ?",
				new String[] { city.getName() }, null, null, null);
		return c.moveToFirst();
	}
	
	public void deleteAll(){
		database.delete(Constants.SUPPORTING_DB_NAME, null, null);
	}

}
