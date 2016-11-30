package layout;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TimePicker;

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
import com.example.gsc.template2.Back.push.AlarmReceiver;
import com.example.gsc.template2.R;
import com.example.gsc.template2.UsersAdapter;
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

import static android.content.Context.ALARM_SERVICE;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link profileTeacher.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link profileTeacher#newInstance} factory method to
 * create an instance of this fragment.
 */
public class profileTeacher extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;
String date  ;
    public profileTeacher() {
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
        View v =
                inflater.inflate(R.layout.fragment_profile_teacher, container, false);



        SharedPreferences prefs = getActivity().getSharedPreferences("prefs", getActivity().getApplicationContext().MODE_PRIVATE);
      String e =   prefs.getString("email",null);
        String whereClause = "email = '"+e + "'";

        ImageView imgvw = (ImageView) v.findViewById(R.id.imageView);
        imgvw.bringToFront();
        BackendlessDataQuery dataQuery = new BackendlessDataQuery();
        dataQuery.setWhereClause( whereClause );


        Backendless.Persistence.of( BackendlessUser.class).find(dataQuery,  new AsyncCallback<BackendlessCollection<BackendlessUser>>() {
            @Override
            public void handleResponse(BackendlessCollection<BackendlessUser> foundContacts)

            {

                Iterator<BackendlessUser> iterator = foundContacts.getCurrentPage().iterator();
                while (iterator.hasNext()) {
                    final BackendlessUser u = iterator.next();




                    TextView profile = (TextView)getView().findViewById(R.id.user_profile_name);
                    TextView emailt = (TextView) getView().findViewById(R.id.input_email);
                    TextView spec = (TextView) getView().findViewById(R.id.spec);
                    TextView price = (TextView) getView().findViewById(R.id.price);
                    TextView number = (TextView) getView().findViewById(R.id.phone);
                    ImageView imgvw = (ImageView) getView().findViewById(R.id.imageView);
                    TextView created = (TextView)  getView().findViewById(R.id.datejoined);
                    TextView lastlog = (TextView)  getView().findViewById(R.id.lastlogin);
                    imgvw.bringToFront();


                    if (u != null) {
                        profile.setText(u.getProperty("name").toString());
                        price.setText(price.getText()+u.getProperty("price").toString());
                        emailt.setText(u.getEmail().toString());
                        spec.setText( spec.getText()+u.getProperty("speciality").toString());
                         number.setText(number.getText()+u.getProperty("Tel").toString());
                        created.setText(created.getText()+u.getProperty("created").toString());
                     //   lastlog.setText(lastlog.getText()+u.getProperty("lastLogin").toString());





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
                            picasso.load(u.getProperty("pic").toString()).into(imgvw);
                        }
                        catch (IOException e){


                        }

                        //add click event on request

                        ImageView add ;
                        add=  (ImageView) getView().findViewById(R.id.add_teacher);
                       add.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                            /*     new MaterialDialog.Builder(getActivity())
                                        .title("Send Request?")
                                        .content("Once you send this teacher a request the teacher will be added to your teacher list")
                                        .positiveText("Agree")
                                        .negativeText("Not now")
                                        .onPositive(new MaterialDialog.SingleButtonCallback() {
                                            @Override
                                            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                          BackendlessUser current=      Backendless.UserService.CurrentUser();
                                                Request n = new Request();

                                                n.setReceiver(u);
                                                n.setSender(current);
                                            n.setReceiveremail(u.getEmail());
                                                n.setSenderemail(current.getEmail());
                                                final String s = current.getProperty("name").toString()+" Sent you a request";

                                                Backendless.Persistence.save( n, new AsyncCallback<Request>() {
                                                    public void handleResponse( Request response )
                                                    {
                                                        // new Contact instance
                                                        new SendNotification(u.getProperty("mtoken").toString(),Uri.encode(s)).execute();
                                                    }

                                                    public void handleFault( BackendlessFault fault )
                                                    {
                                                        // an error has occurred, the error code can be retrieved with fault.getCode()
                                                    }
                                                });


                                            }
                                        })

                                        .show();

   */
                                final int mYear, mMonth, mDay, mHour, mMinute;

                                final Calendar c = Calendar.getInstance();
                                mYear = c.get(Calendar.YEAR);
                                mMonth = c.get(Calendar.MONTH);
                                mDay = c.get(Calendar.DAY_OF_MONTH);
                                mHour = c.get(Calendar.HOUR_OF_DAY);
                                mMinute = c.get(Calendar.MINUTE);



                                DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(),
                                        new DatePickerDialog.OnDateSetListener() {

                                            @Override
                                            public void onDateSet(DatePicker view, int year,
                                                                  int monthOfYear, int dayOfMonth) {
                                              Log.e(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year,"dateeeee");
                                                date=year + "-" + (monthOfYear + 1) + "-" + dayOfMonth;


                                                //date set show hour
                                                TimePickerDialog timePickerDialog = new TimePickerDialog(getActivity(),
                                                        new TimePickerDialog.OnTimeSetListener() {

                                                            @Override
                                                            public void onTimeSet(TimePicker view, final int hourOfDay,
                                                                                  final int minute) {
                                                                SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss");
                                                                String formatted =    format.format((minute * 60 + hourOfDay * 60 * 60) * 1000);


                                                                Log.e(formatted,"timeeeeee");
                                                                BackendlessUser current=      Backendless.UserService.CurrentUser();
                                                                Request n = new Request();

                                                                n.setReceiver(u);
                                                                n.setRdate(date);

                                                                n.setRtime(String.valueOf((minute * 60 + hourOfDay * 60 * 60) * 1000));
                                                                n.setSender(current);
                                                                n.setReceiveremail(u.getEmail());
                                                                n.setSenderemail(current.getEmail());
                                                                final String s = current.getProperty("name").toString()+" Sent you a request";

                                                                Backendless.Persistence.save( n, new AsyncCallback<Request>() {
                                                                    public void handleResponse( Request response )
                                                                    {
                                                                        // new Contact instance
                                                                        new SendNotification(u.getProperty("mtoken").toString(),Uri.encode(s)).execute();


                                                                      final  Snackbar bar = Snackbar.make(getView(), "Request snet", Snackbar.LENGTH_LONG)
                                                                                .setAction("Dismiss", new View.OnClickListener() {
                                                                                    @Override
                                                                                    public void onClick(View v) {


                                                                                    }
                                                                                });

                                                                        bar.show();
                                                                        Random rn = new Random();
                                                                        int answer = rn.nextInt(500 - 1 + 1) + 1;

                                                                 AlarmManager alarmManager = (AlarmManager) getActivity().getSystemService(ALARM_SERVICE);
                                                                    Intent    alarmIntent = new Intent(getActivity(), AlarmReceiver.class);
                                                               PendingIntent  pendingIntent = PendingIntent.getBroadcast(  getActivity(), answer, alarmIntent, 0);

                                                                        SimpleDateFormat dateformat = new SimpleDateFormat("yyyy-MM-dd");
                                                                        Date d = new Date();
                                                                        try {
                                                                             d = dateformat.parse(date);
                                                                        } catch (ParseException e1) {
                                                                            Log.e("error","dateeeee ghalet");
                                                                        }

                                                                        alarmManager.set(AlarmManager.RTC_WAKEUP, d.getTime()+(minute * 60 + hourOfDay * 60 * 60) * 1000 , pendingIntent);

                                                                    }

                                                                    public void handleFault( BackendlessFault fault )
                                                                    {
                                                                       Log.e("erroooorr0",fault.getMessage()); // an error has occurred, the error code can be retrieved with fault.getCode()
                                                                    }
                                                                });

                                                            }
                                                        }, mHour, mMinute, false);
                                                timePickerDialog.show();

                                            }
                                        }, mYear, mMonth, mDay);
                                datePickerDialog.show();



                            }
                        });


                        ImageView chat ;
                        chat=  (ImageView) getView().findViewById(R.id.chat);
                        chat.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                getFragmentManager().beginTransaction().replace(R.id.content_main,new ChatFragment()).addToBackStack(null).commit();


                            }
                        });




                    }
                }


                // click event

            }

            @Override
            public void handleFault(BackendlessFault fault) {

            }

        });









        return v;

    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
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
