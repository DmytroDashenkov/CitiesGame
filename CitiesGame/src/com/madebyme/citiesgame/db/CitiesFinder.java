package com.madebyme.citiesgame.db;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;

import android.content.Context;
import android.content.res.AssetManager;
import android.util.Log;
import com.madebyme.citiesgame.App;
import com.madebyme.citiesgame.models.City;
import com.madebyme.citiesgame.Constants;

public class CitiesFinder {
	private Context context;
	private String allCities;

    public CitiesFinder(Context context) {
		this.context = context;
	}

	private String getAllCitiesString() throws IOException {

		AssetManager am = context.getAssets();
		InputStream is = am.open(Constants.FILE_NAME);
		allCities = convertStreamToString(is);
		is.close();

		return allCities;
	}

	private String convertStreamToString(InputStream is)
			throws UnsupportedEncodingException {

		StringBuilder stringBuilder = new StringBuilder();
		BufferedReader reader = new BufferedReader(new InputStreamReader(is));
		String line;
		try {
			while (true) {
				line = reader.readLine();
				if (line == null) {
					break;
				}
				stringBuilder.append(line);
			}
            allCities = stringBuilder.toString();
		} catch (IOException e) {
			Log.e("error", e.getMessage());
		}
		return allCities;
	}

	public void prepareDBFeed() {
        DBManager dbManager = App.getDBManager();
        try {
            allCities = this.getAllCitiesString();
        } catch (IOException e) {
            Log.e("Error:", e.getMessage());
        }
        String[] cities = allCities.split("  ");
        dbManager.beginTransaction();
        try{
            for (String oneCity : cities) {
                dbManager.inputDBFeed(new City(oneCity, getFirstLetter(oneCity)));
            }
            dbManager.setTransactionSuccessful();
        }finally {
            dbManager.endTransaction();
        }
    }

	public String getLastLetter(String word) {
		try {
			String letter = word.substring(word.length() - 1);
			if (letter.equals("ы") || letter.equals("ь"))
				letter = word.substring(word.length() - 2, word.length() - 1);

			if (letter.equals("ё"))
				letter = "е";
			return letter;
		} catch (StringIndexOutOfBoundsException e) {
			Log.e("Mistake word:", word);
		}
		return "A";
	}

	public String getFirstLetter(String word) {
		String letter = null;
		try {
			letter = word.substring(0, 1);
		} catch (StringIndexOutOfBoundsException e) {
			Log.e("Error word:", word);
		}
		return letter;
	}

    public String getWordFromSecondLetter(String s){
        return s.substring(1, s.length()- 1);
    }
}
