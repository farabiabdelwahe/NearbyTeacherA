package com.example.gsc.template2;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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

import butterknife.Bind;
import butterknife.ButterKnife;

public class UpdatePro extends AppCompatActivity  implements View.OnClickListener{
    @Bind(R.id.input_name) EditText _nameText;
    //@Bind(R.id.input_address) EditText _addressText;
    @Bind(R.id.input_email) EditText _emailText;
    @Bind(R.id.input_mobile) EditText _mobileText;
 @Bind(R.id.input_password) EditText _passwordText;
   // @Bind(R.id.input_reEnterPassword) EditText _reEnterPasswordText;
    @Bind(R.id.btn_up) Button _upButton;


    @Override
    public void onCreate(Bundle savedInstanceState)  {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_pro);
        ButterKnife.bind(this);
        _upButton = (Button) findViewById(R.id.btn_up);
        _nameText=(EditText) findViewById(R.id.input_name);
        _mobileText=(EditText) findViewById(R.id.input_mobile);
        _emailText=(EditText) findViewById(R.id.input_email);
     _upButton.setOnClickListener(this);

        BackendlessUser u = Backendless.UserService.CurrentUser();

        if (u != null) {
            String email = (String) u.getProperty("email");
            String name = (String) u.getProperty("name");
            String password = (String) u.getProperty("password");
            String Tel = (String) u.getProperty("Tel");


            _nameText.setText(name);
          _passwordText.setText(password);
            // String FirstName = etFirstName.getText().toString();
            //   String LastName = etLastName.getText().toString();
            _emailText.setText(email);
            _mobileText.setText(Tel);
        } else {
            Log.e("not connected", "");

        }





    }

    public void signup() {

    }


    public void onSignupSuccess() {
        _upButton.setEnabled(true);
        setResult(RESULT_OK, null);
        finish();
    }

    public void onSignupFailed() {
        Toast.makeText(getBaseContext(), "Login failed", Toast.LENGTH_LONG).show();

        _upButton.setEnabled(true);
    }

    public boolean validate() {
        boolean valid = true;

        String name = _nameText.getText().toString();
        //  String address = _addressText.getText().toString();
        String email = _emailText.getText().toString();
        String mobile = _mobileText.getText().toString();
        String password = _passwordText.getText().toString();
   // String reEnterPassword = _reEnterPasswordText.getText().toString();

        if (name.isEmpty() || name.length() < 3) {
            _nameText.setError("at least 3 characters");
            valid = false;
        } else {
            _nameText.setError(null);
        }

    /*    if (address.isEmpty()) {
            _addressText.setError("Enter Valid Address");
            valid = false;
        } else {
            _addressText.setError(null);
        }

*/
        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            _emailText.setError("enter a valid email address");
            valid = false;
        } else {
            _emailText.setError(null);
        }

        if (mobile.isEmpty() || mobile.length()!=10) {
            _mobileText.setError("Enter Valid Mobile Number");
            valid = false;
        } else {
            _mobileText.setError(null);
        }

        if (password.isEmpty() || password.length() < 4 || password.length() > 10) {
            _passwordText.setError("between 4 and 10 alphanumeric characters");
            valid = false;
        } else {
            _passwordText.setError(null);
        }

     /*   if (reEnterPassword.isEmpty() || reEnterPassword.length() < 4 || reEnterPassword.length() > 10 || !(reEnterPassword.equals(password))) {
            _reEnterPasswordText.setError("Password Do not match");
            valid = false;
        } else {
            _reEnterPasswordText.setError(null);
        }
*/
        return valid;
    }

    @Override
    public void onClick(View v) {
        BackendlessUser u = Backendless.UserService.CurrentUser();

        System.out.println( "[ASYNC] Updating user object without login." );
        final BackendlessUser user1 = new BackendlessUser();
        user1.setProperty( "objectId", u.getProperty("objectId").toString());
        Log.e("cccccc",u.getProperty("objectId").toString());
        user1.setProperty("Tel", _mobileText.getText().toString());
        user1.setProperty("name", _nameText.getText().toString());
        user1.setProperty("email", _emailText.getText().toString());

        Backendless.UserService.update( user1, new AsyncCallback<BackendlessUser>()
        {
            @Override
            public void handleResponse( BackendlessUser backendlessUser )
            {
                Intent intent = new Intent(UpdatePro.this, MainActivity.class);


                startActivity(intent);
                Log.e("ffffffff","ssssssss");
                Log.e("ffffffff", _nameText.getText().toString());


            }

            @Override
            public void handleFault( BackendlessFault backendlessFault )
            {
                Log.e("ffffffff",backendlessFault.getMessage());
            }
        } );

/*
        BackendlessUser u = Backendless.UserService.CurrentUser();
        Log.e("kkkkkkkkkkkkkkk", "kkkkkkkkkk");
        String username = (String) u.getProperty("email");
        String pass = (String) u.getProperty("passwrod");

        Backendless.UserService.login("raid@raid.raid", "azerty", new AsyncCallback<BackendlessUser>() {


            public void handleResponse(BackendlessUser user) {

                // user has been logged in, now user properties can be updated
                user.setProperty("Tel", _mobileText.getText());
                user.setProperty("name", _nameText.getText());
                user.setProperty("email", _emailText.getText());
                //   user.setProperty("password", _passwordText.getText());

                Backendless.UserService.update(user, new AsyncCallback<BackendlessUser>() {
                    public void handleResponse(BackendlessUser user) {
                        // user has been updated
                        Log.e("mchaaa updata", "");
                    }

                    public void handleFault(BackendlessFault fault) {
                        Log.e("mchaaachhhh updata", "");

                    }
                });
            }

            public void handleFault(BackendlessFault fault) {
                // login failed, to get the error code call fault.getCode()
            }
        });
*/
        if (!validate()) {
            onSignupFailed();
            return;
        }




        String name = _nameText.getText().toString();
        //   String address = _addressText.getText().toString();
        String email = _emailText.getText().toString();
        String mobile = _mobileText.getText().toString();
        String password = _passwordText.getText().toString();
        //    String reEnterPassword = _reEnterPasswordText.getText().toString();

        // TODO: Implement your own signup logic here.



      UpdatePro.this.finish();


    }


    @Override
    public void onResume(){


        super.onResume();
        ButterKnife.bind(this);
        _upButton = (Button) findViewById(R.id.btn_up);
        _nameText=(EditText) findViewById(R.id.input_name);
        _mobileText=(EditText) findViewById(R.id.input_mobile);
        _emailText=(EditText) findViewById(R.id.input_email);
        _upButton.setOnClickListener(this);

        BackendlessUser u = Backendless.UserService.CurrentUser();

        if (u != null) {
            String email = (String) u.getProperty("email");
            String name = (String) u.getProperty("name");
            String password = (String) u.getProperty("password");
            String Tel = (String) u.getProperty("Tel");


            _nameText.setText(name);
            _passwordText.setText(password);
            // String FirstName = etFirstName.getText().toString();
            //   String LastName = etLastName.getText().toString();
            _emailText.setText(email);
            _mobileText.setText(Tel);
        } else {
            Log.e("not connected", "");

        }

    }

    @Override
    public void onRestart() {

super.onRestart();
        ButterKnife.bind(this);
        _upButton = (Button) findViewById(R.id.btn_up);
        _nameText=(EditText) findViewById(R.id.input_name);
        _mobileText=(EditText) findViewById(R.id.input_mobile);
        _emailText=(EditText) findViewById(R.id.input_email);
        _upButton.setOnClickListener(this);

        BackendlessUser u = Backendless.UserService.CurrentUser();

        if (u != null) {
            String email = (String) u.getProperty("email");
            String name = (String) u.getProperty("name");
            String password = (String) u.getProperty("password");
            String Tel = (String) u.getProperty("Tel");


            _nameText.setText(name);
            _passwordText.setText(password);
            // String FirstName = etFirstName.getText().toString();
            //   String LastName = etLastName.getText().toString();
            _emailText.setText(email);
            _mobileText.setText(Tel);
        } else {
            Log.e("not connected", "");

        }




    }


}