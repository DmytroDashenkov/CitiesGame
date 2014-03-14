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
		case 'à':
			bigLetter = "À";
			break;

		case 'á':
			bigLetter = "Á";
			break;

		case 'â':
			bigLetter = "Â";
			break;

		case 'ã':
			bigLetter = "Ã";
			break;

		case 'ä':
			bigLetter = "Ä";
			break;

		case 'å':
			bigLetter = "Å";
			break;

		case '¸':
			bigLetter = "Å";
			break;

		case 'æ':
			bigLetter = "Æ";
			break;

		case 'ç':
			bigLetter = "Ç";
			break;

		case 'è':
			bigLetter = "È";
			break;

		case 'ê':
			bigLetter = "Ê";
			break;

		case 'ë':
			bigLetter = "Ë";
			break;

		case 'ì':
			bigLetter = "Ì";
			break;

		case 'í':
			bigLetter = "Í";
			break;

		case 'î':
			bigLetter = "Î";
			break;

		case 'ï':
			bigLetter = "Ï";
			break;

		case 'ð':
			bigLetter = "Ð";
			break;

		case 'ñ':
			bigLetter = "Ñ";
			break;

		case 'ò':
			bigLetter = "Ò";
			break;

		case 'ó':
			bigLetter = "Ó";
			break;

		case 'ô':
			bigLetter = "Ô";
			break;

		case 'õ':
			bigLetter = "Õ";
			break;

		case 'ö':
			bigLetter = "Ö";
			break;

		case 'ø':
			bigLetter = "Ø";
			break;

		case 'ù':
			bigLetter = "Ù";
			break;

		case 'ý':
			bigLetter = "Ý";
			break;

		case 'þ':
			bigLetter = "Þ";
			break;

		case 'ÿ':
			bigLetter = "ß";
			break;

		default:
			Log.e("Error letter:", smallLetter);
			break;
		}
		return bigLetter;
	}

}
