package com.madebyme.citiesgame.activities;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.util.Base64;
import android.util.Log;
import android.view.*;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.madebyme.citiesgame.*;
import com.madebyme.citiesgame.fragments.HighScoresFragment;
import com.madebyme.citiesgame.fragments.MainGameFragment;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;


public class MainActivity extends FragmentActivity {

    private MainGameFragment mainGameFragment;
    private HighScoresFragment highScoresFragment;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.source_layout);
        init();
        attachSlidingMenu();
        showFragment(mainGameFragment);
        getHashKey();

    }

    private void init(){
        mainGameFragment = MainGameFragment.newInstance();
        highScoresFragment = HighScoresFragment.newInstance();
    }

    private void showFragment(Fragment fragment){
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();

        if(fragment instanceof MainGameFragment){
            fragmentTransaction.replace(R.id.source_layout, fragment);
        }else{
            fragmentTransaction.replace(R.id.menu_frame, fragment);
        }
        fragmentTransaction.commit();
    }

    private void getHashKey(){
        PackageInfo info;
        try {
            info = getPackageManager().getPackageInfo("com.madebyme.citiesgame", PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md;
                md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                String something = new String(Base64.encode(md.digest(), 0));
                Log.e("hash key", something);
            }
        } catch (PackageManager.NameNotFoundException e1) {
            Log.e("name not found", e1.toString());
        } catch (NoSuchAlgorithmException e) {
            Log.e("no such an algorithm", e.toString());
        } catch (Exception e) {
            Log.e("exception", e.toString());
        }
    }

    private void attachSlidingMenu(){
        SlidingMenu slidingMenu = new SlidingMenu(this);
        slidingMenu.setMenu(R.layout.menu_frame);
        slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
        slidingMenu.attachToActivity(this, SlidingMenu.SLIDING_CONTENT);
        slidingMenu.setBehindOffset((int)getResources().getDimension(R.dimen.sm_behind_offset));
        showFragment(highScoresFragment);
    }
}