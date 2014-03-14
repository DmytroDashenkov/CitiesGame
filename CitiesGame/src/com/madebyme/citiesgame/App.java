package com.madebyme.citiesgame;

import android.app.Application;

public class App extends Application {
	
	private static DBManager dbManager;
	
	@Override
	public void onCreate() {
		super.onCreate();
		dbManager = new DBManager(this);
	}
	
	public static DBManager getDBManager() {
		return dbManager;
	}
}
