package layout;

import android.app.Dialog;
import android.graphics.Paint;
import android.os.Bundle;
import android.app.Fragment;
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
import com.example.gsc.template2.Back.Data.Request;
import com.example.gsc.template2.Back.GPSTracker;
import com.example.gsc.template2.Back.Utils.FontChangeCrawler;
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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement thebjl
 * to handle interaction events.
 * Use the {@link StudentRequestList#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RequestTeacher extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private Paint p = new Paint();

    ArrayList<Request> lusers;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;


    public RequestTeacher() {
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
        String whereClause = "receiveremail ='" + Backendless.UserService.CurrentUser().getEmail() + "' and approved=1";
        Log.e("whereeee", whereClause);



        Type resultType = new TypeToken<List<Request>>() {}.getType();
        try {
            lusers= Reservoir.get("teacherrequestaccepted", resultType);
            Log.e("reservoireee", String.valueOf(lusers.size()));
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

                Iterator<Request> iterator = foundContacts.getCurrentPage().iterator();
                while (iterator.hasNext()) {
                    final Request restaurant = iterator.next();


                    lusers.add(restaurant);
                    Log.e("whereeee", String.valueOf(lusers.size()));


                    //  Toast.makeText(getApplicationContext(), "Your  fdfdfddfd Location is - \nLat: " + ((GeoPoint)restaurant.getProperty( "location" )) + "\nLong: " + restaurant.getProperty("location"), Toast.LENGTH_LONG).show();


                }


                pDialog.dismiss();
                try {
                    Reservoir.put("teacherrequestaccepted", lusers);
                } catch (Exception e) {
                    //failure;
                    Log.e("reservoireee",e.getMessage());
                }



                final RecyclerView rv = (RecyclerView) view.findViewById(R.id.requestlist);

                RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
                rv.setLayoutManager(mLayoutManager);

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
                        refuse.setVisibility(View.INVISIBLE);


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
                        Phone.setText(Phone.getText()+""+item.getSender().getProperty("Tel").toString());
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

                      accept.setVisibility(View.INVISIBLE);


                    }

                    @Override
                    public void onItemLongclick(final Request item) {
                        final SweetAlertDialog s = new SweetAlertDialog(getActivity(), SweetAlertDialog.WARNING_TYPE);
                        s.setTitleText("Are you sure?")
                                .setContentText("do you want to delete this request")
                                .setConfirmText("Yes,close it!")
                                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                    @Override
                                    public void onClick(final SweetAlertDialog sDialog) {

                                        Backendless.Persistence.of(Request.class).remove(item,
                                                new AsyncCallback<Long>() {
                                                    public void handleResponse(Long response) {


                                                        sDialog
                                                                .setTitleText("success!")
                                                                .setContentText("Reqest deleted!")
                                                                .setConfirmText("OK")
                                                                .setConfirmClickListener(null)
                                                                .changeAlertType(SweetAlertDialog.SUCCESS_TYPE);
                                                        getFragmentManager().beginTransaction().replace(R.id.content_teacher, new RequestTeacher()).addToBackStack(null).commit();

                                                    }

                                                    public void handleFault(BackendlessFault fault) {
                                                        // dan error has occurred, the error code can be
                                                        // retrieved with fault.getCode()
                                                    }

                                                });


                                    }
                                })
                                .show();
                    }
                });








                rv.setAdapter(adapter);



                // click event

            }

            @Override
            public void handleFault(BackendlessFault fault) {
                Log.e("efefefe", fault.getMessage());

 pDialog.dismiss();

                final RecyclerView rv = (RecyclerView) view.findViewById(R.id.requestlist);

                RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
                rv.setLayoutManager(mLayoutManager);

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
                        refuse.setVisibility(View.INVISIBLE);


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
                        Phone.setText(Phone.getText()+""+item.getSender().getProperty("Tel").toString());
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

                        accept.setVisibility(View.INVISIBLE);


                    }

                    @Override
                    public void onItemLongclick(final Request item) {
                        final SweetAlertDialog s = new SweetAlertDialog(getActivity(), SweetAlertDialog.WARNING_TYPE);
                        s.setTitleText("Are you sure?")
                                .setContentText("do you want to delete this request")
                                .setConfirmText("Yes,close it!")
                                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                    @Override
                                    public void onClick(final SweetAlertDialog sDialog) {

                                        Backendless.Persistence.of(Request.class).remove(item,
                                                new AsyncCallback<Long>() {
                                                    public void handleResponse(Long response) {


                                                        sDialog
                                                                .setTitleText("success!")
                                                                .setContentText("Reqest deleted!")
                                                                .setConfirmText("OK")
                                                                .setConfirmClickListener(null)
                                                                .changeAlertType(SweetAlertDialog.SUCCESS_TYPE);
                                                        getFragmentManager().beginTransaction().replace(R.id.content_teacher, new RequestTeacher()).addToBackStack(null).commit();

                                                    }

                                                    public void handleFault(BackendlessFault fault) {
                                                        // dan error has occurred, the error code can be
                                                        // retrieved with fault.getCode()
                                                    }

                                                });


                                    }
                                })
                                .show();
                    }
                });








                rv.setAdapter(adapter);



            }
        });



        FontChangeCrawler fontChanger = new FontChangeCrawler(getActivity().getAssets(), "fonts/myfont.ttf");
        fontChanger.replaceFonts((ViewGroup) view);
        //  pDialog.dismiss();
        // Inflate the layout for this fragment
        return view;

        // Inflate the layout for this fragment

    }


}
