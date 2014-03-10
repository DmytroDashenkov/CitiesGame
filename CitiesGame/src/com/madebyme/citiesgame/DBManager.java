package com.madebyme.citiesgame;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

public class DBManager {
	
	SQLiteDatabase database;
	ContentValues cv = new ContentValues();
	
	public DBManager(Context context) {
		DBOpenHelper dbOpenHelper = new DBOpenHelper(context);
	    database = dbOpenHelper.getWritableDatabase();
	}

	void inputDBFeed(City model) {
			cv.put("name", model.getName());
			cv.put("firstLetter", model.getFirstLetter());
			cv.put("lastLetter", model.getLastLetter());
			database.insert("Cities", null, cv);
			
	}

}
