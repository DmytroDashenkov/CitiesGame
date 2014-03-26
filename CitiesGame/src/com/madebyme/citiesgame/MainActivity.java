package com.madebyme.citiesgame;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity implements OnClickListener {

	private Button bt_ok;
	private EditText et_enterCity;
	private TextView tv_compCity;
	private CitiesFinder citiesFinder;
	private DBManager manager;
	private Cursor cursor;
	private UsedCitiesManager usedCitiesManager;

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
	protected void onDestroy() {
		super.onDestroy();
		usedCitiesManager.deleteAll();
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
		usedCitiesManager = new UsedCitiesManager(this);
	}

	@Override
	public void onClick(View arg) {
		String city = et_enterCity.getText().toString();
		City town = new City(city, citiesFinder.getFirstLetter(city),
				citiesFinder.getLastLetter(city));
		Log.i("City:", city);
		if (manager.checkCityExistans(town)) {
			if (!usedCitiesManager.checkIfUsed(town)) {
				try{
				String requestedLetter = null;
				String answerCity = null;
				requestedLetter = town.getLastLetter().toUpperCase();
				answerCity = findAnswerCity(requestedLetter);
				usedCitiesManager.inputDBFeed(town);
				tv_compCity.setText(answerCity);
				et_enterCity.setText("");
				}catch(NullPointerException e){
					Toast.makeText(this, "Сначала введите город!", Toast.LENGTH_SHORT).show();
				}
			} else {
				Toast.makeText(this, "Было!", Toast.LENGTH_SHORT).show();
				et_enterCity.setText(null);
			}
		} else {
			Toast.makeText(this, "Такого города нет!", Toast.LENGTH_SHORT)
					.show();
			et_enterCity.setText(null);
		}
	}

	private String findAnswerCity(String firstLetter) {//some fatal exception in this method
		DBManager manager = new DBManager(this);
		UsedCitiesManager usedCitiesManager = new UsedCitiesManager(this);
		City city = manager.findCityByFirstLetter(firstLetter, 1);
		for (int i = 1; true; i++) {
			city = manager.findCityByFirstLetter(firstLetter, i);
			i++;
			if (!usedCitiesManager.checkIfUsed(city))
				break;
		}
		usedCitiesManager.inputDBFeed(city);
		return city.getName();
	}

}
