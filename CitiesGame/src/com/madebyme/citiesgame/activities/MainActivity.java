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
import com.madebyme.citiesgame.*;
import com.madebyme.citiesgame.fragments.HighScoresFragment;
import com.madebyme.citiesgame.fragments.MainGameFragment;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;


public class MainActivity extends FragmentActivity {

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
        getHashKey();
        /*Uri targetUrl = AppLinks.getTargetUrl(getIntent());
        if (targetUrl != null) {
            Log.i("Activity", "Target URL: " + targetUrl.toString());
        }*/
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
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

    private void getHashKey(){
        PackageInfo info;
        try {
            info = getPackageManager().getPackageInfo("com.madebyme.citiesgame", PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md;
                md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                String something = new String(Base64.encode(md.digest(), 0));
                //String something = new String(Base64.encodeBytes(md.digest()));
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
}