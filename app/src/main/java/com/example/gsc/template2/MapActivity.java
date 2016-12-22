package com.example.gsc.template2;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Environment;
import android.provider.Settings;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.backendless.Backendless;
import com.backendless.BackendlessCollection;
import com.backendless.BackendlessUser;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;
import com.backendless.geo.GeoPoint;
import com.backendless.persistence.BackendlessDataQuery;
import com.example.gsc.template2.Back.Adapter.MarkerAdapter;
import com.example.gsc.template2.Back.GPSTracker;
import com.example.gsc.template2.Back.User;
import com.example.gsc.template2.Back.Utils.Cache;
import com.example.gsc.template2.Back.Utils.Utils;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;

import com.google.android.gms.location.LocationListener;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.koushikdutta.ion.Ion;
import com.koushikdutta.ion.builder.Builders;
import com.squareup.okhttp.internal.DiskLruCache;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import layout.profileTeacher;

import static android.R.attr.progress;


public class MapActivity extends Activity implements android.location.LocationListener,GoogleMap.OnMarkerClickListener,OnMapReadyCallback {

    GoogleMap googleMap;
    Bitmap bit ;

    ArrayList<Object> lgp = new ArrayList<Object>();
    ArrayList<Marker> arraym= new ArrayList<Marker>();
    private DiskLruCache mDiskLruCache;
    private final Object mDiskCacheLock = new Object();
    private boolean mDiskCacheStarting = true;
    private static final int DISK_CACHE_SIZE = 1024 * 1024 * 80; // 10MB
    private static final String DISK_CACHE_SUBDIR = "thumbnails";
ProgressBar b ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
b = (ProgressBar) findViewById(R.id.google_progress);



        super.onCreate(savedInstanceState);

        //show error dialog if GoolglePlayServices not available
        if (!isGooglePlayServicesAvailable()) {
            finish();
        }
        setContentView(R.layout.activity_map);
        MapFragment supportMapFragment =
                (MapFragment) getFragmentManager().findFragmentById(R.id.mapg);
         supportMapFragment.getMapAsync(this);




    }



    @Override
    public void onLocationChanged(Location location) {

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


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


    }

    @Override
    public boolean onMarkerClick(final Marker marker) {

        String name = marker.getTitle();

        SharedPreferences.Editor editor = getSharedPreferences("prefs", this.MODE_PRIVATE).edit();
        editor.putString("email", marker.getTitle());

        editor.commit();
        getFragmentManager().beginTransaction().replace(R.id.container, new profileTeacher()).addToBackStack(null).commit();

        return true;
    }

    public void startProgress(View view) {
        // do something long
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i <= 10; i++) {
                    final int value = i;
                    Loadmarker();


                }
            }
        };
    }

    public void Loadmarker() {


    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
    this.googleMap=googleMap;
        this.googleMap.setMyLocationEnabled(true);
        this.googleMap.setOnMarkerClickListener(this);


        String whereClause = "ts = 't'";
        BackendlessDataQuery dataQuery = new BackendlessDataQuery();
        dataQuery.setWhereClause( whereClause );




        Backendless.Persistence.of( BackendlessUser.class).find(dataQuery,  new AsyncCallback<BackendlessCollection<BackendlessUser>>(){
            @Override
            public void handleResponse( BackendlessCollection<BackendlessUser> foundContacts )

            {
                double i = 0.001 ;

                Iterator<BackendlessUser> iterator=foundContacts.getCurrentPage().iterator();
                while( iterator.hasNext() )
                {
                    final BackendlessUser restaurant=iterator.next();


                    new CreateMarker(restaurant.getEmail(),restaurant.getProperty("pic").toString(),
                            Double.valueOf(restaurant.getProperty("lat").toString()),Double.valueOf(restaurant.getProperty("long").toString()),i).execute();

                    i+=0.001;
                }


            }
            @Override
            public void handleFault( BackendlessFault fault )
            {
                Log.e("efefefe",fault.getMessage());
            }
        });

        ProgressBar  bc = (ProgressBar) findViewById(R.id.google_progress);

        bc.setVisibility(View.INVISIBLE);


        GPSTracker gps = new GPSTracker(MapActivity.this);

        // check if GPS enabled

        if (gps.canGetLocation()) {
            double latitude = gps.getLatitude();
            double longitude = gps.getLongitude();
            User.changeLocation(latitude, longitude);


            googleMap.addMarker(new MarkerOptions()
                    .position(new LatLng(latitude, longitude))
                    .title("Hello world"));





            // \n is for new line
            Toast.makeText(getApplicationContext(), "Your Location is - \nLat: " + latitude + "\nLong: " + longitude, Toast.LENGTH_LONG).show();
        } else {

            gps.showSettingsAlert();

        }



    }


    private class DownloadWebPageTask extends AsyncTask<Void, Void, ArrayList<MarkerAdapter>> {

        ArrayList<MarkerAdapter> ml = new ArrayList<MarkerAdapter>();

        @Override
        protected ArrayList<MarkerAdapter> doInBackground(Void... voids) {



            String whereClause = "ts = 't'";
            BackendlessDataQuery dataQuery = new BackendlessDataQuery();
            dataQuery.setWhereClause(whereClause);


            BackendlessCollection<BackendlessUser> result = Backendless.Persistence.of(BackendlessUser.class ).find( dataQuery );

                    Iterator<BackendlessUser> iterator = result.getCurrentPage().iterator();
                    double i = 0.001;

                    while (iterator.hasNext()) {
                        MarkerAdapter m = new MarkerAdapter();

                        final BackendlessUser restaurant = iterator.next();
                        Bitmap bitmap = BitmapFactory.decodeResource(getApplicationContext().getResources(),
                                R.drawable.mark);



                        bitmap = Utils.getCroppedBitmap(bitmap);

                        m.bitmap = bitmap;
                        m.email = restaurant.getEmail().toString();

                        Location l = new Location("");
                        l.setLatitude(Double.valueOf(restaurant.getProperty("lat").toString()));
                        l.setLongitude(Double.valueOf(restaurant.getProperty("long").toString()));
                        m.location = l;
                        m.url=restaurant.getProperty("pic").toString();

                        ml.add(m);



                    }








            return ml;


        }

        protected void onPostExecute(ArrayList<MarkerAdapter> l) {
     double i = 0.001;

            for( MarkerAdapter m:l){
                new CreateMarker(m.email,m.url,m.location.getLatitude(),m.location.getLongitude(),0.002);



            }
         ProgressBar   bar = (ProgressBar) findViewById(R.id.google_progress);
            bar.setVisibility
                    (View.INVISIBLE);


        }




    }

  public class CreateMarker extends AsyncTask<Void, Void, MarkerOptions> {

 String email ;
        String pic ;
        double lat;
                double g ;

       public CreateMarker() {}

        public  CreateMarker(String t ,String p , double i , double j , double off){
          email=t;
          pic=p ;
          lat=i+off ;
          g=j+off ;

      }
        @Override
        protected MarkerOptions doInBackground(Void... strings) {




            Bitmap bitmap = BitmapFactory.decodeResource(getApplicationContext().getResources(),
                    R.drawable.mark);


            if(Cache.getInstance().getLru().get(email)==null) {
                Log.e("meche fele cache ", "gggggggg");


                try {



                    bitmap = Ion.with(getApplicationContext())
                            .load(pic).asBitmap().get();
                    Cache.getInstance().getLru().put(email, bitmap);




                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }

                Cache.getInstance().getLru().put(email, bitmap);

            }



            else {

                Log.e(" fele cache ","gggggggg");

                bitmap=(Bitmap)Cache.getInstance().getLru().get(email);


            }


            bitmap = Utils.getCroppedBitmap(bitmap);
            bitmap = Utils.getMarkerBitmapFromView(getApplicationContext(), bitmap);
                Location l = new Location("");
                l.setLatitude(lat);
                l.setLongitude(g);

                MarkerOptions bingTrafficMarker = new MarkerOptions()
                        .position(new LatLng(l.getLatitude(), l.getLongitude()))
                        .title(email)
                        .icon(BitmapDescriptorFactory.fromBitmap(bitmap));

                return bingTrafficMarker;


            }




      @Override
      protected void onPostExecute(MarkerOptions markerOptions) {
        googleMap.addMarker(markerOptions);
      }
  }


    //disk cache










    }








