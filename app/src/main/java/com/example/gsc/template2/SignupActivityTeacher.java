package com.example.gsc.template2;


import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.backendless.files.BackendlessFile;
import com.example.gsc.template2.Back.Utils.Utils;
import com.example.gsc.template2.Back.push.MyFirebaseInstanceIDService;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.backendless.Backendless;
import com.backendless.BackendlessUser;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;
import com.backendless.geo.GeoPoint;
import com.example.gsc.template2.Back.GPSTracker;
import com.mvc.imagepicker.ImagePicker;

import java.io.ByteArrayOutputStream;

import butterknife.Bind;
import butterknife.ButterKnife;

public class SignupActivityTeacher extends AppCompatActivity  implements android.location.LocationListener{
    private static final String TAG = "SignupActivityTeacher";
CheckBox math,ph,eco,lang,inf;
    String spec="speciality ";
    Bitmap bmp ;
    ImageView choose ;
    LocationManager mLocationManager;
    @Bind(R.id.input_name) EditText _nameText;
 //   @Bind(R.id.input_address) EditText _addressText;
    @Bind(R.id.input_email) EditText _emailText;
    @Bind(R.id.input_mobile) EditText _mobileText;
    @Bind(R.id.input_password) EditText _passwordText;
    @Bind(R.id.input_reEnterPassword) EditText _reEnterPasswordText;
    @Bind(R.id.input_price) EditText _priceText;
    @Bind(R.id.btn_signup) Button _signupButton;
    @Bind(R.id.link_login) TextView _loginLink;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup_teacher);
        ButterKnife.bind(this);

        bmp =BitmapFactory.decodeResource(getResources(), R.drawable.teacher);

        choose = ( ImageView)  findViewById(R.id.choose) ;
        choose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ImagePicker.pickImage(SignupActivityTeacher.this, "Select your image:");

            }
        });


        math = (CheckBox)findViewById(R.id.Math);
        ph = (CheckBox)findViewById(R.id.Physique);
        eco = (CheckBox)findViewById(R.id.Economie_et_Gestion);
        inf = (CheckBox)findViewById(R.id.Informatique);
        lang = (CheckBox)findViewById(R.id.Langues);
        _signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signup();
            }
        });

        _loginLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Finish the registration screen and return to the Login activity
                Intent intent = new Intent(getApplicationContext(),LoginActivity.class);
                startActivity(intent);
                finish();

            }
        });
        if (!isGooglePlayServicesAvailable()) {
            finish();
        }
        GPSTracker      gps = new GPSTracker(this);

        // check if GPS enabled

        if(gps.canGetLocation()) {
           Double latitude = gps.getLatitude();
         Double   longitude = gps.getLongitude();

          GeoPoint  geoPoint = new GeoPoint(latitude , longitude);

        }
        else{
            gps.showSettingsAlert();
        }
    }



    public void signup() {



        if (math.isChecked()){spec="math";}
        if (ph.isChecked()){spec="physique";}
        if (inf.isChecked()){spec="informatique";}
        if (eco.isChecked()){spec="economie et gestion";}
        if (lang.isChecked()){spec="langues";}




        String UserName = _nameText.getText().toString();
        final String Password = _passwordText.getText().toString();
        String price = _priceText.getText().toString();
        //   String LastName = etLastName.getText().toString();

        final String Email = _emailText.getText().toString();
        String Tel = _mobileText.getText().toString();
        GPSTracker      gps = new GPSTracker(SignupActivityTeacher.this);
        GeoPoint  geoPoint = new GeoPoint(0 , 0);
        BackendlessUser user = new BackendlessUser();
        if(gps.canGetLocation()) {
         double   latitude = gps.getLatitude();
           double longitude = gps.getLongitude();
            user.setProperty( "lat", latitude );
            user.setProperty("long",longitude);
        }
        else{
            gps.showSettingsAlert();
        }

        if (!validate()) {
            onSignupFailed();
            return;
        }

        else {
            user.setProperty("password", Password);
            user.setProperty("email", Email);
            user.setProperty("price", price);
            user.setProperty("speciality", spec);
            user.setProperty("ts", "t");


            user.setProperty("name", UserName);
            user.setProperty("Tel", Tel);
            final MaterialDialog progressDialog = new MaterialDialog.Builder(this)
                    .title("saving data")
                    .content("it wont take long")
                    .progress(true, 0)
                    .progressIndeterminateStyle(true)
                    .show();


            String s = Utils.getRandomString(20) + ".png";
            bmp = Utils.getResizedBitmap(bmp, 200, 200);

            ByteArrayOutputStream out = new ByteArrayOutputStream();
            bmp.compress(Bitmap.CompressFormat.PNG, 100, out);


            Backendless.Files.Android.upload(bmp,
                    Bitmap.CompressFormat.PNG,
                    100,
                    s,
                    "Profile",
                    new AsyncCallback<BackendlessFile>() {
                        @Override
                        public void handleResponse(final BackendlessFile backendlessFile) {
                            Log.e("sssssss", backendlessFile.getFileURL().toString());
                        }

                        @Override
                        public void handleFault(BackendlessFault backendlessFault) {
                            Log.i("ppppppppp", "File has been uploaded. File URL is - " + backendlessFault.toString());
                        }
                    });


            user.setProperty("pic", "https://api.backendless.com/bba71caf-54d7-f483-ffbb-7a380218d700/v1/files/Profile/" + s);


            Backendless.UserService.register(user, new AsyncCallback<BackendlessUser>() {
                public void handleResponse(BackendlessUser registeredUser) {


                    Backendless.UserService.login(Email, Password, new AsyncCallback<BackendlessUser>() {
                        public void handleResponse(BackendlessUser user)


                        {

                            SharedPreferences.Editor editor = getSharedPreferences(LoginActivity.params, MODE_PRIVATE).edit();
                            editor.putString("login", Email);
                            editor.putString("password", Password);
                            editor.commit();
                            Log.e("setting user", "success");
                            Intent intent = new Intent(SignupActivityTeacher.this, TeacherActivity.class);

                            progressDialog.dismiss();

                            startActivity(intent);
                        }

                        public void handleFault(BackendlessFault fault) {

                            Toast.makeText(getApplicationContext(), fault.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });

                }

                public void handleFault(BackendlessFault fault) {

                    Log.e("gggggg", fault.getMessage());
                }
            });


            Log.d(TAG, "Signup");

            Log.d(TAG, "Signup");



            // TODO: Implement your own signup logic here.


        }
    }


    public void onSignupSuccess() {
        if(math.isChecked())
        {
            Toast.makeText(getBaseContext(), "math checked", Toast.LENGTH_LONG).show();
        }
        if(ph.isChecked())
        {
            Toast.makeText(getBaseContext(), "physique checked", Toast.LENGTH_LONG).show();
        }
        if(inf.isChecked())
        {
            Toast.makeText(getBaseContext(), "informatique checked", Toast.LENGTH_LONG).show();
        }
        if(eco.isChecked())
        {
            Toast.makeText(getBaseContext(), "echonomie checked", Toast.LENGTH_LONG).show();
        }
        if(lang.isChecked())
        {
            Toast.makeText(getBaseContext(), "langues checked", Toast.LENGTH_LONG).show();
        }
        _signupButton.setEnabled(true);
        setResult(RESULT_OK, null);
        finish();
    }

    public void onSignupFailed() {
        Toast.makeText(getBaseContext(), "Login failed", Toast.LENGTH_LONG).show();

        _signupButton.setEnabled(true);
    }

    public boolean validate() {
        boolean valid = true;

        String name = _nameText.getText().toString();
       // String address = _addressText.getText().toString();
        String email = _emailText.getText().toString();
        String mobile = _mobileText.getText().toString();
        String password = _passwordText.getText().toString();
        String reEnterPassword = _reEnterPasswordText.getText().toString();

        if (name.isEmpty() || name.length() < 3) {
            _nameText.setError("at least 3 characters");
            valid = false;
        } else {
            _nameText.setError(null);
        }

    /*   if (address.isEmpty()) {
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

        if (mobile.isEmpty() || mobile.length()<8) {
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

        if (reEnterPassword.isEmpty() || reEnterPassword.length() < 4 || reEnterPassword.length() > 10 || !(reEnterPassword.equals(password))) {
            _reEnterPasswordText.setError("Password Do not match");
            valid = false;
        } else {
            _reEnterPasswordText.setError(null);
        }

        return valid;
    }

    @Override
    public void onLocationChanged(Location location) {
        if (location != null) {
            Log.v("Location Changed", location.getLatitude() + " and " + location.getLongitude());
            mLocationManager.removeUpdates(this);
        }
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }


    private boolean isGooglePlayServicesAvailable() {
        int status = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
        if (ConnectionResult.SUCCESS == status) {
            return true;
        } else {
            GooglePlayServicesUtil.getErrorDialog(status, this, 0).show();
            return false;
        }
    }


    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Bitmap bitmap = ImagePicker.getImageFromResult(this, requestCode, resultCode, data);
        bmp=bitmap ;
        choose.setImageBitmap(bitmap);
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        System.gc();
    }

}