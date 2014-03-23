package com.madebyme.citiesgame;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

public class MyTask extends AsyncTask<Context, Void, Void> {

	@Override
	protected Void doInBackground(Context... c) {
		Log.e("Good", "DB creation started");
		CitiesFinder citiesFinder = new CitiesFinder(c[0]);
		citiesFinder.prepareDBFeed();
		Log.e("Good", "DB ready");		
		return null;
	}

	
}
