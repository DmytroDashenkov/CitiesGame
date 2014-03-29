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
import android.widget.*;

public class MainActivity extends Activity implements OnClickListener, OnDataLoadedListener {

    private Button bt_ok;
    private EditText et_enterCity;
    private TextView tv_compCity;
    private CitiesFinder citiesFinder;
    private DBManager manager;
    private Cursor cursor;
    private UsedCitiesManager usedCitiesManager;
    private ProgressBar pb_dbLoadingBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initComps();
        manager.initCursor(cursor);
        if (!manager.initCursor(cursor)) {
            MyTask task = new MyTask(this);
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
        startActivity(new Intent(this, HighscoresActivity.class));
        return super.onOptionsItemSelected(item);
    }

    private void initComps() {
        bt_ok = (Button) findViewById(R.id.ok);
        et_enterCity = (EditText) findViewById(R.id.user_city);
        tv_compCity = (TextView) findViewById(R.id.device_city);
        pb_dbLoadingBar = (ProgressBar) findViewById(R.id.db_loading_progress);
        citiesFinder = new CitiesFinder(this);
        bt_ok.setOnClickListener(this);
        manager = new DBManager(this);
        usedCitiesManager = new UsedCitiesManager(this);
    }

    @Override
    public void onClick(View arg) {
        String city = et_enterCity.getText().toString();
        if (city.length() != 0) {
            City town = new City(city, citiesFinder.getFirstLetter(city));
            if (manager.checkCityExistans(town)) {
                if (!usedCitiesManager.checkIfUsed(town)) {
                    usedCitiesManager.inputDBFeed(town);
                    String requestedLetter = citiesFinder.getLastLetter(city).toUpperCase();
                    String answerCity = findAnswerCity(requestedLetter);
                    tv_compCity.setText(answerCity);
                    et_enterCity.setText("");
                } else {
                    Toast.makeText(this, "Было!", Toast.LENGTH_SHORT).show();
                    et_enterCity.setText(null);
                }
            } else {
                Toast.makeText(this, "Такого города нет!", Toast.LENGTH_SHORT)
                        .show();
                et_enterCity.setText(null);
            }
        } else {
            Toast.makeText(this, "Сначала введите город!",
                    Toast.LENGTH_SHORT).show();

        }
    }

    private String findAnswerCity(String firstLetter) {
        City city;
        int i = 0;
        do {
            city = manager.findCityByFirstLetter(firstLetter, i);
            i++;
        } while (usedCitiesManager.checkIfUsed(city));
        usedCitiesManager.inputDBFeed(city);
        Log.i("methods", "findAnswerCity() completed");

        return city.getName();
    }

    @Override
    public void onDataLoaded(boolean isLoaded) {
        if(isLoaded){
            bt_ok.setVisibility(View.VISIBLE);
            et_enterCity.setVisibility(View.VISIBLE);
            tv_compCity.setVisibility(View.VISIBLE);
            pb_dbLoadingBar.setVisibility(View.INVISIBLE);
        }else{
            bt_ok.setVisibility(View.INVISIBLE);
            et_enterCity.setVisibility(View.INVISIBLE);
            tv_compCity.setVisibility(View.INVISIBLE);
            pb_dbLoadingBar.setVisibility(View.VISIBLE);
        }
    }
}
