package com.example.gsc.template2;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.backendless.Backendless;
import com.backendless.BackendlessUser;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;
import com.dd.processbutton.iml.ActionProcessButton;
import com.example.gsc.template2.Back.push.MyFirebaseInstanceIDService;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    EditText editTextLogin,editTextPassword;
    Button btnConnect;
     LoginButton loginButton;
    TextView link_signup,link_signup2;
    MyApp app;
    Context context;
    public static String params="params";
    //TODO Set Preferences fileName
    private CallbackManager callbackManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        setContentView(R.layout.activity_login);
        if (getIntent().getBooleanExtra("EXIT", false)) {
            finish();
            return;
        }
        String appVersion = "v1";
        Backendless.initApp( this, "BBA71CAF-54D7-F483-FFBB-7A380218D700", "7D635662-27AE-F3F2-FF61-84EC108A1C00", appVersion );


 //check if  already logged in

        SharedPreferences prefs1 = getSharedPreferences(params, MODE_PRIVATE);
        String log = prefs1.getString("login", null);
        String pa = prefs1.getString("password", null);

        if (log!=null){


            Backendless.UserService.login( log, pa, new AsyncCallback<BackendlessUser>()
            {


                public void handleResponse( BackendlessUser user )
                {

                    BackendlessUser u = Backendless.UserService.CurrentUser();




                    if (u!=null) {
                        if (u.getProperty("ts").equals("t")) {

                            startActivity(new Intent(LoginActivity.this, TeacherActivity.class));


                        } else {

                            startActivity(new Intent(LoginActivity.this, MainActivity.class));

                        }

                    }


                }

                public void handleFault( BackendlessFault fault )
                {

                    Toast.makeText(getApplicationContext(), fault.getMessage(), Toast.LENGTH_SHORT ).show();
                }
            });


        }










        editTextLogin = (EditText) findViewById(R.id.et_login);
        editTextPassword = (EditText) findViewById(R.id.et_password);
        btnConnect = (Button) findViewById(R.id.btn_connect);
        loginButton = (LoginButton) findViewById(R.id.login_button);
        List<String> permissionNeeds =
                Arrays.asList("public_profile", "email", "user_friends");
        loginButton.setReadPermissions(permissionNeeds);


        link_signup = (TextView) findViewById(R.id.link_signup);
        link_signup2 = (TextView) findViewById(R.id.link_signup2);

 //facebook


        callbackManager = CallbackManager.Factory.create();



      loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {



            @Override
            public void onSuccess(final LoginResult loginResult) {





                GraphRequest request = GraphRequest.newMeRequest(
                        loginResult.getAccessToken(),

                        new GraphRequest.GraphJSONObjectCallback() {
                            @Override
                            public void onCompleted(final JSONObject object, GraphResponse response) {
                                Log.v("LoginActivity", response.toString());

                                try {

                                    // Application code

                                    //backendless user to facebbok

                                    //upl
                                    final String email = object.getString("email");
                                    String birthday = object.getString("birthday");


                                    SharedPreferences.Editor editor = getSharedPreferences(params, MODE_PRIVATE).edit();
                                    editor.putString("login", email);
                                    editor.putString("password",loginResult.getAccessToken().getUserId() );
                                    editor.commit();
                                    Backendless.UserService.login( email,  loginResult.getAccessToken().getUserId(), new AsyncCallback<BackendlessUser>()
                                    {
                                        public void handleResponse( BackendlessUser user )
                                        {


                                            BackendlessUser u = Backendless.UserService.CurrentUser();
                                            Log.e("bbbbbbbb","success"+u.getEmail());



                                            if (u!=null) {
                                                if (u.getProperty("ts").equals("t")) {
                                                    startActivity(new Intent(LoginActivity.this, TeacherActivity.class));


                                                } else {

                                                    startActivity(new Intent(LoginActivity.this, MainActivity.class));

                                                }

                                            }


                                            // user has been logged in
                                        }

                                        public void handleFault( BackendlessFault fault )
                                        {





                                            BackendlessUser user = new BackendlessUser();

                                            user.setProperty("pic","https://graph.facebook.com/"+ loginResult.getAccessToken().getUserId()+"/picture?type=large");
                                            user.setProperty("password", loginResult.getAccessToken().getUserId());
                                            user.setProperty("email", email);
                                            user.setProperty("ts", "s");

                                            try {
                                                user.setProperty("name", object.getString("name"));
                                            } catch (JSONException e) {
                                                e.printStackTrace();
                                            }


                                            Backendless.UserService.register( user, new AsyncCallback<BackendlessUser>()
                                            {
                                                public void handleResponse( BackendlessUser registeredUser )
                                                {

                                                    Backendless.UserService.login( email,  loginResult.getAccessToken().getUserId(), new AsyncCallback<BackendlessUser>()
                                                    {
                                                        public void handleResponse( BackendlessUser user )
                                                        {

                                                            Log.e("setting user","success");
                                                            Intent intent = new Intent(LoginActivity.this, MainActivity.class);


                                                            startActivity(intent);
                                                        }

                                                        public void handleFault( BackendlessFault fault )
                                                        {

                                                            Toast.makeText(getApplicationContext(), fault.getMessage(), Toast.LENGTH_SHORT ).show();
                                                        }
                                                    });



                                                }

                                                public void handleFault( BackendlessFault fault )
                                                {

                                                    Log.e("gggggg",fault.getMessage());
                                                }
                                            } );



                                        }
                                    });





                                        //create account






                                }
                                catch (Exception E){
 Log.v("oooooooooooo","aaaaaaaa");
                                }
                            }
                        });
                Bundle parameters = new Bundle();
                parameters.putString("fields", "id,name,email,gender,birthday");
                request.setParameters(parameters);
                request.executeAsync();



            }

            @Override
            public void onCancel() {


            }

            @Override
            public void onError(FacebookException e) {
                Log.e("facebooooooooook",e.getMessage());
            }
        });
        //TODO Get App Variable

        //TODO Get Preferences

        String login = "";
        String password = "";
        //shared preferences get
        SharedPreferences prefs = getSharedPreferences(params, MODE_PRIVATE);
        String restoredText = prefs.getString("login", null);
        if (restoredText != null) {
            //	String name = prefs.getString("name", "No name defined");//"No name defined" is the default value.
            editTextLogin.setText(prefs.getString("login","abd"));
            editTextPassword.setText(prefs.getString("password","abd1"));
            //	int idName = prefs.getstring("idName", 0); //0 is thedefault value.
        }
        else {


            editTextLogin.setText(login);
            editTextPassword.setText(password);
        }

        btnConnect.setOnClickListener(this);
        link_signup.setOnClickListener(this);
        link_signup2.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_connect:


                String login = editTextLogin.getText().toString();
                String password = editTextPassword.getText().toString();
                if (!login.equals("") && !password.equals("")) {

                    ((AppName) this.getApplication()).getMyStateManager();

                    ((AppName) this.getApplication()).setMyStateManager(true);





                    final ActionProcessButton btnSignIn = (ActionProcessButton) findViewById(R.id.btn_connect);
                    btnSignIn.setMode(ActionProcessButton.Mode.PROGRESS);

                    btnSignIn.setProgress(5);
                    btnSignIn.setProgress(20);
                    btnSignIn.setProgress(25);
//shared preeferences save
                    SharedPreferences.Editor editor = getSharedPreferences(params, MODE_PRIVATE).edit();
                    editor.putString("login", editTextLogin.getText().toString());
                    editor.putString("password", editTextPassword.getText().toString());
                    editor.commit();
                    btnSignIn.setProgress(35);


                    Backendless.UserService.login( editTextLogin.getText().toString(), editTextPassword.getText().toString(), new AsyncCallback<BackendlessUser>()
                    {


                        public void handleResponse( BackendlessUser user )
                        {


                            btnSignIn.setProgress(50);
                            btnSignIn.setProgress(60);
                            BackendlessUser u = Backendless.UserService.CurrentUser();
                            btnSignIn.setProgress(70);
                            if (u!=null) {
                                if (u.getProperty("ts").equals("t")) {
                                    btnSignIn.setProgress(80);
                                    btnSignIn.setProgress(100);
                                    startActivity(new Intent(LoginActivity.this, TeacherActivity.class));


                                } else {

                                    startActivity(new Intent(LoginActivity.this, MainActivity.class));

                                }

                            }// user has been logged in
                        }

                        public void handleFault( BackendlessFault fault )
                        {

                            Toast.makeText(getApplicationContext(), fault.getMessage(), Toast.LENGTH_SHORT ).show();
                        }
                    });




                   // User user = new User(login, password);
                  //  authenticate(user);


                    //Intent inte = new Intent(this,MainActivity.class);
                  //  startActivity(inte);



                }
                else {
                    Toast.makeText(getApplicationContext(), "please, tap login and password ", Toast.LENGTH_SHORT ).show();
                }

                break;
 case R.id.link_signup:
                //TODO Set connect state
                //   startActivity(new Intent(ActivityLogin.this,MainActivity.class));

                Intent inter = new Intent(this,SignupActivity.class);
                startActivity(inter);

                ((AppName) this.getApplication()).setMyStateManager(false);
                break;
            case R.id.link_signup2:
                //TODO Set connect state
                //   startActivity(new Intent(ActivityLogin.this,MainActivity.class));

                Intent inter2 = new Intent(this,SignupActivityTeacher.class);
                startActivity(inter2);

                ((AppName) this.getApplication()).setMyStateManager(false);
                break;


        }

    }


    private void showErrorMessage() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(LoginActivity.this);
        dialogBuilder.setMessage("Wrong User Detail");
        dialogBuilder.setPositiveButton("OK", null);
        dialogBuilder.show();
    }
   /* private void logUserIn(User returnedUser) {

        startActivity(new Intent(this, MainActivity.class));
    }*/

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onBackPressed() {

            new SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE)
                    .setTitleText("Are you sure?")
                    .setContentText("Do you want to close the application")
                    .setConfirmText("Yes,close it!")
                    .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sDialog) {
                          finish();
                        }
                    })
                    .show();

        }

}
