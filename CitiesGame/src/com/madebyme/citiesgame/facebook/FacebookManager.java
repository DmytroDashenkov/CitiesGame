package com.madebyme.citiesgame.facebook;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.widget.Toast;
import com.facebook.*;
import org.json.JSONException;
import org.json.JSONObject;


public class FacebookManager {

    private Context context;
    private int resultToPost;

    public void setResultToPost(int resultToPost) {
        this.resultToPost = resultToPost;
    }

    private Session.StatusCallback callback = new Session.StatusCallback() {

        @Override
        public void call(Session session, SessionState state,
                         Exception exception) {
            postHighScoreOnFacebook();

        }
    };

    public FacebookManager(Context context) {
        this.context = context;
    }


    public void loginToFb(Context context, Fragment fragment) {
        Session session = Session.getActiveSession();
        if (!session.isOpened() && !session.isClosed()) {
            session.openForRead(new Session.OpenRequest(fragment).setCallback(callback));
        } else {
            Session.openActiveSession(context, fragment, true, callback);
        }
        Log.e("name", session.getState().name());
    }

    public void postHighScoreOnFacebook(){
        Session session = Session.getActiveSession();
        Log.i("session", session.getState().name());
        Bundle postParams = new Bundle();
        postParams.putString("message", String.valueOf(resultToPost));
        Request.Callback callback = new Request.Callback() {
            @Override
            public void onCompleted(Response response) {
                if(response.getGraphObject() != null){
                    JSONObject graphResponse = response.getGraphObject().getInnerJSONObject();
                    String postId = null;
                    try {
                        postId = graphResponse.getString("id");
                    } catch (JSONException e) {
                        Log.i("error", "JSON error " + e.getMessage());
                    }
                    FacebookRequestError error = response.getError();
                    if (error != null) {
                        Toast.makeText(context, error.getErrorMessage(), Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(context, postId, Toast.LENGTH_LONG).show();
                    }
                }
            }
        };

        Request request = new Request(session, "me/feed", postParams,
                HttpMethod.POST, callback);

        RequestAsyncTask task = new RequestAsyncTask(request);
        task.execute();
    }


    public Session.StatusCallback getCallback() {
        return callback;
    }

}
