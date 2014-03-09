package com.madebyme.citiesgame;

import java.io.IOException;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class DBManager {
	
	public DBManager(Context context) {
		DBOpenHelper dbOpenHelper = new DBOpenHelper(context);
		SQLiteDatabase database = dbOpenHelper.getWritableDatabase();
	}
	
	ContentValues cv = new ContentValues();
	String[] cities;

	private void prepareDBFeed(Context context) {
		String allCities = null;
		String[] letters = null;
		String letter;
		StringBuilder sb = new StringBuilder();
		CitiesFinder citiesFinder = new CitiesFinder(context);
		try {
			allCities = citiesFinder.getAllCitiesString();
		} catch (IOException e) {
			Log.e("Error:", e.getMessage());
		}
		int n = -1;
		for (int i = 1; true; i++) {
			letter = allCities.substring(i, i + 1);
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

	public void inputDBFeed(Context context) {
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
	}

}
