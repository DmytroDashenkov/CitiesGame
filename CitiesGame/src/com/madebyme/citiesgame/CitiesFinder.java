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
	private Context context;
	private String allCities;
	private String[] cities = null;
	private final String FILE_NAME = "cities.txt";

	public CitiesFinder(Context context) {
		this.context = context;
	}

	private String getAllCitiesString() throws IOException {
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

	void prepareDBFeed() {
		try {
			allCities = this.getAllCitiesString();
		} catch (IOException e) {
			Log.e("Error:", e.getMessage());
		}

		cities = allCities.split("  ");
		for (String oneCity : cities) {
			App.getDBManager().inputDBFeed(new City(oneCity, getFirstLetter(oneCity), getLastLetter(oneCity)));
		}
	}

	public String[] getCitiesArrey() {
		return cities;
	}

	String getLastLetter(String word) {
		String letter = null;
		try {
			letter = word.substring(word.length() - 1, word.length());
		} catch (StringIndexOutOfBoundsException e) {
			Log.e("Error word:", word);
		}
		assert letter != null;
		if(letter == "ü" || letter == "û")
			try {
				letter =  word.substring(word.length() - 2, word.length() - 1);
			} catch (StringIndexOutOfBoundsException e) {
				Log.e("Mistake word:", word);
			}
		if(letter == "¸")
			letter = "å";
		return letter;
	}

	String getFirstLetter(String word) {
		String letter = null;
		try {
			letter = word.substring(0, 1);
		} catch (StringIndexOutOfBoundsException e) {
			Log.e("Error word:", word);
		}
		return letter;
	}
}
