package com.madebyme.citiesgame.activities;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.view.*;
import com.madebyme.citiesgame.*;
import com.madebyme.citiesgame.fragments.HighScoresFragment;
import com.madebyme.citiesgame.fragments.MainGameFragment;


public class MainActivity extends FragmentActivity{

    private MainGameFragment mainGameFragment;
    private HighScoresFragment highScoresFragment;
    private int currentFragment;
    private final int MAIN_GAME_FRAGMENT_INDEX = 0;
    private final int HIGH_SCORES_FRAGMENT_INDEX = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.source_layout);
        init();
        showFragment(mainGameFragment, false);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(currentFragment != HIGH_SCORES_FRAGMENT_INDEX){
            showFragment(highScoresFragment, true);
        }
        return super.onOptionsItemSelected(item);
    }

    private void init(){
        mainGameFragment = MainGameFragment.newInstance();
        highScoresFragment = HighScoresFragment.newInstance();
    }

    private void showFragment(Fragment fragment, boolean shouldAddToBackStack){
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.source_layout, fragment);
        if(shouldAddToBackStack){
            fragmentTransaction.addToBackStack(null);
        }
        fragmentTransaction.commit();
        if(fragment instanceof MainGameFragment){
            currentFragment = MAIN_GAME_FRAGMENT_INDEX;
        }else{
            currentFragment = HIGH_SCORES_FRAGMENT_INDEX;
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        currentFragment = MAIN_GAME_FRAGMENT_INDEX;
    }

}