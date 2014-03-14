package com.madebyme.citiesgame;

import android.os.Bundle;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity implements OnClickListener, Runnable {

	private String city;
	private Button bt_ok;
	private EditText et_enterCity;
	private TextView tv_compCity;
	private Thread dbCreation;
	private CitiesFinder citiesFinder;
	private DBManager manager;
	private Cursor cursor;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		initComps();
		if (cursor.moveToFirst())
			dbCreation.start();

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
		new ContentValues();
		bt_ok = (Button) findViewById(R.id.ok);
		et_enterCity = (EditText) findViewById(R.id.user_city);
		tv_compCity = (TextView) findViewById(R.id.device_city);
		dbCreation = new Thread(this);
		citiesFinder = new CitiesFinder(this);
		bt_ok.setOnClickListener(this);
		manager = new DBManager(this);
		cursor = manager.database.query("Cities", null, null, null, null, null,
				null);
	}

	@Override
	public void onClick(View arg) {
		city = et_enterCity.getText().toString();
		if (manager.checkCityExistans(city)) {
			String requestedLetter = citiesFinder.getLastLetter(city);
			requestedLetter = manager.convertSmallLetterToBig(requestedLetter);
			city = manager.findCityByFirstLetter(cursor, requestedLetter);
		}else{
			Toast.makeText(this, "Такого города нет!", Toast.LENGTH_SHORT).show();
		}

	}

	private boolean noMoreCitiesForThosLetterLeft(String cityName) {
		if (cityName == null)
			return true;
		else
			return false;
	}

	@Override
	public void run() {
		citiesFinder.prepareDBFeed(this);

	}
}
