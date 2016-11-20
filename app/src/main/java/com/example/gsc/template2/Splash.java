package com.example.gsc.template2;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;

import com.backendless.Backendless;
import com.backendless.BackendlessUser;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;
import com.example.gsc.template2.Back.Data.Request;
import com.skyfishjy.library.RippleBackground;

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

        final RippleBackground rippleBackground=(RippleBackground)findViewById(R.id.content);
        ImageView imageView=(ImageView)findViewById(R.id.centerImage);
        rippleBackground.startRippleAnimation();
        new PrefetchData().execute();


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


            SharedPreferences prefs1 = getSharedPreferences(params, MODE_PRIVATE);
            final String log = prefs1.getString("login", null);
            final String pa = prefs1.getString("password", null);



            if (log!=null){



                Backendless.UserService.login( log, pa, new AsyncCallback<BackendlessUser>()
                {



                    public void handleResponse( BackendlessUser user )
                    {

                        BackendlessUser u = Backendless.UserService.CurrentUser();

                        if (u!=null) {
                            if (u.getProperty("ts").equals("t")) {

                                startActivity(new Intent(Splash.this, TeacherActivity.class));


                            } else {

                                startActivity(new Intent(Splash.this, MainActivity.class));

                            }

                        }// user has been logged in
                    }

                    public void handleFault( BackendlessFault fault )
                    {
                        Log.e("hhhhhhhhhhhhhhh",fault.getMessage().toString());

                        startActivity(new Intent(Splash.this,LoginActivity.class));
                    }
                });


            }
            else{
                Runnable r = new Runnable() {
                    @Override
                    public void run(){
                        startActivity(new Intent(Splash.this,LoginActivity.class));
                    }
                };

                Handler h = new Handler();
                h.postDelayed(r, 2000);
            }

        }

    }
}
