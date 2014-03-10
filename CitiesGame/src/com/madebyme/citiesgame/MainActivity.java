package com.madebyme.citiesgame;

import android.os.Bundle;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends Activity implements OnClickListener, Runnable {
	
	String userCity;
	private Button ok;
	private EditText enterCity;
	private TextView compCity;
	private DBManager dbManager;
	private Thread dbCreation;
	private CitiesFinder citiesFinder;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		initComps();
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

	private void initComps(){
		new ContentValues();
		ok = (Button) findViewById(R.id.ok);
		enterCity = (EditText) findViewById(R.id.user_city);
		compCity = (TextView) findViewById(R.id.device_city);
		dbCreation = new Thread(this);
		citiesFinder = new CitiesFinder(this);
		ok.setOnClickListener(this);
		dbManager = new DBManager(this);
	}

	@Override
	public void onClick(View arg) {
		userCity = enterCity.getText().toString();
		
	}

	@Override
	public void run() {
		citiesFinder.prepareDBFeed(this);
		
	}
}

