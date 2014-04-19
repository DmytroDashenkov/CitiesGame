package com.madebyme.citiesgame.activitiesandfragments.fragments;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import com.madebyme.citiesgame.listeners.OnClickDialogButtonListener;
import com.madebyme.citiesgame.R;
import com.madebyme.citiesgame.views.MyButton;
import com.madebyme.citiesgame.views.MyTextView;

public class MyDialog extends DialogFragment implements View.OnClickListener {

    private OnClickDialogButtonListener listener;
    private MyButton bt_ok;
    private MyTextView tvScore;

    public MyDialog(OnClickDialogButtonListener listener) {
       this.listener = listener;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        getDialog().setTitle("Игра окончена!");
        View v = inflater.inflate(R.layout.game_finish_dialog, null);
        Bundle bundle = getArguments();
        bt_ok = (MyButton) v.findViewById(R.id.dialog_ok);
        bt_ok.setOnClickListener(this);
        tvScore = (MyTextView) v.findViewById(R.id.score);
        tvScore.setText(bundle.getInt("score", 0));
        return v;
    }

    @Override
    public void onClick(View v) {
        listener.onDialogButtonClick();
        dismiss();
    }

    public static MyDialog newInstance(OnClickDialogButtonListener listener){
        MyDialog dialog = new MyDialog(listener);
        return dialog;
    }
}
