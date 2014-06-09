package com.madebyme.citiesgame.views;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.Button;
import com.madebyme.citiesgame.App;
import com.madebyme.citiesgame.R;

public class FacebookShareButton extends Button{

    public FacebookShareButton(Context context) {
        super(context);
        init();
    }

    public FacebookShareButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public FacebookShareButton(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    private void init(){
        setTypeface(App.getFacebookTypeface());
        setBackgroundColor(getResources().getColor(R.color.facebook_bg));
        setTextColor(getResources().getColor(R.color.white));
        setTextSize(getResources().getDimension(R.dimen.f_letter_size));


    }
}
