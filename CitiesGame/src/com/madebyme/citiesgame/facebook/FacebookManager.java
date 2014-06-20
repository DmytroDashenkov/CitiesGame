package com.madebyme.citiesgame.facebook;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.widget.Toast;
import com.facebook.*;
import com.facebook.android.Facebook;
import com.facebook.model.GraphUser;
import com.facebook.widget.FacebookDialog;
import com.facebook.widget.WebDialog;
import com.madebyme.citiesgame.Constants;
import com.madebyme.citiesgame.R;
import com.madebyme.citiesgame.listeners.FbDialogCallBack;
import com.madebyme.citiesgame.listeners.ShareFlagHolder;

public class FacebookManager {

    private Context context;
    private int resultToPost;
    private FbDialogCallBack fbDialogCallBack;
    private ShareFlagHolder shareFlagHolder;

    private Session.StatusCallback callback = new Session.StatusCallback() {

        @Override
        public void call(Session session, SessionState state,
                         Exception exception) {
            if (shareFlagHolder.getFlag()){
                postHighScoreOnFacebook();
                shareFlagHolder.setFlag(false);
            }

        }
    };

    public FacebookManager(Context context, FbDialogCallBack fbDialogCallBack, ShareFlagHolder shareFlagHolder) {
        this.context = context;
        this.fbDialogCallBack = fbDialogCallBack;
        this.shareFlagHolder = shareFlagHolder;
    }


    public void loginToFb(Context context, Fragment fragment) {
        Session session = Session.getActiveSession();
        if (!session.isOpened() && !session.isClosed()) {
            session.openForRead(new Session.OpenRequest(fragment).setCallback(callback));
        } else {
            Session.openActiveSession(context, fragment, true, callback);
        }
    }

    public void postHighScoreOnFacebook(){
        if (FacebookDialog.canPresentShareDialog(context, FacebookDialog.ShareDialogFeature.SHARE_DIALOG)){
            shareWithFbDialog();
        }else{
            shareWithWebDialog();
        }
    }


    public Session.StatusCallback getCallback() {
        return callback;
    }

    private void shareWithFbDialog(){
        fbDialogCallBack.share(resultToPost);
    }

    private void shareWithWebDialog(){
        Bundle params = new Bundle();
        params.putString("name", "Cities Game");
        params.putString("caption", "Новый рекорд!");
        params.putString("description", formPostDescription());
        params.putString("link", Constants.GOOGLE_PLAY_URL);
        params.putString("picture", Constants.ICON_URL);


        WebDialog feedDialog =
                new WebDialog.FeedDialogBuilder(context,
                        Session.getActiveSession(),
                        params)
                .setOnCompleteListener(new WebDialog.OnCompleteListener() {

                    @Override
                    public void onComplete(Bundle values,
                                           FacebookException error) {
                        if (error == null) {
                            final String postId = values.getString("post_id");
                            if (postId != null) {
                                Toast.makeText(context,
                                        context.getResources().getString(R.string.published),
                                        Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(context,
                                        context.getResources().getString(R.string.publish_canceled),
                                        Toast.LENGTH_SHORT).show();
                            }
                        } else if (error instanceof FacebookOperationCanceledException) {
                            Toast.makeText(context,
                                    context.getResources().getString(R.string.publish_canceled),
                                    Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(context,
                                    context.getResources().getString(R.string.network_error),
                                    Toast.LENGTH_SHORT).show();
                        }
                    }

                })
                .build();
        feedDialog.show();
    }

    public String formPostDescription(){
        StringBuilder sb = new StringBuilder();
        sb.append("Я поставил новый рекорд в Cities Game- лучшей игре в города для Android! Мой рекорд: ");
        sb.append(String.valueOf(resultToPost));
        sb.append(". Слабо побить?");
        return sb.toString();
    }

    public void setResultToPost(int resultToPost) {
        this.resultToPost = resultToPost;
    }

}

