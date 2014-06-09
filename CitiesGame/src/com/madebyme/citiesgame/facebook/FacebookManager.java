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
    private Fragment fragment;
    private Session.StatusCallback callback = new Session.StatusCallback() {
        @Override
        public void call(Session session, SessionState state, Exception exception) {

        }
    };

    public FacebookManager(Context context, Fragment fragment) {
        this.context = context;
        this.fragment = fragment;
    }

    public void loginToFb(Fragment fragment) {
        Session session = Session.getActiveSession();
        if (session == null) {
            session = Session.openActiveSession(context, fragment, true, callback);
            session.requestNewPublishPermissions(new Session.NewPermissionsRequest(fragment, "public_profile", "publish_actions"));
            session.openForPublish(new Session.OpenRequest(fragment));
            Log.i("session", session.getState().name());
        } else {
            session.openForPublish(new Session.OpenRequest(fragment));
        }
    }

    public void postHighScoreOnFacebook(int result, final Context context){
        Session session = Session.getActiveSession();

        if (session != null) {
            Bundle postParams = new Bundle();
            postParams.putString("message", String.valueOf(result));
            Request.Callback callback = new Request.Callback() {
                @Override
                public void onCompleted(Response response) {
                    JSONObject graphResponse = response.getGraphObject().getInnerJSONObject();
                    String postId = null;
                    try {
                        postId = graphResponse.getString("id");
                    } catch (JSONException e) {
                        Log.i("err", "JSON error " + e.getMessage());
                    }
                    FacebookRequestError error = response.getError();
                    if (error != null) {
                        Toast.makeText(context, error.getErrorMessage(), Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(context, postId, Toast.LENGTH_LONG).show();
                    }
                }
            };

            Request request = new Request(session, "me/feed", postParams,
                    HttpMethod.POST, callback);

            RequestAsyncTask task = new RequestAsyncTask(request);
            task.execute();
        }
    }

}
