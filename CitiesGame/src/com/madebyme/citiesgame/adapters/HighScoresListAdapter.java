package com.madebyme.citiesgame.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import com.madebyme.citiesgame.App;
import com.madebyme.citiesgame.models.GameSave;
import com.madebyme.citiesgame.R;
import com.madebyme.citiesgame.views.CitiesTextView;

import java.util.ArrayList;

public class HighScoresListAdapter extends BaseAdapter{

    private ArrayList<GameSave> saves;
    private LayoutInflater inflater;
    private Context context;


    public HighScoresListAdapter(ArrayList<GameSave> saves, Context context) {
        this.saves = saves;
        this.inflater = LayoutInflater.from(context);
        this.context = context;
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
        CitiesTextView tvUserName;
        CitiesTextView tvDate;
        CitiesTextView tvScore;
    }

    @Override
    public View getView(int position, View v, ViewGroup parent) {

        ViewHolder holder;
        saves = new App().getDBManager().getHighScores();

        if(v == null){
            v = inflater.inflate(R.layout.list_item, null);
            holder = new ViewHolder();
            holder.tvUserName = (CitiesTextView) v.findViewById(R.id.user_name_field);
            holder.tvDate = (CitiesTextView) v.findViewById(R.id.date_field);
            holder.tvScore = (CitiesTextView) v.findViewById(R.id.score_field);
            v.setTag(holder);
        }
        else{
            holder = (ViewHolder)v.getTag();
        }

        GameSave gameSave = saves.get(position);
        holder.tvUserName.setText(gameSave.getName());
        holder.tvScore.setText(context.getResources().getString(R.string.score) + String.valueOf(gameSave.getScore()));
        holder.tvDate.setText(gameSave.getDate());

        return v;
    }

    public void clearSaves(){
        saves.clear();
    }


}
