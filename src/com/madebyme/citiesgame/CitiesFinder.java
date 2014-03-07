package com.madebyme.citiesgame;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;

import android.content.Context;
import android.content.res.AssetManager;
import android.util.Log;

public class CitiesFinder {
	Context context;
	String allCities;
	final String FILE_NAME = "cities.txt";

	public CitiesFinder(Context context) {
		this.context = context;
	}
	
	public String getAllCitiesString() throws IOException {
		AssetManager am = context.getAssets();
		InputStream is = am.open(FILE_NAME);
		allCities = convertStreamToString(is);
		is.close();
		return allCities;
	}

	private String convertStreamToString(InputStream is)
			throws UnsupportedEncodingException {
		StringBuilder stringBuilder = new StringBuilder();
		BufferedReader reader = new BufferedReader(new InputStreamReader(is));
		String line = null;
		try {
			while (true) {
				line = reader.readLine();
				if (line == null) {
					break;
				}
				allCities = stringBuilder.append(line).toString();
			}
		} catch (IOException e) {
			Log.e("error", e.getMessage());
		}
		Log.d("city", allCities);
		return allCities;
	}
}
