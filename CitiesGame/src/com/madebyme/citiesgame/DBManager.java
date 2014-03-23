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

	public DBManager() {

	}

	public void inputDBFeed(City model) {
		ContentValues cv = new ContentValues();
		cv.put(Constants.COLUMN_NAME, model.getName());
		cv.put(Constants.COLUMN_FIRST_LETTER, model.getFirstLetter());
		cv.put(Constants.COLUMN_LAST_LETTER, model.getLastLetter());
		database.insert(Constants.MAIN_DB_NAME, null, cv);
	}

	public boolean checkCityExistans(City city) {
		Cursor c = database.query(Constants.MAIN_DB_NAME, null, "Name = ?",
				new String[] { city.getName() }, null, null, null);
		return c.moveToFirst();
	}

	public boolean initCursor(Cursor cursor) {
		cursor = database.query(Constants.MAIN_DB_NAME, null, null, null, null,
				null, null);
		if (cursor.moveToFirst())
			return true;
		else
			return false;
	}

	public City findCityByFirstLetter(String letter, int position) {
		Cursor cursor = database.rawQuery(
				"SELECT * FROM Cities WHERE firstLetter = ?",
				new String[] { letter });
		cursor.moveToPosition(position);
		CitiesFinder citiesFinder = new CitiesFinder();
		cursor.moveToFirst();
		String cityName = cursor.getString(cursor
				.getColumnIndex(Constants.COLUMN_NAME));

		return new City(cityName, letter, citiesFinder.getLastLetter(cityName));
	}

}
