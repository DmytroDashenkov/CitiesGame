package com.madebyme.citiesgame.fragments;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.madebyme.citiesgame.App;
import com.madebyme.citiesgame.GameSave;
import com.madebyme.citiesgame.highscoresdb.HighScoresDBManager;
import com.madebyme.citiesgame.listeners.OnClickDialogButtonListener;
import com.madebyme.citiesgame.R;
import com.madebyme.citiesgame.listeners.UserNameCallBack;
import com.madebyme.citiesgame.views.MyButton;
import com.madebyme.citiesgame.views.MyEditText;
import com.madebyme.citiesgame.views.MyTextView;

import java.text.SimpleDateFormat;
import java.util.Date;

public class MyDialog extends DialogFragment implements View.OnClickListener, UserNameCallBack {

    private OnClickDialogButtonListener listener;
    private MyButton bt_ok;
    private MyTextView tvScore;
    private MyEditText etUserName;
    private Bundle bundle;

    public MyDialog(OnClickDialogButtonListener listener) {
       this.listener = listener;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        getDialog().setTitle("Игра окончена!");
        View v = inflater.inflate(R.layout.game_finish_dialog, null);
        bundle = getArguments();
        bt_ok = (MyButton) v.findViewById(R.id.dialog_ok);
        bt_ok.setOnClickListener(this);
        etUserName = (MyEditText) v.findViewById(R.id.user_name);
        tvScore = (MyTextView) v.findViewById(R.id.score);

        tvScore.setText(String.valueOf(bundle.getInt("score", 0)));
        return v;
    }

    @Override
    public void onClick(View v) {
        listener.onDialogButtonClick();
        App.getHighScoresDBManager().inputDBFeed(new GameSave(getUserName(), bundle.getInt("score", 0), getCurrentDate()));
        dismiss();
    }

    public static MyDialog newInstance(OnClickDialogButtonListener listener){
        MyDialog dialog = new MyDialog(listener);
        return dialog;
    }

    private String getCurrentDate(){
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm");
        return sdf.format(date);
    }

    @Override
    public String getUserName() {
        String name = etUserName.getText().toString();
        if (name.equals(""))
            name = "Игрок";
        return name;
    }
}

