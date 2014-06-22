package com.madebyme.citiesgame.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import com.madebyme.citiesgame.App;
import com.madebyme.citiesgame.R;
import com.madebyme.citiesgame.adapters.HighScoresListAdapter;
import com.madebyme.citiesgame.db.DBManager;
import com.madebyme.citiesgame.views.CitiesTextView;

public class HighScoresFragment extends Fragment{

    private HighScoresListAdapter adapter;
    private ListView listView;
    private DBManager manager;
    private CitiesTextView noScores;
    private int itemsAmount;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.highscores_fragment, null);
        init(view);

        return view;
    }

    private void init(View view){
        noScores = (CitiesTextView) view.findViewById(R.id.no_scores);
        listView = (ListView) view.findViewById(R.id.all_list_view);
        manager = App.getDBManager();
    }

    private void renewList(){
        adapter = new HighScoresListAdapter(manager.getHighScores(),getActivity());
        if(adapter.getCount() == 0){
            noScores.setVisibility(View.VISIBLE);
        } else {
            while (manager.getHighScoresRowCount() != itemsAmount){
                if (itemsAmount < 10){
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

    private void deleteList(){
        adapter.clearSaves();
        adapter.notifyDataSetChanged();
        itemsAmount = 0;
    }


    public static HighScoresFragment newInstance(){
        HighScoresFragment highScoresFragment = new HighScoresFragment();
        return highScoresFragment;
    }

    @Override
    public void onPause() {
        super.onPause();
        deleteList();
    }

    @Override
    public void onResume() {
        super.onResume();
        renewList();
    }
}
