package com.madebyme.citiesgame.highscoresdb;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.madebyme.citiesgame.Constants;

public class HighScoresDBOpenHelper extends SQLiteOpenHelper{

    public HighScoresDBOpenHelper(Context context) {
        super(context, Constants.HIDHSCORES_DB_NAME, null, Constants.HIGHSCORES_DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase database) {
        String query = "CREATE TABLE " + Constants.HIDHSCORES_DB_NAME + " ( "
                + Constants.COLUMN_DATE + " TEXT, "
                + Constants.COLUMN_NAME + " TEXT, "
                + Constants.COLUMN_SCORE + " INTEGER);";
        database.execSQL(query);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i2) {

    }
}
