package com.madebyme.citiesgame;

import android.app.Application;
import android.graphics.Typeface;
import com.madebyme.citiesgame.maindb.DBManager;

public class App extends Application {
	
	private static DBManager dbManager;
    private static Typeface typeface;
	
	@Override
	public void onCreate() {
		super.onCreate();
		dbManager = new DBManager(this);
        typeface = Typeface.createFromAsset(getAssets(), Constants.FONT_FILE_MAME);
	}
	
	public static DBManager getDBManager() {
		return dbManager;
	}

    public static Typeface getTypeface(){
        return typeface;
    }
}
