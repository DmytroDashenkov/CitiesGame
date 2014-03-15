package com.madebyme.citiesgame;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

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

	String findCityByFirstLetter(Cursor cursor, String letter) {
		String name = null;
		Cursor c = database.query("Cities", null, "firstLetter = ?",
				new String[] { letter }, null, null, null);
		if (c.moveToFirst()) {
			cursor = database.rawQuery(
					"SELECT * FROM Cities WHERE firstLetter = ?",
					new String[] { letter });
			try {
				name = cursor.getString(cursor.getColumnIndex("Name"));
			} catch (IndexOutOfBoundsException e) {
				Log.e("Error:", "Index is out of bouns!");
			}
		}

		return name;
	}

	boolean checkCityExistans(String city) {
		Cursor c = database.query("Cities", null, "Name = ?",
				new String[] { city }, null, null, null);
		return c.moveToFirst();
	}

}
