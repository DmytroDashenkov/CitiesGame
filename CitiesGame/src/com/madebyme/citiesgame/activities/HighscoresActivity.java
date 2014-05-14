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
import com.madebyme.citiesgame.R;
import com.madebyme.citiesgame.adapters.HighScoresListAdapter;
import com.madebyme.citiesgame.highscoresdb.HighScoresDBManager;
import com.madebyme.citiesgame.views.MyTextView;

public class HighScoresActivity extends Activity {

    private HighScoresListAdapter adapter;
    private HighScoresDBManager manager;
    private ListView listView;
    private LinearLayout layout;
    private MyTextView noScores;
    private int itemsAmount;

    @Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_highscore);
        layout = (LinearLayout) findViewById(R.id.layout_high_score);
        noScores = (MyTextView) findViewById(R.id.no_scores);
        listView = (ListView)findViewById(R.id.all_list_view);
	}

    @Override
    protected void onResume() {
        super.onResume();
        manager = App.getHighScoresDBManager();
        adapter = new HighScoresListAdapter(manager.getHighScores(),
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
        listView.setAdapter(adapter);
    }

}
