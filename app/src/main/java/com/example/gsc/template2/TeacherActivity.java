package com.example.gsc.template2;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import com.anupcowkur.reservoir.Reservoir;
import com.backendless.Backendless;
import com.backendless.BackendlessUser;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;
import com.example.gsc.template2.Back.Async.Savetoken;
import com.example.gsc.template2.Back.GPSTracker;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginManager;
import com.squareup.okhttp.Cache;
import com.squareup.okhttp.Interceptor;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Response;
import com.squareup.picasso.OkHttpDownloader;
import com.squareup.picasso.Picasso;

import java.io.IOException;

import cn.pedant.SweetAlert.SweetAlertDialog;
import layout.FragmentDisucssions;
import layout.Mainstudent;
import layout.MyteacherProfile;
import layout.Profile;
import layout.RequestTeacher;
import layout.TeacherRequestTab;
import layout.UpdateProfileTeacher;
import layout.map;
import layout.profileTeacher;


import static com.example.gsc.template2.LoginActivity.params;


public class TeacherActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    ImageView img ;
    GPSTracker gps ;
 public   FloatingActionButton fab;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher);
        String appVersion = "v1";
        Backendless.initApp( this, "BBA71CAF-54D7-F483-FFBB-7A380218D700", "7D635662-27AE-F3F2-FF61-84EC108A1C00", appVersion );
        getFragmentManager().beginTransaction().replace(R.id.content_teacher, new Mainstudent()).setCustomAnimations(android.R.animator.fade_in, android.R.animator.fade_out).commit();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//gps= new GPSTracker(this);
        //   if(gps.canGetLocation()) {
        //     double  latitude = gps.getLatitude();
        //    double   longitude = gps.getLongitude();
        // geoPoint = new GeoPoint(latitude , longitude);

        //  }
        setSupportActionBar(toolbar);
new Savetoken().execute();
       fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                new SweetAlertDialog(TeacherActivity.this, SweetAlertDialog.WARNING_TYPE)
                        .setTitleText("Are you sure?")
                        .setContentText("Logout ? ")
                        .setConfirmText("Yes,close it!")
                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sDialog) {

                                Backendless.UserService.logout( new AsyncCallback<Void>()
                                {
                                    public void handleResponse( Void response )
                                    {
                                        // user has been logged out.
                                    }

                                    public void handleFault( BackendlessFault fault )
                                    {
                                        // something went wrong and logout failed, to get the error code call fault.getCode()
                                    }
                                });

                                FacebookSdk.sdkInitialize(getApplicationContext());


                                LoginManager.getInstance().logOut();

                                Intent intent = new Intent(TeacherActivity.this, LoginActivity.class);
                                startActivity(intent);
                                SharedPreferences.Editor editor = getSharedPreferences(params, MODE_PRIVATE).edit();
                                editor.putString("login", null);
                                editor.putString("password", null);
                                editor.commit();

                                try {
                                    Reservoir.clear();
                                } catch (Exception e) {
                                    //failure
                                }

                            }
                        })
                        .show();


            }
        });


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        /*************************/
        View header=navigationView.getHeaderView(0);
        final ImageView imgvw = (ImageView) header.findViewById(R.id.imageView);

        final BackendlessUser u = Backendless.UserService.CurrentUser();


        OkHttpClient okHttpClient = new OkHttpClient();
        okHttpClient.networkInterceptors().add(new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Response originalResponse = chain.proceed(chain.request());
                return originalResponse.newBuilder().header("Cache-Control", "max-age=" + (60 * 60 * 24 * 365)).build();
            }
        });
        try{


            okHttpClient.setCache(new Cache(this.getCacheDir(), Integer.MAX_VALUE));
            OkHttpDownloader okHttpDownloader = new OkHttpDownloader(okHttpClient);
            Picasso picasso = new Picasso.Builder(this).downloader(okHttpDownloader).build();
            picasso.load(u.getProperty("pic").toString()).error(R.drawable.teacher).into(imgvw);
        }
        catch (Exception e){


        }




    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            int backStackEntryCount = getFragmentManager().getBackStackEntryCount();

            if (backStackEntryCount == 0) {
                new SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE)
                        .setTitleText("Are you sure?")
                        .setContentText("Do you want to close the application")
                        .setConfirmText("Yes,close it!")
                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sDialog) {
                                Intent intent = new Intent(TeacherActivity.this, Splash.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                intent.putExtra("EXIT", true);
                                startActivity(intent);
                                sDialog.dismissWithAnimation();
                            }
                        })
                        .show();
            } else {
                super.onBackPressed();

            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.teacher, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        fab.show();
        int id = item.getItemId();
        map m = new map();
        if (id == R.id.nav_tprofile) {
            getFragmentManager().beginTransaction().setCustomAnimations(R.animator.slide_out,R.animator.slide_in,0,0).replace(R.id.content_teacher,new MyteacherProfile()).addToBackStack(null).commit();

        }
        else if (id == R.id.upprofile) {

            getFragmentManager().beginTransaction().setCustomAnimations(R.animator.slide_out,R.animator.slide_in,0,0).replace(R.id.content_teacher,new UpdateProfileTeacher()).addToBackStack(null).commit();

        } else if (id == R.id.nav_treq) {
            getFragmentManager().beginTransaction().setCustomAnimations(R.animator.slide_out,R.animator.slide_in,0,0).replace(R.id.content_teacher,new TeacherRequestTab()).addToBackStack(null).commit();


        }  else if (id == R.id.nav_share) {
            getFragmentManager().beginTransaction().setCustomAnimations(R.animator.slide_out,R.animator.slide_in,0,0).replace(R.id.content_teacher,new FragmentDisucssions()).addToBackStack(null).commit();



        } else if (id == R.id.nav_send) {

            getFragmentManager().beginTransaction().replace(R.id.content_teacher,new profileTeacher()).setCustomAnimations(R.animator.slide_out,R.animator.slide_in,0,0).addToBackStack(null).commit();


        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
