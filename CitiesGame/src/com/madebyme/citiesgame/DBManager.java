package com.madebyme.citiesgame;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.CursorIndexOutOfBoundsException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class DBManager {

	SQLiteDatabase database;

	public DBManager(Context context) {
		DBOpenHelper dbOpenHelper = new DBOpenHelper(context);
		database = dbOpenHelper.getWritableDatabase();
	}
	
	public void inputDBFeed(City model) {
		ContentValues cv = new ContentValues();
		cv.put(Constants.COLUMN_NAME, model.getName());
		cv.put(Constants.COLUMN_FIRST_LETTER, model.getFirstLetter());
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
                "SELECT * FROM " + Constants.MAIN_DB_NAME + " WHERE " + Constants.COLUMN_FIRST_LETTER + " = ?",
                new String[] { letter });
        cursor.moveToPosition(position);
        String cityName = null;
        try{
            cityName = cursor.getString(cursor
                    .getColumnIndex(Constants.COLUMN_NAME));
        }catch(CursorIndexOutOfBoundsException e){
            Log.e("CursorIndexOutOfBoundsException", "DBManager:findCityByFirstLetter");
        }
        return new City(cityName, letter);
    }

    public boolean compereTablesOfUsedAndGeneral(String letter, Context context){
        Cursor used = database.rawQuery(
                "SELECT * FROM " + Constants.MAIN_DB_NAME + " WHERE " + Constants.COLUMN_FIRST_LETTER + " = ?",
                new String[] { letter });
        UsedCitiesManager usedCitiesManager = new UsedCitiesManager(context);
        Cursor general = usedCitiesManager.getDatabase().rawQuery(
                "SELECT * FROM " + Constants.SUPPORTING_DB_NAME + " WHERE " + Constants.COLUMN_FIRST_LETTER + " = ?",
                new String[] { letter });
        if(used.getCount() == general.getCount())
            return true;
        else
            return false;
    }
}
