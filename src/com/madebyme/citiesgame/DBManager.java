package com.madebyme.citiesgame;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class DBManager extends SQLiteOpenHelper {
	private final String[] dbCategories = {
			"id integer primary key autoincrement", "name text",
			"firstLeter text", "lastLetter text" };
	ContentValues cv = new ContentValues();
	String[] cities;

	public DBManager(Context context, String name, CursorFactory factory,
			int version) {
		super(context, name, factory, version);
	}

	@Override
	public void onCreate(SQLiteDatabase database) {
		database.execSQL("Cities", dbCategories);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersoin) {

	}

	private void prepareDBFeed(Context context) {
		String[] letters = null;
		String letter;
		StringBuilder sb = new StringBuilder();
		CitiesFinder citiesFinder = new CitiesFinder(context);
		int n = -1;
		for (int i = 1; true; i++) {
			letter = citiesFinder.allCities.substring(i, i + 1);
			if (letter == null)
				break;
			else
				letters[i] = letter;
			if (letters[i] != " " && letters[i - 1] != " ") {
				sb.append(letter);

			} else {
				cities[n + 1] = sb.toString();
			}
		}
	}

	public SQLiteDatabase inputDBFeed(Context context) {
		SQLiteDatabase database = this.getWritableDatabase();
		prepareDBFeed(context);
		int rowsAmount = cities.length;
		for (int i = 0; true; i++) {
			if (i > rowsAmount)
				break;
			cv.put("name", cities[i]);
			cv.put("firstLetter", cities[i].substring(1, 2));
			cv.put("lastLetter",
					cities[i].substring(cities[i].length() - 2,
							cities[i].length() - 1));
		}

		return database;
	}

}
