package com.madebyme.citiesgame.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.madebyme.citiesgame.Constants;

public class DBOpenHelper extends SQLiteOpenHelper {

	public DBOpenHelper(Context context) {
		super(context, Constants.DB_NAME, null, Constants.DB_VERSION);

	}

	@Override
	public void onCreate(SQLiteDatabase database) {
        createMainTable(database);
        createSupportingTable(database);
        createHighScoresDB(database);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

	}

    private void createMainTable(SQLiteDatabase database){
        String query = new StringBuilder().append("CREATE TABLE ").append(Constants.MAIN_TABLE_NAME)
                .append(" ( ").append(Constants.COLUMN_NAME).append(" TEXT, ")
                .append(Constants.COLUMN_FIRST_LETTER).append(" TEXT);").toString();
        database.execSQL(query);
    }

    private void createSupportingTable(SQLiteDatabase database){
        String query = new StringBuilder().append("CREATE TABLE ").append(Constants.SUPPORTING_TABLE_NAME)
                .append(" ( ").append(Constants.COLUMN_ID)
                .append(" INTEGER PRIMARY KEY AUTOINCREMENT, ").append(Constants.COLUMN_NAME)
                .append(" TEXT, ").append(Constants.COLUMN_FIRST_LETTER).append(" TEXT, ")
                .append(Constants.COLUMN_LAST_LETTER).append(" TEXT);").toString();
        database.execSQL(query);
    }

    private void createHighScoresDB(SQLiteDatabase database){
        String query = new StringBuilder().append("CREATE TABLE ").append(Constants.HIGH_SCORES_TABLE_NAME)
                .append(" ( ").append(Constants.COLUMN_DATE).append(" TEXT, ").append(Constants.COLUMN_NAME)
                .append(" TEXT, ").append(Constants.COLUMN_SCORE).append(" INTEGER);").toString();
        database.execSQL(query);

    }

}
