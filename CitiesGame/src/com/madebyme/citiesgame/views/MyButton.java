package com.madebyme.citiesgame.views;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.Button;
import com.madebyme.citiesgame.App;

public class MyButton extends Button {

    public MyButton(Context context) {
        super(context);
        init();
    }

    public MyButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public MyButton(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    private void init(){
        setTypeface(App.getTypeface());
    }
}
