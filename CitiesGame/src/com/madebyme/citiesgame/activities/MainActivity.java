package com.madebyme.citiesgame.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.*;
import android.view.View.OnClickListener;
import android.widget.*;
import com.madebyme.citiesgame.*;
import com.madebyme.citiesgame.fragments.MyDialog;
import com.madebyme.citiesgame.listeners.OnClickDialogButtonListener;
import com.madebyme.citiesgame.listeners.OnDataLoadedListener;
import com.madebyme.citiesgame.db.CitiesFinder;
import com.madebyme.citiesgame.db.DBManager;
import com.madebyme.citiesgame.models.City;
import com.madebyme.citiesgame.tasks.MyTask;
import com.madebyme.citiesgame.views.MyButton;
import com.madebyme.citiesgame.views.MyEditText;
import com.madebyme.citiesgame.views.MyTextView;

public class MainActivity extends FragmentActivity implements OnClickListener,
        OnDataLoadedListener, OnClickDialogButtonListener{

    private MyButton btOk;
    private MyEditText etEnterCity;
    private MyTextView tvCompCity;
    private MyButton btNewGame;
    private CitiesFinder citiesFinder;
    private DBManager manager;
    private ProgressBar pbDbLoadingBar;
    private String lastCity;
    private SharedPreferences pref;
    private MyTextView tvWaitPlease;
    private int score;
    private MyDialog dialog;
    private boolean dialogIsCalled;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initComps();
        manager.initCursor();
        if (!manager.initCursor()) {
            MyTask task = new MyTask(this);
            task.execute(this);
        }
        lastCity = loadLastCityAndScoreFromPreferences();
        if(lastCity.length() != 0)
            tvCompCity.setText(lastCity);
        else
            onNewGameStarted(false, false);
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
        startActivity(new Intent(this, HighScoresActivity.class));
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
        score = 0;
        dialog = MyDialog.newInstance(this);
    }

    @Override
    public void onClick(View src) {
        switch (src.getId()){
            case R.id.ok:
                onButtonOkClicked();
                break;
            case R.id.bt_new_game:
                if (!dialogIsCalled)
                    onNewGameStarted(true, false);
                dialogIsCalled = true;
                break;
        }
    }

    private String findAnswerCity(String firstLetter) {
        City city;
        do {
            city = manager.findCityByFirstLetter(firstLetter);
        } while (manager.checkIfUsed(city));
        manager.inputUsedCity(city);

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

    private void onNewGameStarted(boolean shouldCallDialog, boolean userWin){
        if(shouldCallDialog){
            callDialog(userWin);
        }
        manager.deleteAllUsedCities();
        tvCompCity.setText("Ваш ход!");
        lastCity = null;
        score = 0;
        etEnterCity.setText("");


    }

    private boolean checkIfGameIsFinished(String lastUsedCity){
        if(lastUsedCity != null){
            String letter = citiesFinder.getLastLetter(lastUsedCity).toUpperCase();
            return manager.compereTablesOfUsedAndGeneral(letter);
        }else{
            return false;
        }
    }

    private void onButtonOkClicked(){
        String city = etEnterCity.getText().toString();
        if (city.length() != 0) {
            City town = new City(city, citiesFinder.getFirstLetter(city));
            if (manager.checkCityExistence(town)) {
                if (!manager.checkIfUsed(town)) {
                        if(lastCity == null || citiesFinder.getFirstLetter(city).equals(citiesFinder.getLastLetter(lastCity).toUpperCase())){
                            if(!checkIfGameIsFinished(lastCity)){
                                manager.inputUsedCity(town);
                                String requestedLetter = citiesFinder.getLastLetter(city).toUpperCase();
                                String answerCity = findAnswerCity(requestedLetter);
                                lastCity = answerCity;
                                tvCompCity.setText(answerCity);
                                etEnterCity.setText("");
                                score++;
                            }else{
                                onNewGameStarted(true, true);
                            }
                        }else{
                        Toast.makeText(this, R.string.wrong_letter, Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(this, R.string.used, Toast.LENGTH_SHORT).show();
                    etEnterCity.setText(null);
                }
            } else {
                Toast.makeText(this, R.string.wrong_city, Toast.LENGTH_SHORT)
                        .show();
                etEnterCity.setText(null);
            }
        } else {
            Toast.makeText(this, R.string.enter_city_first, Toast.LENGTH_SHORT).show();

        }
    }

    @Override
    public void onDialogButtonClick() {
        onNewGameStarted(false, false);

    }

    private void callDialog(boolean userWin){
        if(userWin)
            score = score + Constants.FOR_VICTORY_PRISE;
        Bundle bundle = new Bundle();
        bundle.putInt("score", score);
        dialog.setArguments(bundle);
        dialog.show(getSupportFragmentManager(), "Dialog fragment");

    }
}