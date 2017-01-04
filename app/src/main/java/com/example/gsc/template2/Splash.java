package com.example.gsc.template2;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.Toast;

import com.anupcowkur.reservoir.Reservoir;
import com.anupcowkur.reservoir.ReservoirPutCallback;
import com.backendless.Backendless;
import com.backendless.BackendlessUser;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;
import com.example.gsc.template2.Back.Async.SendNotification;
import com.example.gsc.template2.Back.Data.Comment;
import com.example.gsc.template2.Back.Data.Message;
import com.example.gsc.template2.Back.Data.Request;
import com.example.gsc.template2.Back.push.MyFirebaseInstanceIDService;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessaging;
import com.skyfishjy.library.RippleBackground;

import layout.ChatFragment;

import static com.example.gsc.template2.LoginActivity.params;

public class Splash extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

//hello from the otherside   jds

        super.onCreate(savedInstanceState);



        if (getIntent().getBooleanExtra("EXIT", false)) {
            finish();
            //finish
//raid 

            return;
        }

        setContentView(R.layout.activity_splash);
        String appVersion = "v1";
        Backendless.initApp( this, "BBA71CAF-54D7-F483-FFBB-7A380218D700", "7D635662-27AE-F3F2-FF61-84EC108A1C00", appVersion );

        Backendless.Data.mapTableToClass("Request",Request.class);
        Backendless.Data.mapTableToClass("Message",Message.class);
        Backendless.Data.mapTableToClass("Comments",Comment.class);






            final RippleBackground rippleBackground=(RippleBackground)findViewById(R.id.content);
        ImageView imageView=(ImageView)findViewById(R.id.centerImage);
        rippleBackground.startRippleAnimation();
        new PrefetchData().execute();


    }



    @Override
    public void onPause()
    {
        super.onPause();

        unbindDrawables(findViewById(R.id.activity_splash));
        System.gc();
    }


    @Override
    public void onDestroy()
    {
        super.onDestroy();

        unbindDrawables(findViewById(R.id.activity_splash));
        System.gc();
    }

    private void unbindDrawables(View view)
    {
        if (view.getBackground() != null)
        {
            view.getBackground().setCallback(null);
        }
        if (view instanceof ViewGroup && !(view instanceof AdapterView))
        {
            for (int i = 0; i < ((ViewGroup) view).getChildCount(); i++)
            {
                unbindDrawables(((ViewGroup) view).getChildAt(i));
            }
            ((ViewGroup) view).removeAllViews();
        }
    }


    private class PrefetchData extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // before making http calls

        }

        @Override
        protected Void doInBackground(Void... arg0) {
      return null ;
        }

        @Override
        protected void onPostExecute(Void result) {

            BackendlessUser connected=null;

            try {
                connected= Reservoir.get("connecteduser", BackendlessUser.class);
            } catch (Exception e) {

                //failure
            }
            if ( connected==null) {


                SharedPreferences prefs1 = getSharedPreferences(params, MODE_PRIVATE);
                final String log = prefs1.getString("login", null);
                final String pa = prefs1.getString("password", null);


                if (log != null) {


                    Backendless.UserService.login(log, pa, new AsyncCallback<BackendlessUser>() {


                        public void handleResponse(BackendlessUser user) {


                            BackendlessUser u = Backendless.UserService.CurrentUser();

                            Reservoir.putAsync("connecteduser", u, new ReservoirPutCallback() {
                                @Override
                                public void onSuccess() {
                                    //success
                                }

                                @Override
                                public void onFailure(Exception e) {
                                    //error
                                }
                            });

                            if (u != null) {
                                if (u.getProperty("ts").equals("t")) {

                                    startActivity(new Intent(Splash.this, TeacherActivity.class));


                                } else {

                                    startActivity(new Intent(Splash.this, MainActivity.class));

                                }

                            }// user has been logged in
                        }

                        public void handleFault(BackendlessFault fault) {
                            Log.e("hhhhhhhhhhhhhhh", fault.getMessage().toString());

                            startActivity(new Intent(Splash.this, LoginActivity.class));
                        }
                    });


                } else {
                    Runnable r = new Runnable() {
                        @Override
                        public void run() {
                            startActivity(new Intent(Splash.this, LoginActivity.class));
                        }
                    };

                    Handler h = new Handler();
                    h.postDelayed(r, 2000);
                }

            }
            else{
            Backendless.UserService.setCurrentUser(connected);
                if (connected.getProperty("ts").equals("t")) {

                Runnable r = new Runnable() {
                    @Override
                    public void run() {
                        startActivity(new Intent(Splash.this, TeacherActivity.class));
                    }
                };

                Handler h = new Handler();
                h.postDelayed(r, 2000);
            }
                else {

                    Runnable r = new Runnable() {
                        @Override
                        public void run() {
                            startActivity(new Intent(Splash.this, MainActivity.class));
                        }
                    };

                    Handler h = new Handler();
                    h.postDelayed(r, 2000);
                }

                }

            }

        }


    }




