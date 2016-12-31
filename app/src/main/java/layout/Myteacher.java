package layout;

/**
 * Created by GSC on 30/12/2016.
 */

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.Fragment;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.anupcowkur.reservoir.Reservoir;
import com.anupcowkur.reservoir.ReservoirPutCallback;
import com.backendless.Backendless;
import com.backendless.BackendlessCollection;
import com.backendless.BackendlessUser;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;
import com.backendless.persistence.BackendlessDataQuery;
import com.example.gsc.template2.Back.Async.SendNotification;
import com.example.gsc.template2.Back.Data.Request;
import com.example.gsc.template2.Back.GPSTracker;
import com.example.gsc.template2.Back.Utils.FontChangeCrawler;
import com.example.gsc.template2.Back.push.AlarmReceiver;
import com.example.gsc.template2.LoginActivity;
import com.example.gsc.template2.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.squareup.okhttp.Cache;
import com.squareup.okhttp.Interceptor;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Response;
import com.squareup.picasso.OkHttpDownloader;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.Random;

import cn.pedant.SweetAlert.SweetAlertDialog;

import static android.content.Context.ALARM_SERVICE;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.backendless.Backendless;
import com.backendless.BackendlessCollection;
import com.backendless.BackendlessUser;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;
import com.backendless.persistence.BackendlessDataQuery;
import com.example.gsc.template2.Back.Async.SendNotification;
import com.example.gsc.template2.Back.Data.Request;
import com.example.gsc.template2.Back.GPSTracker;
import com.example.gsc.template2.Back.User;
import com.example.gsc.template2.Back.Utils.FontChangeCrawler;
import com.example.gsc.template2.Back.push.AlarmReceiver;
import com.example.gsc.template2.LoginActivity;
import com.example.gsc.template2.R;
import com.example.gsc.template2.UsersAdapter;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.squareup.okhttp.Cache;
import com.squareup.okhttp.Interceptor;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Response;
import com.squareup.picasso.OkHttpDownloader;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.Random;

import cn.pedant.SweetAlert.SweetAlertDialog;

import static android.content.Context.ALARM_SERVICE;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link profileTeacher.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link profileTeacher#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Myteacher extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    double lat=0 , lon=0 ;

    BackendlessUser u ;

    private OnFragmentInteractionListener mListener;
    String date  ;
    public Myteacher() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment profileTeacher.
     */
    // TODO: Rename and change types and number of parameters
    public static profileTeacher newInstance(String param1, String param2) {
        profileTeacher fragment = new profileTeacher();
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
        final View v =
                inflater.inflate(R.layout.fragment_profile_teacher, container, false);



        SharedPreferences prefs = getActivity().getSharedPreferences("prefs", getActivity().getApplicationContext().MODE_PRIVATE);
        final String e =   prefs.getString("email",null);
        String whereClause = "email = '"+e + "'";

        try {
            u= Reservoir.get(e, BackendlessUser.class);
        } catch (Exception ex) {

            //failure
        }



        final ImageView imgvw = (ImageView) v.findViewById(R.id.imageView);
        imgvw.bringToFront();
        BackendlessDataQuery dataQuery = new BackendlessDataQuery();
        dataQuery.setWhereClause( whereClause );


        Backendless.Persistence.of( BackendlessUser.class).find(dataQuery,  new AsyncCallback<BackendlessCollection<BackendlessUser>>() {
            @Override
            public void handleResponse(BackendlessCollection<BackendlessUser> foundContacts)

            {

                Iterator<BackendlessUser> iterator = foundContacts.getCurrentPage().iterator();
                while (iterator.hasNext()) {
                    u = iterator.next();


                    TextView profile = (TextView) getView().findViewById(R.id.user_profile_name);
                    TextView emailt = (TextView) getView().findViewById(R.id.input_email);
                    TextView spec = (TextView) getView().findViewById(R.id.spec);
                    TextView price = (TextView) getView().findViewById(R.id.price);
                    TextView number = (TextView) getView().findViewById(R.id.phone);
                    ImageView imgvw = (ImageView) getView().findViewById(R.id.imageView);
                    TextView created = (TextView) getView().findViewById(R.id.datejoined);
                    TextView lastlog = (TextView) getView().findViewById(R.id.lastlogin);
                    imgvw.bringToFront();


                    if (u != null) {


                        Reservoir.putAsync(e, u, new ReservoirPutCallback() {
                            @Override
                            public void onSuccess() {
                                //success
                            }

                            @Override
                            public void onFailure(Exception e) {
                                //error
                            }
                        });

                        try {
                            String last = (String) u.getProperty("lastLogin").toString();
                            lastlog.setText(lastlog.getText()+last);

                        } catch (Exception e ){
                            lastlog.setText(lastlog.getText()+"never");

                        }

                        final RatingBar tratingBar = (RatingBar) getView().findViewById(R.id.TeacherRating);

                        Drawable progress = tratingBar.getProgressDrawable();
                        DrawableCompat.setTint(progress, Color.WHITE);
                        try {
                            final float rating = Float.parseFloat(u.getProperty("rating").toString())/Float.parseFloat(u.getProperty("nrating").toString())  ;
                            Log.e("fllloat", String.valueOf(rating));
                            tratingBar.setRating(rating);
                        }
                        catch ( Exception e ) {
                            tratingBar.setRating(2);

                        }


                        ImageView feed = (ImageView) getView().findViewById(R.id.feed);
                        feed.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {

                                getFragmentManager().beginTransaction().replace(R.id.content_main, new Feedback()).addToBackStack(null).commit();

                            }
                        });


                        profile.setText(u.getProperty("name").toString());
                        price.setText(price.getText() + u.getProperty("price").toString());
                        emailt.setText(u.getEmail().toString());
                        spec.setText(spec.getText() + u.getProperty("speciality").toString());
                        number.setText(number.getText() + u.getProperty("Tel").toString());
                        created.setText(created.getText() + u.getProperty("created").toString());

                        //   lastlog.setText(lastlog.getText()+u.getProperty("lastLogin").toString());


                        OkHttpClient okHttpClient = new OkHttpClient();
                        okHttpClient.networkInterceptors().add(new Interceptor() {
                            @Override
                            public Response intercept(Chain chain) throws IOException {
                                Response originalResponse = chain.proceed(chain.request());
                                return originalResponse.newBuilder().header("Cache-Control", "max-age=" + (60 * 60 * 24 * 365)).build();
                            }
                        });


                        try {


                            okHttpClient.setCache(new Cache(getActivity().getCacheDir(), Integer.MAX_VALUE));
                            OkHttpDownloader okHttpDownloader = new OkHttpDownloader(okHttpClient);
                            Picasso picasso = new Picasso.Builder(getActivity()).downloader(okHttpDownloader).build();
                            picasso.load(u.getProperty("pic").toString()).into(imgvw);
                        } catch (Exception e) {


                        }

                        //add click event on request

                        ImageView add;
                        add = (ImageView) v.findViewById(R.id.add_teacher);
                       add.setVisibility(View.INVISIBLE);




                        ImageView chat;
                        chat = (ImageView) getView().findViewById(R.id.chat);
                        chat.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                getFragmentManager().beginTransaction().replace(R.id.content_main, new ChatFragment()).addToBackStack(null).commit();


                            }
                        });


                        final ImageView rate;
                        rate = (ImageView) getView().findViewById(R.id.rateteacher);
                        BackendlessUser cu = Backendless.UserService.CurrentUser()  ;
                        if ( cu.getProperty("rated")==null) {
                            cu.setProperty("rated","");
                            Backendless.UserService.setCurrentUser(cu );

                        }

//test if already rated  ;
                        if (cu.getProperty("rated").
                                toString().
                                toLowerCase().
                                contains(u.getEmail().toLowerCase())) {
                            Log.e("rattttt","e ");

                            rate.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    new SweetAlertDialog(getActivity(),SweetAlertDialog.NORMAL_TYPE)
                                            .setTitleText("alert !")
                                            .setContentText("You have already rated this teacher  ")

                                            .show();

                                }
                            });



                        }
                        else {
                            rate.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {

                                    final Dialog rankDialog = new Dialog(getActivity());
                                    rankDialog.setTitle(" rate " + u.getProperty("name").toString());
                                    rankDialog.setContentView(R.layout.ratingbar);
                                    rankDialog.setCancelable(true);
                                    final RatingBar ratingBar = (RatingBar) rankDialog.findViewById(R.id.dialog_ratingbar);
                                    ratingBar.setRating(2);


                                    Button updateButton = (Button) rankDialog.findViewById(R.id.rank_dialog_button);
                                    updateButton.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            try {
                                                u.setProperty("nrating", Float.parseFloat(u.getProperty("nrating").toString() )+1);
                                                u.setProperty("rating", Float.parseFloat(u.getProperty("rating").toString()  )+ratingBar.getRating());
                                                tratingBar.setRating((Float.parseFloat(u.getProperty("rating").toString() )+ratingBar.getRating())/(Float.parseFloat(u.getProperty("nrating").toString()  )+1));
                                                rate.setOnClickListener(null);


                                            } catch (Exception e) {
                                                u.setProperty("nrating", 1);
                                                u.setProperty("rating", ratingBar.getRating());
                                                tratingBar.setRating(ratingBar.getRating());
                                                rate.setOnClickListener(null);



                                            } finally {
                                                rate.setOnClickListener(null);
                                                rate.setVisibility(View.INVISIBLE);


                                                Backendless.UserService.update(u, new AsyncCallback<BackendlessUser>() {
                                                    @Override
                                                    public void handleResponse(BackendlessUser response) {

                                                        final Snackbar bar = Snackbar.make(getView(), "Rating submitted", Snackbar.LENGTH_LONG)
                                                                .setAction("Dismiss", new View.OnClickListener() {
                                                                    @Override
                                                                    public void onClick(View v) {


                                                                    }
                                                                });
                                                        bar.show();

                                                    }

                                                    @Override
                                                    public void handleFault(BackendlessFault fault) {
                                                        final Snackbar bar = Snackbar.make(getView(), "Something went wrong ", Snackbar.LENGTH_LONG)
                                                                .setAction("Dismiss", new View.OnClickListener() {
                                                                    @Override
                                                                    public void onClick(View v) {


                                                                    }
                                                                });
                                                        bar.show();

                                                    }
                                                });


                                                final BackendlessUser current = Backendless.UserService.CurrentUser();
                                                try {

                                                    current.setProperty("rated", current.getProperty("rated").toString() + u.getEmail());
                                                    Backendless.UserService.setCurrentUser(current);
                                                } catch (Exception e) {

                                                    current.setProperty("rated", u.getEmail());
                                                    Backendless.UserService.setCurrentUser(current);


                                                } finally {




                                                    Backendless.UserService.update(current, new AsyncCallback<BackendlessUser>() {
                                                        @Override
                                                        public void handleResponse(BackendlessUser response) {

                                                            SharedPreferences prefs1 = getActivity().getSharedPreferences(LoginActivity.params, getActivity().MODE_PRIVATE);
                                                            String log = prefs1.getString("login", null);
                                                            String pa = prefs1.getString("password", null);
                                                            Backendless.UserService.login( log, pa, new AsyncCallback<BackendlessUser>()
                                                            {


                                                                public void handleResponse( BackendlessUser user )
                                                                {



                                                                }// user has been logged in


                                                                public void handleFault( BackendlessFault fault )
                                                                {

                                                                }
                                                            });

                                                        }

                                                        @Override
                                                        public void handleFault(BackendlessFault fault) {


                                                        }
                                                    });


                                                }

                                                rankDialog.dismiss();


                                            }


                                        }


                                    });
                                    rankDialog.show();
                                }
                            });
                        }


                    }


                    //rating


                    // click event

                }
            }

            @Override
            public void handleFault(BackendlessFault fault) {

                if (u != null) {


                    Reservoir.putAsync(e, u, new ReservoirPutCallback() {
                        @Override
                        public void onSuccess() {
                            //success
                        }

                        @Override
                        public void onFailure(Exception e) {
                            //error
                        }
                    });

                    TextView profile = (TextView) getView().findViewById(R.id.user_profile_name);
                    TextView emailt = (TextView) getView().findViewById(R.id.input_email);
                    TextView spec = (TextView) getView().findViewById(R.id.spec);
                    TextView price = (TextView) getView().findViewById(R.id.price);
                    TextView number = (TextView) getView().findViewById(R.id.phone);
                    ImageView imgvw = (ImageView) getView().findViewById(R.id.imageView);
                    TextView created = (TextView) getView().findViewById(R.id.datejoined);
                    TextView lastlog = (TextView) getView().findViewById(R.id.lastlogin);
                    imgvw.bringToFront();

                    try {
                        String last = (String) u.getProperty("lastLogin").toString();
                        lastlog.setText(lastlog.getText()+last);

                    } catch (Exception e ){
                        lastlog.setText(lastlog.getText()+"never");

                    }

                    final RatingBar tratingBar = (RatingBar) getView().findViewById(R.id.TeacherRating);

                    Drawable progress = tratingBar.getProgressDrawable();
                    DrawableCompat.setTint(progress, Color.WHITE);
                    try {
                        final float rating = Float.parseFloat(u.getProperty("rating").toString())/Float.parseFloat(u.getProperty("nrating").toString())  ;
                        Log.e("fllloat", String.valueOf(rating));
                        tratingBar.setRating(rating);
                    }
                    catch ( Exception e ) {
                        tratingBar.setRating(2);

                    }


                    ImageView feed = (ImageView) getView().findViewById(R.id.feed);
                    feed.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                         //   getFragmentManager().beginTransaction().replace(R.id.content_main, new Feedback()).addToBackStack(null).commit();

                        }
                    });




                    profile.setText(u.getProperty("name").toString());
                    price.setText(price.getText() + u.getProperty("price").toString());
                    emailt.setText(u.getEmail().toString());
                    spec.setText(spec.getText() + u.getProperty("speciality").toString());
                    number.setText(number.getText() + u.getProperty("Tel").toString());
                    created.setText(created.getText() + u.getProperty("created").toString());
                    //   lastlog.setText(lastlog.getText()+u.getProperty("lastLogin").toString());


                    OkHttpClient okHttpClient = new OkHttpClient();
                    okHttpClient.networkInterceptors().add(new Interceptor() {
                        @Override
                        public Response intercept(Chain chain) throws IOException {
                            Response originalResponse = chain.proceed(chain.request());
                            return originalResponse.newBuilder().header("Cache-Control", "max-age=" + (60 * 60 * 24 * 365)).build();
                        }
                    });


                    try {


                        okHttpClient.setCache(new Cache(getActivity().getCacheDir(), Integer.MAX_VALUE));
                        OkHttpDownloader okHttpDownloader = new OkHttpDownloader(okHttpClient);
                        Picasso picasso = new Picasso.Builder(getActivity()).downloader(okHttpDownloader).build();
                        picasso.load(u.getProperty("pic").toString()).error(R.drawable.teacher).into(imgvw);
                    } catch (Exception e) {


                    }

                    //add click event on request

                    ImageView add;
                    add = (ImageView) v.findViewById(R.id.add_teacher);
                    add.setVisibility(View.GONE);




                    ImageView chat;
                    chat = (ImageView) v.findViewById(R.id.chat);
                    chat.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                         //   getFragmentManager().beginTransaction().replace(R.id.content_main, new ChatFragment()).addToBackStack(null).commit();


                        }
                    });


                    final ImageView rate;
                    rate = (ImageView) getView().findViewById(R.id.rateteacher);
                    BackendlessUser cu = Backendless.UserService.CurrentUser()  ;
                    if ( cu.getProperty("rated")==null) {
                        cu.setProperty("rated","");
                        Backendless.UserService.setCurrentUser(cu );

                    }

//test if already rated  ;
                    if (cu.getProperty("rated").
                            toString().
                            toLowerCase().
                            contains(u.getEmail().toLowerCase())) {
                        Log.e("rattttt","e ");

                        rate.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                new SweetAlertDialog(getActivity(),SweetAlertDialog.NORMAL_TYPE)
                                        .setTitleText("alert !")
                                        .setContentText("You have already rated this teacher  ")

                                        .show();

                            }
                        });



                    }
                    else {
                        rate.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {

                                final Dialog rankDialog = new Dialog(getActivity());
                                rankDialog.setTitle(" rate " + u.getProperty("name").toString());
                                rankDialog.setContentView(R.layout.ratingbar);
                                rankDialog.setCancelable(true);
                                final RatingBar ratingBar = (RatingBar) rankDialog.findViewById(R.id.dialog_ratingbar);
                                ratingBar.setRating(2);


                                Button updateButton = (Button) rankDialog.findViewById(R.id.rank_dialog_button);
                                updateButton.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        try {
                                            u.setProperty("nrating", Float.parseFloat(u.getProperty("nrating").toString() )+1);
                                            u.setProperty("rating", Float.parseFloat(u.getProperty("rating").toString()  )+ratingBar.getRating());
                                            tratingBar.setRating((Float.parseFloat(u.getProperty("rating").toString() )+ratingBar.getRating())/(Float.parseFloat(u.getProperty("nrating").toString()  )+1));
                                            rate.setOnClickListener(null);


                                        } catch (Exception e) {
                                            u.setProperty("nrating", 1);
                                            u.setProperty("rating", ratingBar.getRating());
                                            tratingBar.setRating(ratingBar.getRating());
                                            rate.setOnClickListener(null);



                                        } finally {
                                            rate.setOnClickListener(null);
                                            rate.setVisibility(View.INVISIBLE);


                                            Backendless.UserService.update(u, new AsyncCallback<BackendlessUser>() {
                                                @Override
                                                public void handleResponse(BackendlessUser response) {

                                                    final Snackbar bar = Snackbar.make(getView(), "Rating submitted", Snackbar.LENGTH_LONG)
                                                            .setAction("Dismiss", new View.OnClickListener() {
                                                                @Override
                                                                public void onClick(View v) {


                                                                }
                                                            });
                                                    bar.show();

                                                }

                                                @Override
                                                public void handleFault(BackendlessFault fault) {
                                                    final Snackbar bar = Snackbar.make(getView(), "Something went wrong ", Snackbar.LENGTH_LONG)
                                                            .setAction("Dismiss", new View.OnClickListener() {
                                                                @Override
                                                                public void onClick(View v) {


                                                                }
                                                            });
                                                    bar.show();

                                                }
                                            });


                                            final BackendlessUser current = Backendless.UserService.CurrentUser();
                                            try {

                                                current.setProperty("rated", current.getProperty("rated").toString() + u.getEmail());
                                                Backendless.UserService.setCurrentUser(current);
                                            } catch (Exception e) {

                                                current.setProperty("rated", u.getEmail());
                                                Backendless.UserService.setCurrentUser(current);


                                            } finally {




                                                Backendless.UserService.update(current, new AsyncCallback<BackendlessUser>() {
                                                    @Override
                                                    public void handleResponse(BackendlessUser response) {

                                                        SharedPreferences prefs1 = getActivity().getSharedPreferences(LoginActivity.params, getActivity().MODE_PRIVATE);
                                                        String log = prefs1.getString("login", null);
                                                        String pa = prefs1.getString("password", null);
                                                        Backendless.UserService.login( log, pa, new AsyncCallback<BackendlessUser>()
                                                        {


                                                            public void handleResponse( BackendlessUser user )
                                                            {



                                                            }// user has been logged in


                                                            public void handleFault( BackendlessFault fault )
                                                            {

                                                            }
                                                        });

                                                    }

                                                    @Override
                                                    public void handleFault(BackendlessFault fault) {


                                                    }
                                                });


                                            }

                                            rankDialog.dismiss();


                                        }


                                    }


                                });
                                rankDialog.show();
                            }
                        });
                    }


                }

            }

        });







        FontChangeCrawler fontChanger = new FontChangeCrawler(getActivity().getAssets(), "fonts/myfont.ttf");
        fontChanger.replaceFonts((ViewGroup) v);

        return v;

    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();

    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
