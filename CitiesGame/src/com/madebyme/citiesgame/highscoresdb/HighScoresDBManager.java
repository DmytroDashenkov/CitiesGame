package com.madebyme.citiesgame.highscoresdb;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import com.madebyme.citiesgame.Constants;
import com.madebyme.citiesgame.GameSave;

import java.util.ArrayList;

public class HighScoresDBManager {

    SQLiteDatabase database;

    public HighScoresDBManager(Context context) {
        HighScoresDBOpenHelper highScoresDBOpenHelper = new HighScoresDBOpenHelper(context);
        database = highScoresDBOpenHelper.getWritableDatabase();
    }

    public void inputDBFeed(GameSave save){
        ContentValues cv = new ContentValues();
        cv.put(Constants.COLUMN_DATE, save.getDate());
        cv.put(Constants.COLUMN_SCORE, save.getScore());
        cv.put(Constants.COLUMN_NAME, save.getName());
        database.insert(Constants.HIDHSCORES_DB_NAME, null, cv);
    }

    public ArrayList<GameSave> getHighScores(){
        ArrayList<GameSave> saves = new ArrayList<GameSave>();
        Cursor c = database.query(Constants.HIDHSCORES_DB_NAME, null, null, null, null, null, Constants.COLUMN_SCORE + " DESC");
        for(int i = 0; i < c.getCount(); i++){
            c.moveToPosition(i);
            saves.add(new GameSave(c.getString(c.getColumnIndex(Constants.COLUMN_NAME)),
                    c.getInt(c.getColumnIndex(Constants.COLUMN_SCORE)),
                    c.getString(c.getColumnIndex(Constants.COLUMN_DATE))));
        }

        return saves;
    }

    public int getRowCount(){
        Cursor c = database.query(Constants.HIDHSCORES_DB_NAME, null, null, null, null, null, Constants.COLUMN_SCORE);
        return c.getCount();
    }

    public void delete(){
        database.delete(Constants.HIDHSCORES_DB_NAME, null, null);
    }

}