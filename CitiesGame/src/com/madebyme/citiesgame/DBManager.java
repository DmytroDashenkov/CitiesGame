package com.madebyme.citiesgame;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class DBManager {

	SQLiteDatabase database;

	public DBManager(Context context) {
		DBOpenHelper dbOpenHelper = new DBOpenHelper(context);
		database = dbOpenHelper.getWritableDatabase();
	}

	void inputDBFeed(City model) {
		ContentValues cv = new ContentValues();
		cv.put("name", model.getName());
		cv.put("firstLetter", model.getFirstLetter());
		cv.put("lastLetter", model.getLastLetter());
		database.insert("Cities", null, cv);
	}

	boolean checkCityExistans(String city) {
		Cursor c = database.query("Cities", null, "Name = ?",
				new String[] { city }, null, null, null);
		return c.moveToFirst();
	}

	public boolean initCursor(Cursor cursor) {
		cursor = database.query("Cities", null, null, null, null, null, null);
		if (cursor.moveToFirst())
			return true;
		else
			return false;
	}

	public City findCityByFirstLetter(String letter, Context context) {
		Cursor cursor = database.rawQuery(
				"SELECT * FROM Cities WHERE firstLetter = ?",
				new String[] { letter });
		cursor.moveToFirst();
		CitiesFinder citiesFinder = new CitiesFinder(context);
		cursor.moveToFirst();
		String cityName = cursor.getString(cursor.getColumnIndex("Name"));

		return new City(cityName, letter, citiesFinder.getLastLetter(cityName));
	}

}
