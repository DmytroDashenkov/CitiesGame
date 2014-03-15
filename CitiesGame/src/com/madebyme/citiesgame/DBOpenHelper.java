package com.madebyme.citiesgame;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBOpenHelper extends SQLiteOpenHelper {

	public DBOpenHelper(Context context) {
		super(context, "Cities", null, 1);
	}

	@Override
	public void onCreate(SQLiteDatabase database) {
		String query = "CREATE TABLE Cities ( _id INTEGER PRIMARY KEY AUTOINCREMENT, Name TEXT, firstLetter TEXT, lastLetter TEXT);";
		database.execSQL(query);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersoin) {

	}

}
