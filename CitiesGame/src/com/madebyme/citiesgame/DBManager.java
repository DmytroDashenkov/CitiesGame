package com.madebyme.citiesgame;

import java.io.IOException;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class DBManager {
	
	SQLiteDatabase database;
	ContentValues cv = new ContentValues();
	String[] cities;
	
	public DBManager(Context context) {
		DBOpenHelper dbOpenHelper = new DBOpenHelper(context);
	    database = dbOpenHelper.getWritableDatabase();
	}

	public void inputDBFeed(Context context) {
		CitiesFinder citiesFinder = new CitiesFinder(context);
		citiesFinder.prepareDBFeed(context);
		int rowsAmount = cities.length;
		for (int i = 0; true; i++) {
			if (i > rowsAmount)
				break;
			cv.put("name", cities[i]);
			cv.put("firstLetter", cities[i].substring(1, 2));
			cv.put("lastLetter",
					cities[i].substring(cities[i].length() - 2,
							cities[i].length() - 1));
			database.insert("Cities", null, cv);
			
		}
	}

}
