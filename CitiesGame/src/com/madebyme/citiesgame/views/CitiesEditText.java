package com.madebyme.citiesgame.views;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.EditText;
import com.madebyme.citiesgame.App;

public class CitiesEditText extends EditText {

    public CitiesEditText(Context context) {
        super(context);
        init();
    }

    public CitiesEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public CitiesEditText(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    private void init(){
        setTypeface(App.getTypeface());
    }
}
