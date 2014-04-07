package com.madebyme.citiesgame;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

public class MyDialog extends DialogFragment implements View.OnClickListener {

    private OnClickDialogButtonListener listener;
    private Button bt_ok;

    public MyDialog(OnClickDialogButtonListener listener) {
       this.listener = listener;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        getDialog().setTitle("Игра окончена!");
        View v = inflater.inflate(R.layout.game_finish_dialog, null);
        bt_ok = (Button) v.findViewById(R.id.dialog_ok);
        bt_ok.setOnClickListener(this);
        return v;
    }

    @Override
    public void onClick(View v) {
        listener.onDialogButtonClick();
        dismiss();
    }
}
