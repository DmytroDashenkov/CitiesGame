package com.madebyme.citiesgame;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

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

	String findCityByFirstLetter(Cursor cursor, String letter) {
		String name = null;
		Cursor c = database.query("myTable", null, "firstLetter = ?",
				new String[] { letter }, null, null, null);
		if(c.moveToFirst()){
		cursor = database.rawQuery(
				"SELECT * FROM Cities WHERE firstLetter = ?",
				new String[] { letter });
		int nameColumnIndex = cursor.getColumnIndex("Name");
		name = cursor.getString(nameColumnIndex);
		}
		
		return name;
	}
	
	boolean checkCityExistans(String city){
		Cursor c = database.query("table", null, "Name = ?", new String[]{ city }, null, null, null);
		return c.moveToFirst();
	}

	String convertSmallLetterToBig(String smallLetter) {
		String bigLetter = null;
		char letter = smallLetter.charAt(0);
		switch (letter) {
		case '�':
			bigLetter = "�";
			break;

		case '�':
			bigLetter = "�";
			break;

		case '�':
			bigLetter = "�";
			break;

		case '�':
			bigLetter = "�";
			break;

		case '�':
			bigLetter = "�";
			break;

		case '�':
			bigLetter = "�";
			break;

		case '�':
			bigLetter = "�";
			break;

		case '�':
			bigLetter = "�";
			break;

		case '�':
			bigLetter = "�";
			break;

		case '�':
			bigLetter = "�";
			break;

		case '�':
			bigLetter = "�";
			break;

		case '�':
			bigLetter = "�";
			break;

		case '�':
			bigLetter = "�";
			break;

		case '�':
			bigLetter = "�";
			break;

		case '�':
			bigLetter = "�";
			break;

		case '�':
			bigLetter = "�";
			break;

		case '�':
			bigLetter = "�";
			break;

		case '�':
			bigLetter = "�";
			break;

		case '�':
			bigLetter = "�";
			break;

		case '�':
			bigLetter = "�";
			break;

		case '�':
			bigLetter = "�";
			break;

		case '�':
			bigLetter = "�";
			break;

		case '�':
			bigLetter = "�";
			break;

		case '�':
			bigLetter = "�";
			break;

		case '�':
			bigLetter = "�";
			break;

		case '�':
			bigLetter = "�";
			break;

		case '�':
			bigLetter = "�";
			break;

		case '�':
			bigLetter = "�";
			break;

		default:
			Log.e("Error letter:", smallLetter);
			break;
		}
		return bigLetter;
	}

}
