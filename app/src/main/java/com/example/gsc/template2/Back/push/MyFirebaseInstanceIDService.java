package com.example.gsc.template2.Back.push;

/**
 * Created by GSC on 20/11/2016.
 */


import android.content.SharedPreferences;
import android.util.Log;
import android.widget.Toast;

import com.backendless.Backendless;
import com.backendless.BackendlessUser;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;
import com.example.gsc.template2.LoginActivity;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

import layout.Profile;


public class MyFirebaseInstanceIDService extends FirebaseInstanceIdService {

    private static final String TAG = "MyFirebaseIIDService";

    /**
     * Called if InstanceID token is updated. This may occur if the security of
     * the previous token had been compromised. Note that this is called when the InstanceID token
     * is initially generated so this is where you would retrieve the token.
     */
    // [START refresh_token]
    @Override
    public void onTokenRefresh() {
        // Get updated InstanceID token.
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        Log.d(TAG, "Refreshed token: " + refreshedToken);

        // If you want to send messages to this application instance or
        // manage this apps subscriptions on the server side, send the
        // Instance ID token to your app server.
        sendRegistrationToServer(refreshedToken);
    }
    // [END refresh_token]

    /**
     * Persist token to third-party servers.
     * <p>
     * Modify this method to associate the user's FCM InstanceID token with any server-side account
     * maintained by your application.
     *
     * @param token The new token.
     */
    private void sendRegistrationToServer(String token) {
        try {
            final BackendlessUser u = Backendless.UserService.CurrentUser();


            u.setProperty("mtoken", token);


            Backendless.UserService.update(u, new AsyncCallback<BackendlessUser>() {
                @Override
                public void handleResponse(BackendlessUser backendlessUser) {

                    Backendless.UserService.setCurrentUser(u);


                }

                @Override
                public void handleFault(BackendlessFault fault) {

                }

            });


        } catch (Exception e) {

        }
    }

}