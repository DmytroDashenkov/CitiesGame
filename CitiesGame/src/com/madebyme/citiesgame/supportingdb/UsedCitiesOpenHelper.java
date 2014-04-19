package com.madebyme.citiesgame.supportingdb;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.madebyme.citiesgame.Constants;

public class UsedCitiesOpenHelper extends SQLiteOpenHelper {

	public UsedCitiesOpenHelper(Context context) {
		super(context, "Used cities", null, 1);
	}

	@Override
	public void onCreate(SQLiteDatabase database) {
		String query = "CREATE TABLE " + Constants.SUPPORTING_DB_NAME + " ( "
				+ Constants.COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
				+ Constants.COLUMN_NAME + " TEXT, "
				+ Constants.COLUMN_FIRST_LETTER + " TEXT, "
				+ Constants.COLUMN_LAST_LETTER + " TEXT);";
		database.execSQL(query);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

	}

}
