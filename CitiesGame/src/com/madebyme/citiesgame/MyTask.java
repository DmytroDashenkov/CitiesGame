package com.madebyme.citiesgame;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

public class MyTask extends AsyncTask<Context, Void, Void> {

    private OnDataLoadedListener listener;

    public MyTask(OnDataLoadedListener listener) {
        this.listener = listener;
    }

    @Override
	protected Void doInBackground(Context... c) {
        listener.onDataLoaded(false);
		Log.e("Good", "DB creation started");
		CitiesFinder citiesFinder = new CitiesFinder(c[0]);
		citiesFinder.prepareDBFeed();
		Log.e("Good", "DB ready");		
		return null;
	}

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        listener.onDataLoaded(true);


    }
}
