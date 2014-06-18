package com.madebyme.citiesgame.fragments;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.madebyme.citiesgame.App;
import com.madebyme.citiesgame.listeners.ShareButtonPressedListener;
import com.madebyme.citiesgame.listeners.OnClickDialogButtonListener;
import com.madebyme.citiesgame.listeners.ShareFlagHolder;
import com.madebyme.citiesgame.models.GameSave;
import com.madebyme.citiesgame.R;
import com.madebyme.citiesgame.listeners.UserNameCallBack;
import com.madebyme.citiesgame.views.CitiesButton;
import com.madebyme.citiesgame.views.CitiesEditText;
import com.madebyme.citiesgame.views.CitiesTextView;
import com.madebyme.citiesgame.views.FacebookShareButton;

import java.text.SimpleDateFormat;
import java.util.Date;

public class GameOverDialog extends DialogFragment implements View.OnClickListener, UserNameCallBack{

    private OnClickDialogButtonListener listener;
    private CitiesButton bt_ok;
    private FacebookShareButton bt_share;
    private CitiesTextView tvScore;
    private CitiesEditText etUserName;
    private Bundle bundle;
    private ShareButtonPressedListener callback;
    private boolean shareFlag;

    public void setListener(OnClickDialogButtonListener listener) {
        this.listener = listener;
    }

    public void setCallback(ShareButtonPressedListener callback) {
        this.callback = callback;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        getDialog().setTitle(R.string.game_over);
        View v = inflater.inflate(R.layout.game_finish_dialog, null);
        init(v);
        tvScore.setText(String.valueOf(bundle.getInt("score", 0)));
        return v;
    }

    private void init(View v){
        bundle = getArguments();
        bt_ok = (CitiesButton) v.findViewById(R.id.dialog_ok);
        bt_ok.setOnClickListener(this);
        etUserName = (CitiesEditText) v.findViewById(R.id.user_name);
        tvScore = (CitiesTextView) v.findViewById(R.id.score);
        bt_share = (FacebookShareButton) v.findViewById(R.id.share);
        bt_share.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        int result = bundle.getInt("score", 0);
        switch (v.getId()){
            case R.id.share:
                callback.onShareButtonClick();
            case R.id.dialog_ok:
                listener.onDialogButtonClick();
                App.getDBManager().inputHighScore(new GameSave(getUserName(), result, getCurrentDate()));
                dismiss();
        }
    }

    public static GameOverDialog newInstance(OnClickDialogButtonListener listener, ShareButtonPressedListener callback){
        GameOverDialog dialog = new GameOverDialog();
        dialog.setListener(listener);
        dialog.setCallback(callback);
        return dialog;
    }

    private String getCurrentDate(){
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yy HH:mm");
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

