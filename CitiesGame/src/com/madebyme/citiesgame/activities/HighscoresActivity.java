package com.madebyme.citiesgame.activities;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;
import com.madebyme.citiesgame.App;
import com.madebyme.citiesgame.GameSave;
import com.madebyme.citiesgame.R;
import com.madebyme.citiesgame.adapters.HighScoresListAdapter;
import com.madebyme.citiesgame.highscoresdb.HighScoresDBManager;
import com.madebyme.citiesgame.views.MyTextView;

import java.util.ArrayList;

public class HighScoresActivity extends Activity {

    private ArrayList<GameSave> saves;
    private HighScoresListAdapter adapter;
    private HighScoresDBManager manager;
    private LinearLayout layout;
    private MyTextView noScores;
    private int itemsAmount = 0;

    @Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_highscore);
        layout = (LinearLayout) findViewById(R.id.layout_high_score);
        noScores = (MyTextView) findViewById(R.id.no_scores);
	}

    @Override
    protected void onResume() {
        super.onResume();
        manager = App.getHighScoresDBManager();
        saves = manager.getHighScores();
        adapter = new HighScoresListAdapter(saves,
                (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE));
        renewList();

    }

    private void renewList(){
        if(adapter.getCount() == 0){
            noScores.setVisibility(View.VISIBLE);
        } else {
            while (manager.getRowCount() != itemsAmount){
                if (itemsAmount <= 10){
                    addListItem();
                    itemsAmount++;
                }else{
                    return;
                }
            }
        }
    }

    private void addListItem(){
        ListView listView = new ListView(this);
        listView.setAdapter(adapter);
        layout.addView(listView,
                new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
    }

}
