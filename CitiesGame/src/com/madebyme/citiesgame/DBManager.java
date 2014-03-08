package com.madebyme.citiesgame;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

public class DBManager {
	
	ContentValues cv = new ContentValues();
	String[] cities;

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
		DBOpenHelper dbOpenHelper = new DBOpenHelper(context);
		SQLiteDatabase database = dbOpenHelper.getWritableDatabase();
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
