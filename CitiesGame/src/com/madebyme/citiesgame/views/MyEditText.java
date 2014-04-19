package com.madebyme.citiesgame.views;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.EditText;
import com.madebyme.citiesgame.App;

public class MyEditText extends EditText {

    public MyEditText(Context context) {
        super(context);
        init();
    }

    public MyEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public MyEditText(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    private void init(){
        setTypeface(App.getTypeface());
    }
}
