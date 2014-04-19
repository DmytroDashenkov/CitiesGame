package com.madebyme.citiesgame.views;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;
import com.madebyme.citiesgame.App;

public class MyTextView extends TextView{

    public MyTextView(Context context) {
        super(context);
        init();
    }

    public MyTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public MyTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    private void init(){
        setTypeface(App.getTypeface());
    }
}
