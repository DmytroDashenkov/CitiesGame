package com.madebyme.citiesgame.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import com.madebyme.citiesgame.App;
import com.madebyme.citiesgame.models.GameSave;
import com.madebyme.citiesgame.R;
import com.madebyme.citiesgame.views.MyTextView;

import java.util.ArrayList;

public class HighScoresListAdapter extends BaseAdapter{

    private ArrayList<GameSave> saves;
    private LayoutInflater inflater;


    public HighScoresListAdapter(ArrayList<GameSave> saves, LayoutInflater inflater) {
        this.saves = saves;
        this.inflater = inflater;
    }

    public ArrayList<GameSave> getSaves(){
        return saves;
    }

    @Override
    public int getCount() {
        return saves.size();
    }

    @Override
    public Object getItem(int i) {
        return saves.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    class ViewHolder{
        MyTextView tvUserName;
        MyTextView tvDate;
        MyTextView tvScore;
    }

    @Override
    public View getView(int position, View v, ViewGroup parent) {

        ViewHolder holder;
        saves = new App().getDBManager().getHighScores();

        if(v == null){
            v = inflater.inflate(R.layout.list_item, null);
            holder = new ViewHolder();
            holder.tvUserName = (MyTextView) v.findViewById(R.id.user_name_field);
            holder.tvDate = (MyTextView) v.findViewById(R.id.date_field);
            holder.tvScore = (MyTextView) v.findViewById(R.id.score_field);
            v.setTag(holder);
        }
        else{
            holder = (ViewHolder)v.getTag();
        }

        GameSave gameSave = saves.get(position);
        holder.tvUserName.setText(gameSave.getName());
        holder.tvScore.setText("Счет: " + String.valueOf(gameSave.getScore()));
        holder.tvDate.setText(gameSave.getDate());

        return v;
    }

}
