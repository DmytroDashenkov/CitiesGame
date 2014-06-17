package com.madebyme.citiesgame.fragments;


import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;
import com.facebook.UiLifecycleHelper;
import com.madebyme.citiesgame.App;
import com.madebyme.citiesgame.Constants;
import com.madebyme.citiesgame.R;
import com.madebyme.citiesgame.db.CitiesFinder;
import com.madebyme.citiesgame.db.DBManager;
import com.madebyme.citiesgame.facebook.FacebookManager;
import com.madebyme.citiesgame.listeners.ShareButtonPressedListener;
import com.madebyme.citiesgame.listeners.OnClickDialogButtonListener;
import com.madebyme.citiesgame.listeners.OnDataLoadedListener;
import com.madebyme.citiesgame.models.City;
import com.madebyme.citiesgame.tasks.MyTask;
import com.madebyme.citiesgame.views.CitiesButton;
import com.madebyme.citiesgame.views.CitiesEditText;
import com.madebyme.citiesgame.views.CitiesTextView;

public class MainGameFragment extends Fragment implements View.OnClickListener,
        OnDataLoadedListener, OnClickDialogButtonListener, ShareButtonPressedListener{

    private CitiesButton btOk;
    private CitiesEditText etEnterCity;
    private CitiesTextView tvScore;
    private CitiesTextView tvCompCity;
    private CitiesButton btNewGame;
    private CitiesFinder citiesFinder;
    private DBManager manager;
    private ProgressBar pbDbLoadingBar;
    private String lastCity;
    private SharedPreferences pref;
    private CitiesTextView tvWaitPlease;
    private int score;
    private GameOverDialog dialog;
    private UiLifecycleHelper uiHelper;
    private FacebookManager facebookManager;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.main_fragment, null);

        init(view);
        changeOkVisibility(false);
        if (!manager.initCursor()) {
            MyTask task = new MyTask(this);
            task.execute(getActivity());
        }
        lastCity = loadLastCityAndScoreFromPreferences();
        if (lastCity.length() != 0) {
            tvCompCity.setText(lastCity);
        } else {
            onNewGameStarted(false, false);
        }

        return view;
    }

    @Override
    public void onPause() {
        super.onPause();
        saveLastCityAndScoreInPreference(lastCity);
        uiHelper.onPause();
    }

    private void init(View view) {
        btOk = (CitiesButton) view.findViewById(R.id.ok);
        etEnterCity = (CitiesEditText) view.findViewById(R.id.user_city);
        tvCompCity = (CitiesTextView) view.findViewById(R.id.device_city);
        pbDbLoadingBar = (ProgressBar) view.findViewById(R.id.db_loading_progress);
        btNewGame = (CitiesButton) view.findViewById(R.id.bt_new_game);
        tvWaitPlease = (CitiesTextView) view.findViewById(R.id.pb_name);
        tvScore = (CitiesTextView) view.findViewById(R.id.current_score);
        citiesFinder = new CitiesFinder(getActivity());
        btOk.setOnClickListener(this);
        btNewGame.setOnClickListener(this);
        manager = App.getDBManager();
        score = 0;
        dialog = GameOverDialog.newInstance(this, this);
        etEnterCity.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
                changeOkVisibility(etEnterCity.getText().length() != 0);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    @Override
    public void onClick(View src) {
        switch (src.getId()) {
            case R.id.ok:
                onButtonOkClicked();
                break;
            case R.id.bt_new_game:
                onNewGameStarted(true, false);
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
        if (isLoaded) {
            btOk.setVisibility(View.VISIBLE);
            btNewGame.setVisibility(View.VISIBLE);
            etEnterCity.setVisibility(View.VISIBLE);
            tvCompCity.setVisibility(View.VISIBLE);
            pbDbLoadingBar.setVisibility(View.INVISIBLE);
            tvWaitPlease.setVisibility(View.INVISIBLE);
            tvScore.setVisibility(View.VISIBLE);
        } else {
            btOk.setVisibility(View.INVISIBLE);
            btNewGame.setVisibility(View.INVISIBLE);
            etEnterCity.setVisibility(View.INVISIBLE);
            tvCompCity.setVisibility(View.INVISIBLE);
            pbDbLoadingBar.setVisibility(View.VISIBLE);
            tvWaitPlease.setVisibility(View.VISIBLE);
            tvScore.setVisibility(View.INVISIBLE);
        }
    }

    private void saveLastCityAndScoreInPreference(String city) {
        pref = getActivity().getPreferences(Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString("Last Called City", city);
        editor.putInt("Score", score);
        editor.commit();
    }

    private String loadLastCityAndScoreFromPreferences() {
        pref = getActivity().getPreferences(Activity.MODE_PRIVATE);
        score = getActivity().getPreferences(Activity.MODE_PRIVATE).getInt("Score", 0);
        return pref.getString("Last Called City", "");
    }

    private void onNewGameStarted(boolean shouldCallDialog, boolean userWin) {
        if (shouldCallDialog) {
            callDialog(userWin);
            facebookManager.setResultToPost(score);
        }
        manager.deleteAllUsedCities();
        tvCompCity.setText(R.string.your_move);
        lastCity = null;
        score = 0;
        etEnterCity.setText("");

        tvScore.setText(getResources().getString(R.string.score) + String.valueOf(score));


    }

    private boolean checkIfGameIsFinished(String lastUsedCity) {
        if (lastUsedCity != null) {
            String letter = citiesFinder.getLastLetter(lastUsedCity).toUpperCase();
            return manager.compereTablesOfUsedAndGeneral(letter);
        } else {
            return false;
        }
    }

    private void onButtonOkClicked() {
        String city = etEnterCity.getText().toString();
        City town = new City(city, citiesFinder.getFirstLetter(city));
        if (manager.checkCityExistence(town)) {
            if (!manager.checkIfUsed(town)) {
                if (lastCity == null || citiesFinder.getFirstLetter(city).equals(citiesFinder.getLastLetter(lastCity).toUpperCase())) {
                    if (!checkIfGameIsFinished(lastCity)) {
                        manager.inputUsedCity(town);
                        String requestedLetter = citiesFinder.getLastLetter(city).toUpperCase();
                        String answerCity = findAnswerCity(requestedLetter);
                        lastCity = answerCity;
                        tvCompCity.setText(answerCity);
                        etEnterCity.setText("");
                        score++;
                        tvScore.setText(getResources().getString(R.string.score) + String.valueOf(score));
                    } else {
                        onNewGameStarted(true, true);
                    }
                } else {
                    Toast.makeText(getActivity(), R.string.wrong_letter, Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(getActivity(), R.string.used, Toast.LENGTH_SHORT).show();
                etEnterCity.setText(null);
            }
        } else {
            Toast.makeText(getActivity(), R.string.wrong_city, Toast.LENGTH_SHORT)
                    .show();
            etEnterCity.setText(null);
        }
    }

    @Override
    public void onDialogButtonClick() {
        onNewGameStarted(false, false);

    }

    private void callDialog(boolean userWin) {
        if (userWin)
            score = score + Constants.FOR_VICTORY_PRISE;
        Bundle bundle = new Bundle();
        bundle.putInt("score", score);
        dialog.setArguments(bundle);
        dialog.show(getActivity().getSupportFragmentManager(), "Dialog fragment");

    }

    public static MainGameFragment newInstance(){
        MainGameFragment mainGameFragment = new MainGameFragment();
        return mainGameFragment;
    }

    private void changeOkVisibility(boolean toVisible){
        if (toVisible) {
            btOk.setVisibility(View.VISIBLE);
        }else{
            btOk.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        uiHelper.onResume();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        facebookManager = new FacebookManager(getActivity());
        uiHelper = new UiLifecycleHelper(getActivity(), facebookManager.getCallback());
        uiHelper.onCreate(savedInstanceState);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        uiHelper.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        uiHelper.onDestroy();
    }

    @Override
    public void onShareButtonClick() {
        facebookManager.loginToFb(getActivity(), this);
    }
}
