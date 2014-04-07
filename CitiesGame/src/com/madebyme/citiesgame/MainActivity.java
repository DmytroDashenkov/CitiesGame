package com.madebyme.citiesgame;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.*;

public class MainActivity extends FragmentActivity implements OnClickListener, OnDataLoadedListener, OnClickDialogButtonListener{

    private Button bt_ok;
    private EditText et_enterCity;
    private TextView tv_compCity;
    private Button bt_newGame;
    private CitiesFinder citiesFinder;
    private DBManager manager;
    private Cursor cursor;
    private UsedCitiesManager usedCitiesManager;
    private ProgressBar pb_dbLoadingBar;
    private String lastCity;
    private SharedPreferences pref;
    private MyDialog dialog;

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
        lastCity = loadLastCityFromPreferences();
        if(!lastCity.equals(""))
            tv_compCity.setText(lastCity);
    }

    @Override
    protected void onStop() {
        super.onStop();
        saveLastCityInPreference(lastCity);
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
        bt_newGame = (Button) findViewById(R.id.bt_new_game);
        citiesFinder = new CitiesFinder(this);
        bt_ok.setOnClickListener(this);
        bt_newGame.setOnClickListener(this);
        manager = new DBManager(this);
        usedCitiesManager = new UsedCitiesManager(this);
        dialog = new MyDialog(this);
    }

    @Override
    public void onClick(View src) {
        switch (src.getId()){
            case R.id.ok:
                onButtonOkClicked();
                break;
            case R.id.bt_new_game:
                onNewGameStarted();
                break;
        }
    }

    private String findAnswerCity(String firstLetter) {
        City city;
        do {
            city = manager.findCityByFirstLetter(firstLetter);
        } while (usedCitiesManager.checkIfUsed(city));
        usedCitiesManager.inputDBFeed(city);

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

    private void saveLastCityInPreference(String city){
        pref = getPreferences(MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString("Last Called City", city);
        editor.commit();
    }

    private String loadLastCityFromPreferences(){
        pref = getPreferences(MODE_PRIVATE);
        String lastCityUsed = pref.getString("Last Called City", "");
        return lastCityUsed;
    }

    private void onNewGameStarted(){
        usedCitiesManager.deleteAll();
        tv_compCity.setText("Ваш ход!");
        lastCity = null;
        et_enterCity.setText("");
    }

    private boolean checkIfGameIsFinished(String lastUsedCity){
        if(!lastUsedCity.equals("")){
            String letter = citiesFinder.getLastLetter(lastUsedCity).toUpperCase();
            return manager.compereTablesOfUsedAndGeneral(letter, this);
        }else{
            return false;
        }
    }

    private void onButtonOkClicked(){
        String city = et_enterCity.getText().toString();
        if (city.length() != 0) {
            City town = new City(city, citiesFinder.getFirstLetter(city));
            if (manager.checkCityExistans(town)) {
                if (!usedCitiesManager.checkIfUsed(town)) {
                    if(lastCity.equals("") || citiesFinder.getFirstLetter(city).equals(citiesFinder.getLastLetter(lastCity).toUpperCase())){
                        if(!checkIfGameIsFinished(lastCity)){
                            usedCitiesManager.inputDBFeed(town);
                            String requestedLetter = citiesFinder.getLastLetter(city).toUpperCase();
                            String answerCity = findAnswerCity(requestedLetter);
                            lastCity = answerCity;
                            tv_compCity.setText(answerCity);
                            et_enterCity.setText("");
                        }else{
                            dialog.show(getSupportFragmentManager(), "Dialog fragment");
                        }
                    }else{
                        Toast.makeText(this, "Не та буква!", Toast.LENGTH_SHORT).show();
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
        } else {
            Toast.makeText(this, "Сначала введите город!",
                    Toast.LENGTH_SHORT).show();

        }
    }

    @Override
    public void onDialogButtonClick() {
        onNewGameStarted();
    }
}
