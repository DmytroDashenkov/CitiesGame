package com.madebyme.citiesgame.tasks;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import com.madebyme.citiesgame.listeners.OnDataLoadedListener;
import com.madebyme.citiesgame.db.CitiesFinder;

public class DataLoadingTask extends AsyncTask<Context, Void, Void> {

    private OnDataLoadedListener listener;

    public DataLoadingTask(OnDataLoadedListener listener) {
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
