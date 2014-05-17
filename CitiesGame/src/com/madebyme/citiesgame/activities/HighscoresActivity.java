package com.madebyme.citiesgame.activities;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ListView;
import com.madebyme.citiesgame.App;
import com.madebyme.citiesgame.R;
import com.madebyme.citiesgame.adapters.HighScoresListAdapter;
import com.madebyme.citiesgame.db.DBManager;
import com.madebyme.citiesgame.views.MyTextView;

public class HighScoresActivity extends Activity {

    private HighScoresListAdapter adapter;
    private ListView listView;
    private DBManager manager;
    private MyTextView noScores;
    private int itemsAmount;

    @Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_highscore);
        noScores = (MyTextView) findViewById(R.id.no_scores);
        listView = (ListView)findViewById(R.id.all_list_view);
        manager = App.getDBManager();
	}

    @Override
    protected void onResume() {
        super.onResume();
        adapter = new HighScoresListAdapter(manager.getHighScores(),this);
        renewList();

    }

    private void renewList(){
        if(adapter.getCount() == 0){
            noScores.setVisibility(View.VISIBLE);
        } else {
            while (manager.getHighScoresRowCount() != itemsAmount){
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
