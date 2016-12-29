package com.example.gsc.template2;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import com.anupcowkur.reservoir.Reservoir;
import com.backendless.Backendless;
import com.backendless.BackendlessUser;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;

import com.example.gsc.template2.Back.Async.Savetoken;

import com.example.gsc.template2.Back.Utils.FontChangeCrawler;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginManager;
import com.squareup.okhttp.Cache;
import com.squareup.okhttp.Interceptor;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Response;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.OkHttpDownloader;
import com.squareup.picasso.Picasso;
import com.wooplr.spotlight.SpotlightView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import cn.pedant.SweetAlert.SweetAlertDialog;
import layout.BlankFragment;
import layout.Find;
import layout.FragmentDisucssions;
import layout.Mainstudent;
import layout.Profile;
import layout.StudentRequestList;
import layout.TabrRequest;
import layout.Teacher;
import layout.UpdateProfileStudent;
import layout.map;
import uk.co.deanwild.materialshowcaseview.IShowcaseListener;
import uk.co.deanwild.materialshowcaseview.MaterialShowcaseView;

import static com.example.gsc.template2.LoginActivity.params;


public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    public static ArrayList<View> items ;
    ImageView img;
    public FloatingActionButton fab;
    public static Menu menu;
     public static Toolbar toolbar ;

   public static NavigationView nav ;

   public static DrawerLayout drawer ;



    @Override


    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getFragmentManager().beginTransaction().replace(R.id.content_main, new Mainstudent()).setCustomAnimations(android.R.animator.fade_in, android.R.animator.fade_out).commit();

items= new ArrayList<>();

      /* SliderLayout mSlider = (SliderLayout)findViewById(R.id.myslider);

        HashMap<String,Integer> file_maps = new HashMap<String, Integer>();
        file_maps.put("Hannibal",R.drawable.selection);
        file_maps.put("Big Bang Theory",R.drawable.learn);
        for(String name : file_maps.keySet()){
            TextSliderView textSliderView = new TextSliderView(this);
            // initialize a SliderLayout
            textSliderView
                    .description(name)
                    .image(file_maps.get(name))
                    .setScaleType(BaseSliderView.ScaleType.Fit)
                    .setOnSliderClickListener(
                            new BaseSliderView.OnSliderClickListener() {

                                @Override
                                public void onSliderClick(BaseSliderView slider) {

                                    Target target = new ViewTarget(R.id.nav_send, MainActivity.this);
                                    new ShowcaseView.Builder(MainActivity.this)
                                            .setTarget(target)
                                            .setContentTitle("Settings menu")
                                            .setContentText("Tap here to view and set the app settings")
                                            .hideOnTouchOutside()
                                            .build();


                                }
                            }
                    );

            //add your extra information
            textSliderView.bundle(new Bundle());
            textSliderView.getBundle()
                    .putString("extra",name);

            mSlider.addSlider(textSliderView);
        }
       // Button b =(Button) findViewById(R.id.mybutton) ;
       */


        String appVersion = "v1";
        Backendless.initApp(this, "BBA71CAF-54D7-F483-FFBB-7A380218D700", "7D635662-27AE-F3F2-FF61-84EC108A1C00", appVersion);


        new Savetoken().execute();


       toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                new SweetAlertDialog(MainActivity.this, SweetAlertDialog.WARNING_TYPE)
                        .setTitleText("Are you sure?")
                        .setContentText("Logout ? ")
                        .setConfirmText("Yes,close it!")
                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sDialog) {

                                Backendless.UserService.logout(new AsyncCallback<Void>() {
                                    public void handleResponse(Void response) {
                                        // user has been logged out.
                                    }

                                    public void handleFault(BackendlessFault fault) {
                                        // something went wrong and logout failed, to get the error code call fault.getCode()
                                    }
                                });

                                FacebookSdk.sdkInitialize(getApplicationContext());


                                LoginManager.getInstance().logOut();


                                try {
                                    Reservoir.clear();
                                } catch (Exception e) {
                                    //failure
                                }

                                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                                startActivity(intent);
                                SharedPreferences.Editor editor = getSharedPreferences(params, MODE_PRIVATE).edit();
                                editor.putString("login", null);
                                editor.putString("password", null);
                                editor.commit();


                            }
                        })
                        .show();


            }
        });


      drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);

        drawer.setDrawerListener(toggle);
        toggle.syncState();


        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        nav=navigationView;
        navigationView.setNavigationItemSelectedListener(this);
        View header = navigationView.getHeaderView(0);
        final ImageView imgvw = (ImageView) header.findViewById(R.id.imageView);

        BackendlessUser u=Backendless.UserService.CurrentUser();

      //  final BackendlessUser u = Backendless.UserService.CurrentUser();


        OkHttpClient okHttpClient = new OkHttpClient();
        okHttpClient.networkInterceptors().add(new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Response originalResponse = chain.proceed(chain.request());

                return originalResponse.newBuilder().header("Cache-Control", "public,max-age=" + (60 * 60 * 24 * 365)).build();
            }
        });
        try {


            okHttpClient.setCache(new Cache(this.getCacheDir(), Integer.MAX_VALUE));
            OkHttpDownloader okHttpDownloader = new OkHttpDownloader(okHttpClient);
            final Picasso picasso = new Picasso.Builder(this).downloader(okHttpDownloader).build();
            final BackendlessUser finalU = u;
            picasso.load(u.getProperty("pic").toString()).error(R.drawable.student).into(imgvw);
        } catch (Exception e) {


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
                                Intent intent = new Intent(MainActivity.this, Splash.class);
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
        getMenuInflater().inflate(R.menu.main, menu);


        this.menu=menu;
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

            new MaterialShowcaseView.Builder(this)
                    .setTarget(fab)
                    .setDismissText("GOT IT")

                    .setListener(new IShowcaseListener() {
                        @Override
                        public void onShowcaseDisplayed(MaterialShowcaseView materialShowcaseView) {

                        }

                        @Override
                        public void onShowcaseDismissed(MaterialShowcaseView materialShowcaseView) {
                            drawer.openDrawer(GravityCompat.START);
                            new SpotlightView.Builder(MainActivity.this)
                                    .introAnimationDuration(400)
                                    .performClick(true)


                                    .fadeinTextDuration(400)
                                    .headingTvColor(Color.parseColor("#eb273f"))
                                    .headingTvSize(32)
                                    .headingTvText("Love")
                                    .subHeadingTvColor(Color.parseColor("#ffffff"))
                                    .subHeadingTvSize(16)
                                    .subHeadingTvText("Use the navigation drawer to browse?\nthe app ")
                                    .maskColor(Color.parseColor("#dc000000"))
                                    .target(MainActivity.nav)
                                    .lineAnimDuration(400)
                                    .lineAndArcColor(Color.parseColor("#eb273f"))
                                    .dismissOnTouch(true)
                                    .dismissOnBackPress(true)
                                    .show();


                        }
                    })
                    .setContentText("This is some amazing feature you should know about")
                    .setDelay(500) // optional but starting animations immediately in onCreate can make them choppy
                   // provide a unique ID used to ensure it is only shown once
                    .show();








        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        map m = new map();

   displayView(id);


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    public void displayView(int id) {
        String title="" ;
        fab.show();
        if (id == R.id.find) {
            title="Find teacher" ;
            getFragmentManager().beginTransaction().setCustomAnimations(R.animator.slide_out,R.animator.slide_in).replace(R.id.content_main, new Find()). addToBackStack(null).commit();
        } else if (id == R.id.profile) {
            title="MyProfile" ;
            getFragmentManager().beginTransaction().setCustomAnimations(R.animator.slide_out,R.animator.slide_in).replace(R.id.content_main, new Profile()).addToBackStack(null).commit();
        } else if (id == R.id.map) {
            title="Teacher map" ;
            Intent intent = new Intent(this, MapActivity.class);
            startActivity(intent);

        } else if (id == R.id.upprofile) {
            title="Update profile" ;

            getFragmentManager().beginTransaction().setCustomAnimations(R.animator.slide_out,R.animator.slide_in).replace(R.id.content_main, new UpdateProfileStudent()).setCustomAnimations(android.R.animator.fade_in, android.R.animator.fade_out).addToBackStack(null).commit();
        } else if (id == R.id.myteachers) {
            title="My Teachers" ;

            getFragmentManager().beginTransaction().setCustomAnimations(R.animator.slide_out,R.animator.slide_in).replace(R.id.content_main, new Teacher()).setCustomAnimations(android.R.animator.fade_in, android.R.animator.fade_out).addToBackStack(null).commit();

        } else if (id == R.id.nav_share) {
            title="Requstes" ;
            getFragmentManager().beginTransaction().setCustomAnimations(R.animator.slide_out,R.animator.slide_in).replace(R.id.content_main, new TabrRequest()).setCustomAnimations(android.R.animator.fade_in, android.R.animator.fade_out).addToBackStack(null).commit();


        } else if (id == R.id.nav_send) {
            title="Discussions" ;
            getFragmentManager().beginTransaction().setCustomAnimations(R.animator.slide_out,R.animator.slide_in).replace(R.id.content_main, new FragmentDisucssions()).addToBackStack(null).setCustomAnimations(android.R.animator.fade_in, android.R.animator.fade_out).commit();


        }
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(title);
        }
        items.add(findViewById(R.id.nav_send));
        items.add(findViewById(R.id.nav_share));




    }
    public static void  open() {


    }
}
