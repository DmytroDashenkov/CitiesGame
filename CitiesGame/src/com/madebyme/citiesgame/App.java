package com.madebyme.citiesgame;

import android.app.Application;
import android.graphics.Typeface;
import com.madebyme.citiesgame.highscoresdb.HighScoresDBManager;
import com.madebyme.citiesgame.maindb.DBManager;
import com.madebyme.citiesgame.supportingdb.UsedCitiesManager;

public class App extends Application {
	
	private static DBManager dbManager;
    private static Typeface typeface;
    private static HighScoresDBManager highScoresDBManager;
    private static UsedCitiesManager usedCitiesManager;
	
	@Override
	public void onCreate() {
		super.onCreate();
		dbManager = new DBManager(this);
        typeface = Typeface.createFromAsset(getAssets(), Constants.FONT_FILE_MAME);
        highScoresDBManager = new HighScoresDBManager(this);
        usedCitiesManager = new UsedCitiesManager(this);
	}
	
	public static DBManager getDBManager() {
		return dbManager;
	}

    public static Typeface getTypeface(){
        return typeface;
    }

    public static HighScoresDBManager getHighScoresDBManager() {
        return highScoresDBManager;
    }

    public static UsedCitiesManager getUsedCitiesManager() {
        return usedCitiesManager;
    }
}
