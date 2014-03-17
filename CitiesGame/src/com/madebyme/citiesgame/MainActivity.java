package com.madebyme.citiesgame;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity implements OnClickListener {

	private String city;
	private Button bt_ok;
	private EditText et_enterCity;
	private TextView tv_compCity;
	private CitiesFinder citiesFinder;
	private DBManager manager;
	private Cursor cursor;
	private ArrayList<City> usedCities;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		initComps();
		manager.initCursor(cursor);
		if (!manager.initCursor(cursor)) {
			MyTask task = new MyTask();
			task.execute(this);
		}

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);

		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		Intent intent = new Intent(this, HighscoresActivity.class);
		startActivity(intent);
		return super.onOptionsItemSelected(item);
	}

	private void initComps() {
		bt_ok = (Button) findViewById(R.id.ok);
		et_enterCity = (EditText) findViewById(R.id.user_city);
		tv_compCity = (TextView) findViewById(R.id.device_city);
		citiesFinder = new CitiesFinder(this);
		bt_ok.setOnClickListener(this);
		manager = new DBManager(this);
		usedCities = new ArrayList<City>();
	}

	@Override
	public void onClick(View arg) {
		city = et_enterCity.getText().toString();
		if (manager.checkCityExistans(city)) {
			String requestedLetter = citiesFinder.getLastLetter(city);
			try {
				requestedLetter = requestedLetter.toUpperCase();
				City town = manager.findCityByFirstLetter(requestedLetter, this);
				if (checkIfUsed(town)) {
					Toast.makeText(this, "Было!", Toast.LENGTH_SHORT);
					et_enterCity.setText(null);
				} else {
					city = town.getName();
					tv_compCity.setText(city);
					et_enterCity.setText(null);
					appendCityToList(town);
				}
			} catch (NullPointerException e) {
				Toast.makeText(this, "Сначала введите город!",
						Toast.LENGTH_SHORT).show();
			}
		} else {
			Toast.makeText(this, "Такого города нет!", Toast.LENGTH_SHORT)
					.show();
			et_enterCity.setText(null);
		}

	}

	private ArrayList<City> appendCityToList(City city) {
		usedCities.add(city);
		return usedCities;
	}

	private boolean checkIfUsed(City city) {
		for (int i = 1; i <= usedCities.size(); i++) {
			if (usedCities.contains(city))
				return true;
		}
		return false;
	}

}
