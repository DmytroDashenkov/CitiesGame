package com.madebyme.citiesgame.activitiesandfragments.activities;

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
import com.madebyme.citiesgame.*;
import com.madebyme.citiesgame.activitiesandfragments.fragments.MyDialog;
import com.madebyme.citiesgame.listeners.OnClickDialogButtonListener;
import com.madebyme.citiesgame.listeners.OnDataLoadedListener;
import com.madebyme.citiesgame.maindb.CitiesFinder;
import com.madebyme.citiesgame.maindb.DBManager;
import com.madebyme.citiesgame.supportingdb.UsedCitiesManager;
import com.madebyme.citiesgame.tasks.MyTask;
import com.madebyme.citiesgame.views.MyButton;
import com.madebyme.citiesgame.views.MyEditText;
import com.madebyme.citiesgame.views.MyTextView;

public class MainActivity extends FragmentActivity implements OnClickListener, OnDataLoadedListener, OnClickDialogButtonListener {

    private MyButton btOk;
    private MyEditText etEnterCity;
    private MyTextView tvCompCity;
    private MyButton btNewGame;
    private CitiesFinder citiesFinder;
    private DBManager manager;
    private Cursor cursor;
    private UsedCitiesManager usedCitiesManager;
    private ProgressBar pbDbLoadingBar;
    private String lastCity;
    private SharedPreferences pref;
    private MyDialog dialog;
    private MyTextView tvWaitPlease;
    private int score;

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
        lastCity = loadLastCityAndScoreFromPreferences();
        if(lastCity.length() != 0)
            tvCompCity.setText(lastCity);
        else
            onNewGameStarted(false);
    }

    @Override
    protected void onStop() {
        super.onStop();
        saveLastCityAndScoreInPreference(lastCity);
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
        btOk = (MyButton) findViewById(R.id.ok);
        etEnterCity = (MyEditText) findViewById(R.id.user_city);
        tvCompCity = (MyTextView) findViewById(R.id.device_city);
        pbDbLoadingBar = (ProgressBar) findViewById(R.id.db_loading_progress);
        btNewGame = (MyButton) findViewById(R.id.bt_new_game);
        tvWaitPlease = (MyTextView) findViewById(R.id.pb_name);
        citiesFinder = new CitiesFinder(this);
        btOk.setOnClickListener(this);
        btNewGame.setOnClickListener(this);
        manager = App.getDBManager();
        usedCitiesManager = new UsedCitiesManager(this);
        dialog = MyDialog.newInstance(this);
        score = 0;
    }

    @Override
    public void onClick(View src) {
        switch (src.getId()){
            case R.id.ok:
                onButtonOkClicked();
                break;
            case R.id.bt_new_game:
                onNewGameStarted(true);
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
            btOk.setVisibility(View.VISIBLE);
            btNewGame.setVisibility(View.VISIBLE);
            etEnterCity.setVisibility(View.VISIBLE);
            tvCompCity.setVisibility(View.VISIBLE);
            pbDbLoadingBar.setVisibility(View.INVISIBLE);
            tvWaitPlease.setVisibility(View.INVISIBLE);
        }else{
            btOk.setVisibility(View.INVISIBLE);
            btNewGame.setVisibility(View.INVISIBLE);
            etEnterCity.setVisibility(View.INVISIBLE);
            tvCompCity.setVisibility(View.INVISIBLE);
            pbDbLoadingBar.setVisibility(View.VISIBLE);
            tvWaitPlease.setVisibility(View.VISIBLE);
        }
    }

    private void saveLastCityAndScoreInPreference(String city){
        pref = getPreferences(MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString("Last Called City", city);
        editor.putInt("Score", score);
        editor.commit();
    }

    private String loadLastCityAndScoreFromPreferences(){
        pref = getPreferences(MODE_PRIVATE);
        score = getPreferences(MODE_PRIVATE).getInt("Score", 0);
        return pref.getString("Last Called City", "");
    }

    private void onNewGameStarted(boolean shouldCallDialog){
        usedCitiesManager.deleteAll();
        tvCompCity.setText("Ваш ход!");
        lastCity = null;
        score = 0;
        etEnterCity.setText("");
        saveScore();
        if(shouldCallDialog)
            dialog.show(getSupportFragmentManager(), "Dialog fragment");
    }

    private boolean checkIfGameIsFinished(String lastUsedCity){
        if(lastUsedCity != null){
            String letter = citiesFinder.getLastLetter(lastUsedCity).toUpperCase();
            return manager.compereTablesOfUsedAndGeneral(letter, this);
        }else{
            return false;
        }
    }

    private void onButtonOkClicked(){
        String city = etEnterCity.getText().toString();
        if (city.length() != 0) {
            City town = new City(city, citiesFinder.getFirstLetter(city));
            if (manager.checkCityExistans(town)) {
                if (!usedCitiesManager.checkIfUsed(town)) {
                        if(lastCity == null || citiesFinder.getFirstLetter(city).equals(citiesFinder.getLastLetter(lastCity).toUpperCase())){
                            if(!checkIfGameIsFinished(lastCity)){
                                usedCitiesManager.inputDBFeed(town);
                                String requestedLetter = citiesFinder.getLastLetter(city).toUpperCase();
                                String answerCity = findAnswerCity(requestedLetter);
                                lastCity = answerCity;
                                tvCompCity.setText(answerCity);
                                etEnterCity.setText("");
                                score++;
                            }else{
                                dialog.show(getSupportFragmentManager(), "Dialog fragment");
                            }
                        }else{
                        Toast.makeText(this, "Не та буква!", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(this, "Было!", Toast.LENGTH_SHORT).show();
                    etEnterCity.setText(null);
                }
            } else {
                Toast.makeText(this, "Такого города нет!", Toast.LENGTH_SHORT)
                        .show();
                etEnterCity.setText(null);
            }
        } else {
            Toast.makeText(this, "Сначала введите город!",
                    Toast.LENGTH_SHORT).show();

        }
    }

    @Override
    public void onDialogButtonClick() {
        onNewGameStarted(false);
    }

    private void saveScore(){
        Bundle bundle = new Bundle();
        bundle.putInt("score", score);
        dialog.setArguments(bundle);

    }
}
