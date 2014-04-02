package com.madebyme.citiesgame;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class MyDialog extends DialogFragment implements DialogInterface.OnClickListener {

    private OnClickDialogListener listener;

    public MyDialog(OnClickDialogListener listener) {
        this.listener = listener;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        getDialog().setTitle("Игра окончена!");
        View v = inflater.inflate(R.layout.game_finish_dialog, null);
        v.findViewById(R.id.dialog_ok);
        return v;
    }

    @Override
    public void onClick(DialogInterface dialogInterface, int i) {
        listener.onClickDialogButton();
    }
}
