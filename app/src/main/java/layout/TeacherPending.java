package layout;

import android.app.AlarmManager;
import android.app.Dialog;
import android.app.PendingIntent;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.provider.CalendarContract;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.anupcowkur.reservoir.Reservoir;
import com.backendless.Backendless;
import com.backendless.BackendlessCollection;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;
import com.backendless.persistence.BackendlessDataQuery;
import com.example.gsc.template2.AppName;
import com.example.gsc.template2.Back.Adapter.RequestTeacherAdapter;
import com.example.gsc.template2.Back.Async.SendNotification;
import com.example.gsc.template2.Back.Data.Request;
import com.example.gsc.template2.Back.GPSTracker;
import com.example.gsc.template2.Back.push.AlarmReceiver;
import com.example.gsc.template2.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.reflect.TypeToken;
import com.squareup.okhttp.Cache;
import com.squareup.okhttp.Interceptor;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Response;
import com.squareup.picasso.OkHttpDownloader;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.lang.reflect.Type;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.TimeZone;

import cn.pedant.SweetAlert.SweetAlertDialog;

import static android.content.Context.ALARM_SERVICE;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * to handle interaction events.
 * Use the {@link StudentRequestList#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TeacherPending extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private Paint p = new Paint();

    ArrayList<Request> lusers;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;



    public TeacherPending() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment StudentRequestList.
     */
    // TODO: Rename and change types and number of parameters
    public static StudentRequestList newInstance(String param1, String param2) {
        StudentRequestList fragment = new StudentRequestList();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        lusers = new ArrayList<Request>();
        String appVersion = "v1";
        // Backendless.initApp( getActivity(), "BBA71CAF-54D7-F483-FFBB-7A380218D700", "7D635662-27AE-F3F2-FF61-84EC108A1C00", appVersion );
        final View view = inflater.inflate(R.layout.fragment_student_request_list, container, false);
        final String s = ((AppName) getActivity().getApplication()).getSpec();
        Double d = ((AppName) getActivity().getApplication()).getPrice();
        String whereClause = "receiveremail ='" + Backendless.UserService.CurrentUser().getEmail() + "' and approved=0";
        Log.e("whereeee", whereClause);

        Type resultType = new TypeToken<List<Request>>() {}.getType();
        try {
            lusers= Reservoir.get("teacherequestpending", resultType);

        } catch (Exception e) {
            Log.e("reservoireee",e.toString());

        }
        BackendlessDataQuery dataQuery = new BackendlessDataQuery();
        dataQuery.setWhereClause(whereClause);
        final MaterialDialog pDialog = new MaterialDialog.Builder(getActivity())
                .title("Getting data")
                .content("it wont take long")
                .progress(true, 0)
                .progressIndeterminateStyle(true)
                .show();


        Backendless.Persistence.of(Request.class).find(dataQuery, new AsyncCallback<BackendlessCollection<Request>>() {
            @Override

            public void handleResponse(BackendlessCollection<Request> foundContacts)

            {

                lusers=new ArrayList<Request>();

                   foundContacts.setPageSize(500000);
                Iterator<Request> iterator = foundContacts.getCurrentPage().iterator();
                while (iterator.hasNext()) {
                    final Request next = iterator.next();


                    lusers.add(next);
                    Log.e("whereeee", String.valueOf(lusers.size()));


                    //  Toast.makeText(getApplicationContext(), "Your  fdfdfddfd Location is - \nLat: " + ((GeoPoint)restaurant.getProperty( "location" )) + "\nLong: " + restaurant.getProperty("location"), Toast.LENGTH_LONG).show();


                }
                pDialog.dismiss();



                try {
                    Reservoir.put("teacherequestpending", lusers);
                } catch (Exception e) {
                    //failure;
                    Log.e("reservoireee",e.getMessage());
                }


                final RecyclerView rv = (RecyclerView) view.findViewById(R.id.requestlist);

                RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
                rv.setLayoutManager(mLayoutManager);
                //  rv.setLayoutManager(llm);
                rv.setHasFixedSize(true);

                RequestTeacherAdapter adapter = new RequestTeacherAdapter( lusers, new RequestTeacherAdapter.OnItemClickListener() {


                    @Override
                    public void onItemClick(final Request item) {

                        final Dialog d = new Dialog(getActivity());
                        d.setContentView(R.layout.requestdetails);
                        d.setTitle("Request details");
                        d.show();
                        TextView profile = (TextView)  d.findViewById(R.id.user_profile_name);
                        TextView  em = (TextView)  d.findViewById(R.id.email);
                        TextView type = (TextView)  d.findViewById(R.id.type);
                        TextView sent = (TextView)  d.findViewById(R.id.Sent);
                        TextView Phone = (TextView)  d.findViewById(R.id.phone);
                        TextView date = (TextView)  d.findViewById(R.id.Date);
                        ImageView imgvw = (ImageView) d.findViewById(R.id.imageView);
                        ImageView directions = (ImageView) d.findViewById(R.id.Direction);
                        ImageView refuse = (ImageView) d.findViewById(R.id.refuse);


                        refuse.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                {

                                    new SweetAlertDialog(getActivity(), SweetAlertDialog.WARNING_TYPE)
                                            .setTitleText("Are you sure?")
                                            .setContentText("Refuse Reques!")
                                            .setConfirmText("Yes!")
                                            .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                                @Override
                                                public void onClick(final SweetAlertDialog sDialog) {


                                                    item.setApproved(2);
                                                    Backendless.Persistence.save(item, new AsyncCallback<Request>() {

                                                        @Override
                                                        public void handleResponse(Request response) {
                                                            sDialog.dismissWithAnimation();
                                                            new SendNotification(item.getSender().getProperty("mtoken").toString(), Uri.encode(s)).execute();
                                                            final String s = item.getReceiver().getProperty("name").toString() + " refused your request your request";
                                                            Log.e("Tooooken", item.getSender().getProperty("mtoken").toString());


                                                            getFragmentManager().beginTransaction().replace(R.id.content_teacher,new TeacherRequestTab()).setCustomAnimations(android.R.animator.fade_in,android.R.animator.fade_out).commit();




                                                            new SendNotification(item.getSender().getProperty("mtoken").toString(), Uri.encode(s)).execute();

                                                            sDialog.dismiss();
                                                            d.dismiss();


                                                            // Contact instance has been updated
                                                        }

                                                        @Override
                                                        public void handleFault(BackendlessFault fault) {

                                                            Log.e("dateeeee ghalet", fault.getMessage());
                                                            sDialog
                                                                    .setTitleText("Errot!")
                                                                    .setContentText("Somethingwent wrong!")
                                                                    .setConfirmText("OK")
                                                                    .setConfirmClickListener(null)
                                                                    .changeAlertType(SweetAlertDialog.ERROR_TYPE);
                                                            // an error has occurred, the error code can be retrieved with fault.getCode()
                                                        }
                                                    });


                                                }
                                            })
                                            .show();

                                }
                            }
                        });

                        SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss");

                        String formatted =    format.format(Long.parseLong(item.getRtime().toString()));
                        profile.setText(item.getSender().getProperty("name").toString());
                        date.setText(date.getText()+""+item.getRdate()+" "+formatted);
                        em.setText(item.getSenderemail());
                        if(item.getType()==1){
                            type.setText(type.getText()+" "+"Student Home");
                            directions.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    final Dialog d2 = new Dialog(getActivity());
                                    d2.setTitle(" Select Location ");
                                    d2.setContentView(R.layout.selectlocaion);
                                    d2.show();


                                    MapView mMapView = (MapView) d2.findViewById(R.id.mapView);
                                    MapsInitializer.initialize(getActivity());

                                    mMapView = (MapView) d2.findViewById(R.id.mapView);
                                    mMapView.onCreate(d2.onSaveInstanceState());
                                    mMapView.onResume();// needed to get the map to display immediately


                                    mMapView.getMapAsync(new OnMapReadyCallback() {
                                        @Override
                                        public void onMapReady(final GoogleMap googleMap) {

                                            final MarkerOptions m = new MarkerOptions();
                                            m.position(new LatLng(item.getLat(), item.getLon()));

                                            m.title(item.getSender().getProperty("name").toString());
                                            m.draggable(true);

                                            final Marker marker = googleMap.addMarker(m);

                                            googleMap.moveCamera( CameraUpdateFactory.newLatLngZoom(new LatLng(item.getLat(),item.getLon()) , 14.0f) );




                                            googleMap.setMyLocationEnabled(true);
                                            GPSTracker gps = new GPSTracker(getActivity());

                                            if (gps.canGetLocation()) {



                                            } else {

                                                gps.showSettingsAlert();

                                            }

                                        }
                                    });



                                    }
                            });



                        }
                        else{
                            type.setText(type.getText()+" "+"Teacher Home");
                            directions.setVisibility( View.INVISIBLE);
                        }
                        sent.setText(sent.getText()+" "+item.getCreated());
                        try{
                            Phone.setText(Phone.getText()+""+item.getSender().getProperty("Tel").toString());
                        }
                        catch (Exception e ) {
                            Phone.setText(Phone.getText()+""+"not available");

                        }

                        OkHttpClient okHttpClient = new OkHttpClient();
                        okHttpClient.networkInterceptors().add(new Interceptor() {
                            @Override
                            public Response intercept(Chain chain) throws IOException {
                                Response originalResponse = chain.proceed(chain.request());
                                return originalResponse.newBuilder().header("Cache-Control", "max-age=" + (60 * 60 * 24 * 365)).build();
                            }
                        });
                        try{


                            okHttpClient.setCache(new Cache(getActivity().getCacheDir(), Integer.MAX_VALUE));
                            OkHttpDownloader okHttpDownloader = new OkHttpDownloader(okHttpClient);
                            Picasso picasso = new Picasso.Builder(getActivity()).downloader(okHttpDownloader).build();
                            picasso.load(item.getSender().getProperty("pic").toString()).error(R.drawable.student).into(imgvw);
                        }
                        catch (Exception e){


                        }

                        imgvw.bringToFront();

                        ImageView accept = (ImageView) d.findViewById(R.id.accept);
                        accept.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {

                                new SweetAlertDialog(getActivity(), SweetAlertDialog.SUCCESS_TYPE)
                                        .setTitleText("Are you sure?")
                                        .setContentText("Accept Reques!")
                                        .setConfirmText("Yes!")
                                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                            @Override
                                            public void onClick(final SweetAlertDialog sDialog) {


                                                item.setApproved(1);
                                                Backendless.Persistence.save(item, new AsyncCallback<Request>() {

                                                    @Override
                                                    public void handleResponse(Request response) {
                                                        d.dismiss();


                                                        sDialog.dismissWithAnimation();
                                                        new SendNotification(item.getSender().getProperty("mtoken").toString(), Uri.encode(s)).execute();
                                                        final String s = item.getReceiver().getProperty("name").toString() + " Acepted your request";
                                                        Log.e("Tooooken", item.getSender().getProperty("mtoken").toString());
                                                        Random rn = new Random();
                                                        int answer = rn.nextInt(500 - 1 + 1) + 1;
                                                        AlarmManager alarmManager = (AlarmManager) getActivity().getSystemService(ALARM_SERVICE);
                                                        Intent alarmIntent = new Intent(getActivity(), AlarmReceiver.class);
                                                        PendingIntent pendingIntent = PendingIntent.getBroadcast(getActivity(), answer, alarmIntent, 0);

                                                        SimpleDateFormat dateformat = new SimpleDateFormat("yyyy-MM-dd");
                                                        Date d = new Date();
                                                        try {

                                                            //calendar event

                                                            d = dateformat.parse(item.getRdate());


                                                            ContentResolver cr = getActivity().getContentResolver();
                                                            ContentValues values = new ContentValues();
                                                            values.put(CalendarContract.Events.DTSTART, d.getTime() + Long.parseLong(item.getRtime()));
                                                            values.put(CalendarContract.Events.DTEND, d.getTime() + Long.parseLong(item.getRtime())+2*60*60*1000);
                                                            values.put(CalendarContract.Events.TITLE, "Session with" +item.getSender().getProperty("name"));

                                                            TimeZone timeZone = TimeZone.getDefault();
                                                            values.put(CalendarContract.Events.EVENT_TIMEZONE, timeZone.getID());

                                                            values.put(CalendarContract.Events.CALENDAR_ID, 1);




                                                            values.put(CalendarContract.Events.HAS_ALARM, 1);
                                                            Uri uri = cr.insert(CalendarContract.Events.CONTENT_URI, values);
                                                            long eventId = new Long(uri.getLastPathSegment());

                                                            Uri REMINDERS_URI = Uri.parse(getCalendarUriBase(true) + "reminders");


                                                            values = new ContentValues();
                                                            values.put(CalendarContract.Reminders.EVENT_ID, eventId );

                                                            values.put(CalendarContract.Reminders.MINUTES ,30);
                                                            values.put(CalendarContract.Reminders.METHOD, CalendarContract.Reminders.METHOD_ALERT);
                                                            cr.insert(CalendarContract.Reminders.CONTENT_URI, values);

                                                            values = new ContentValues();
                                                            values.put(CalendarContract.Attendees.EVENT_ID,eventId) ;

                                                            values.put(CalendarContract.Attendees.ATTENDEE_TYPE, CalendarContract.Attendees.TYPE_REQUIRED);
                                                            values.put(CalendarContract.Attendees.ATTENDEE_NAME, item.getSender().getProperty("name").toString());
                                                            values.put(CalendarContract.Attendees.ATTENDEE_EMAIL, item.getSenderemail());

                                                            cr.insert(CalendarContract.Attendees.CONTENT_URI, values);



                                                        } catch (ParseException e1) {
                                                            Log.e("error", "dateeeee ghalet");
                                                        }

                                                        alarmManager.set(AlarmManager.RTC_WAKEUP, d.getTime() + Long.parseLong(item.getRtime()), pendingIntent);


                                                        new SendNotification(item.getSender().getProperty("mtoken").toString(), Uri.encode(s)).execute();

                                                      sDialog.dismiss();

                                                        getFragmentManager().beginTransaction().replace(R.id.content_teacher,new TeacherRequestTab()).setCustomAnimations(android.R.animator.fade_in,android.R.animator.fade_out).commit();

                                                        // Contact instance has been updated
                                                    }

                                                    @Override
                                                    public void handleFault(BackendlessFault fault) {

                                                        Log.e("dateeeee ghalet", fault.getMessage());
                                                        sDialog
                                                                .setTitleText("Errot!")
                                                                .setContentText("Somethingwent wrong!")
                                                                .setConfirmText("OK")
                                                                .setConfirmClickListener(null)
                                                                .changeAlertType(SweetAlertDialog.ERROR_TYPE);
                                                        // an error has occurred, the error code can be retrieved with fault.getCode()
                                                    }
                                                });


                                            }
                                        })
                                        .show();

                            }
                        });
                        /*


*/
                    }

                    @Override
                    public void onItemLongclick(final Request item) {

                    }
                });







                rv.setAdapter(adapter);



                // click event

            }

            @Override
            public void handleFault(BackendlessFault fault) {
                pDialog.dismiss();


                final RecyclerView rv = (RecyclerView) view.findViewById(R.id.requestlist);

                RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
                rv.setLayoutManager(mLayoutManager);
                //  rv.setLayoutManager(llm);
                rv.setHasFixedSize(true);

                RequestTeacherAdapter adapter = new RequestTeacherAdapter( lusers, new RequestTeacherAdapter.OnItemClickListener() {


                    @Override
                    public void onItemClick(final Request item) {

                        final Dialog d = new Dialog(getActivity());
                        d.setContentView(R.layout.requestdetails);
                        d.setTitle("Request details");
                        d.show();
                        TextView profile = (TextView)  d.findViewById(R.id.user_profile_name);
                        TextView  em = (TextView)  d.findViewById(R.id.email);
                        TextView type = (TextView)  d.findViewById(R.id.type);
                        TextView sent = (TextView)  d.findViewById(R.id.Sent);
                        TextView Phone = (TextView)  d.findViewById(R.id.phone);
                        TextView date = (TextView)  d.findViewById(R.id.Date);
                        ImageView imgvw = (ImageView) d.findViewById(R.id.imageView);
                        ImageView directions = (ImageView) d.findViewById(R.id.Direction);
                        ImageView refuse = (ImageView) d.findViewById(R.id.refuse);


                        refuse.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                {

                                    new SweetAlertDialog(getActivity(), SweetAlertDialog.WARNING_TYPE)
                                            .setTitleText("Are you sure?")
                                            .setContentText("Refuse Reques!")
                                            .setConfirmText("Yes!")
                                            .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                                @Override
                                                public void onClick(final SweetAlertDialog sDialog) {


                                                    item.setApproved(2);
                                                    Backendless.Persistence.save(item, new AsyncCallback<Request>() {

                                                        @Override
                                                        public void handleResponse(Request response) {
                                                            sDialog.dismissWithAnimation();
                                                            new SendNotification(item.getSender().getProperty("mtoken").toString(), Uri.encode(s)).execute();
                                                            final String s = item.getReceiver().getProperty("name").toString() + " refused your request your request";
                                                            Log.e("Tooooken", item.getSender().getProperty("mtoken").toString());


                                                            getFragmentManager().beginTransaction().replace(R.id.content_teacher,new TeacherRequestTab()).setCustomAnimations(android.R.animator.fade_in,android.R.animator.fade_out).commit();




                                                            new SendNotification(item.getSender().getProperty("mtoken").toString(), Uri.encode(s)).execute();

                                                            sDialog.dismiss();
                                                            d.dismiss();


                                                            // Contact instance has been updated
                                                        }

                                                        @Override
                                                        public void handleFault(BackendlessFault fault) {

                                                            Log.e("dateeeee ghalet", fault.getMessage());
                                                            sDialog
                                                                    .setTitleText("Errot!")
                                                                    .setContentText("Check your  internet connection!")
                                                                    .setConfirmText("OK")
                                                                    .setConfirmClickListener(null)
                                                                    .changeAlertType(SweetAlertDialog.ERROR_TYPE);
                                                            // an error has occurred, the error code can be retrieved with fault.getCode()
                                                        }
                                                    });


                                                }
                                            })
                                            .show();

                                }
                            }
                        });

                        SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss");

                        String formatted =    format.format(Long.parseLong(item.getRtime().toString()));
                        profile.setText(item.getSender().getProperty("name").toString());
                        date.setText(date.getText()+""+item.getRdate()+" "+formatted);
                        em.setText(item.getSenderemail());
                        if(item.getType()==1){
                            type.setText(type.getText()+" "+"Student Home");
                            directions.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    final Dialog d2 = new Dialog(getActivity());
                                    d2.setTitle(" Select Location ");
                                    d2.setContentView(R.layout.selectlocaion);
                                    d2.show();


                                    MapView mMapView = (MapView) d2.findViewById(R.id.mapView);
                                    MapsInitializer.initialize(getActivity());

                                    mMapView = (MapView) d2.findViewById(R.id.mapView);
                                    mMapView.onCreate(d2.onSaveInstanceState());
                                    mMapView.onResume();// needed to get the map to display immediately


                                    mMapView.getMapAsync(new OnMapReadyCallback() {
                                        @Override
                                        public void onMapReady(final GoogleMap googleMap) {

                                            final MarkerOptions m = new MarkerOptions();
                                            m.position(new LatLng(item.getLat(), item.getLon()));
                                            m.title(item.getSender().getProperty("name").toString());
                                            m.draggable(true);

                                            final Marker marker = googleMap.addMarker(m);




                                            googleMap.setMyLocationEnabled(true);
                                            GPSTracker gps = new GPSTracker(getActivity());

                                            if (gps.canGetLocation()) {



                                            } else {

                                                gps.showSettingsAlert();

                                            }

                                        }
                                    });



                                }
                            });



                        }
                        else{
                            type.setText(type.getText()+" "+"Teacher Home");
                            directions.setVisibility( View.INVISIBLE);
                        }
                        sent.setText(sent.getText()+" "+item.getCreated());
                        try{
                            Phone.setText(Phone.getText()+""+item.getSender().getProperty("Tel").toString());
                        }
                        catch (Exception e ) {
                            Phone.setText(Phone.getText()+""+"not available");

                        }

                        OkHttpClient okHttpClient = new OkHttpClient();
                        okHttpClient.networkInterceptors().add(new Interceptor() {
                            @Override
                            public Response intercept(Chain chain) throws IOException {
                                Response originalResponse = chain.proceed(chain.request());
                                return originalResponse.newBuilder().header("Cache-Control", "max-age=" + (60 * 60 * 24 * 365)).build();
                            }
                        });
                        try{


                            okHttpClient.setCache(new Cache(getActivity().getCacheDir(), Integer.MAX_VALUE));
                            OkHttpDownloader okHttpDownloader = new OkHttpDownloader(okHttpClient);
                            Picasso picasso = new Picasso.Builder(getActivity()).downloader(okHttpDownloader).build();
                            picasso.load(item.getSender().getProperty("pic").toString()).error(R.drawable.student).into(imgvw);
                        }
                        catch (Exception e){


                        }

                        imgvw.bringToFront();

                        ImageView accept = (ImageView) d.findViewById(R.id.accept);
                        accept.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {

                                new SweetAlertDialog(getActivity(), SweetAlertDialog.SUCCESS_TYPE)
                                        .setTitleText("Are you sure?")
                                        .setContentText("Accept Request!")
                                        .setConfirmText("Yes!")
                                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                            @Override
                                            public void onClick(final SweetAlertDialog sDialog) {


                                                item.setApproved(1);
                                                Backendless.Persistence.save(item, new AsyncCallback<Request>() {

                                                    @Override
                                                    public void handleResponse(Request response) {
                                                        d.dismiss();
                                                        sDialog.dismissWithAnimation();
                                                        new SendNotification(item.getSender().getProperty("mtoken").toString(), Uri.encode(s)).execute();
                                                        final String s = item.getReceiver().getProperty("name").toString() + " Acepted your request";
                                                        Log.e("Tooooken", item.getSender().getProperty("mtoken").toString());
                                                        Random rn = new Random();
                                                        int answer = rn.nextInt(500 - 1 + 1) + 1;
                                                        AlarmManager alarmManager = (AlarmManager) getActivity().getSystemService(ALARM_SERVICE);
                                                        Intent alarmIntent = new Intent(getActivity(), AlarmReceiver.class);
                                                        PendingIntent pendingIntent = PendingIntent.getBroadcast(getActivity(), answer, alarmIntent, 0);

                                                        SimpleDateFormat dateformat = new SimpleDateFormat("yyyy-MM-dd");
                                                        Date d = new Date();
                                                        try {
                                                            d = dateformat.parse(item.getRdate());
                                                        } catch (ParseException e1) {
                                                            Log.e("error", "dateeeee ghalet");
                                                        }

                                                        alarmManager.set(AlarmManager.RTC_WAKEUP, d.getTime() + Long.parseLong(item.getRtime()), pendingIntent);


                                                        new SendNotification(item.getSender().getProperty("mtoken").toString(), Uri.encode(s)).execute();

                                                        sDialog.dismiss();
                                                        getFragmentManager().beginTransaction().replace(R.id.content_teacher,new TeacherRequestTab()).setCustomAnimations(android.R.animator.fade_in,android.R.animator.fade_out).commit();

                                                        // Contact instance has been updated
                                                    }

                                                    @Override
                                                    public void handleFault(BackendlessFault fault) {


                                                        sDialog
                                                                .setTitleText("Erro!")
                                                                .setContentText("Check your  internet connection!!")
                                                                .setConfirmText("OK")
                                                                .setConfirmClickListener(null)
                                                                .changeAlertType(SweetAlertDialog.ERROR_TYPE);
                                                        // an error has occurred, the error code can be retrieved with fault.getCode()
                                                    }
                                                });


                                            }
                                        })
                                        .show();

                            }
                        });
                        /*


*/
                    }

                    @Override
                    public void onItemLongclick(final Request item) {

                    }
                });







                rv.setAdapter(adapter);

                Log.e("efefefe", fault.getMessage());
            }
        });
        lusers=null;

        //  pDialog.dismiss();
        // Inflate the layout for this fragment
        return view;

        // Inflate the layout for this fragment

    }


    private String getCalendarUriBase(boolean eventUri) {
        Uri calendarURI = null;
        try {
            if (android.os.Build.VERSION.SDK_INT <= 7) {
                calendarURI = (eventUri) ? Uri.parse("content://calendar/") : Uri.parse("content://calendar/calendars");
            } else {
                calendarURI = (eventUri) ? Uri.parse("content://com.android.calendar/") : Uri
                        .parse("content://com.android.calendar/calendars");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return calendarURI.toString();
    }


}
