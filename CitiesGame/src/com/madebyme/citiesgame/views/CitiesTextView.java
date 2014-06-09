package com.madebyme.citiesgame.views;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;
import com.madebyme.citiesgame.App;

public class CitiesTextView extends TextView{

    public CitiesTextView(Context context) {
        super(context);
        init();
    }

    public CitiesTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public CitiesTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    private void init(){
        setTypeface(App.getTypeface());
    }
}
