package layout;


import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.app.Fragment;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.anupcowkur.reservoir.Reservoir;
import com.anupcowkur.reservoir.ReservoirPutCallback;
import com.backendless.Backendless;
import com.backendless.BackendlessCollection;
import com.backendless.BackendlessUser;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;
import com.backendless.persistence.BackendlessDataQuery;
import com.backendless.persistence.QueryOptions;
import com.easyandroidanimations.library.BounceAnimation;
import com.example.gsc.template2.Back.Adapter.ChatArrayAdapter;
import com.example.gsc.template2.Back.Async.SendNotification;
import com.example.gsc.template2.Back.Data.Message;
import com.example.gsc.template2.Back.Data.Request;
import com.example.gsc.template2.MainActivity;
import com.example.gsc.template2.R;
import com.example.gsc.template2.TeacherActivity;
import com.google.gson.reflect.TypeToken;
import com.squareup.okhttp.Cache;
import com.squareup.okhttp.Interceptor;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Response;
import com.squareup.picasso.OkHttpDownloader;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import at.markushi.ui.CircleButton;
import cn.pedant.SweetAlert.SweetAlertDialog;

/**
 * A simple {@link Fragment} subclass.
 */
public class ChatFragment extends Fragment {

    private ChatArrayAdapter chatArrayAdapter;
    private ListView listView;
    private EditText chatText;
    private CircleButton buttonSend;
    private ArrayList<Message> lusers ;
    BackendlessUser chatuser;


    public ChatFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        BackendlessUser c = Backendless.UserService.CurrentUser();

        try {
            if (c.getProperty("ts").equals("t")) {


                FloatingActionButton floatingActionButton = ((TeacherActivity) getActivity()).fab;

                if (floatingActionButton != null) {
                    floatingActionButton.hide();
                }

            } else {
                FloatingActionButton floatingActionButton = ((MainActivity) getActivity()).fab;
                if (floatingActionButton != null) {
                    floatingActionButton.hide();
                }

            }
        }
        catch (Exception e ){

        }

        final View v = inflater.inflate(R.layout.fragment_chat, container, false);
        lusers=new ArrayList<Message>();

        SharedPreferences prefs = getActivity().getSharedPreferences("prefs", getActivity().MODE_PRIVATE);



        final String e = prefs.getString("email", null);
        String whereClause = "email = '" + e + "'";
        BackendlessDataQuery dataQuery = new BackendlessDataQuery();
        dataQuery.setWhereClause(whereClause);
        Log.e("xwhereeee2",whereClause);

        Type resultType = new TypeToken<List<Message>>() {}.getType();
        try {
            lusers= Reservoir.get("chat"+e, resultType);

        } catch (Exception ex) {


        }


        final SweetAlertDialog pDialog = new SweetAlertDialog(getActivity(), SweetAlertDialog.PROGRESS_TYPE);
        pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        pDialog.setTitleText("Loading");
        pDialog.setCancelable(false);
        pDialog.show();

        Backendless.Persistence.of(BackendlessUser.class).find(dataQuery, new AsyncCallback<BackendlessCollection<BackendlessUser>>() {
            @Override
            public void handleResponse(BackendlessCollection<BackendlessUser> foundContacts)

            {

                Iterator<BackendlessUser> iterator = foundContacts.getCurrentPage().iterator();
                while (iterator.hasNext()) {

                    final BackendlessUser u = iterator.next();


                    if (u != null) {

                        String whereClause = "(senderemail ='"+Backendless.UserService.CurrentUser().getEmail()+"' AND receiveremail='"+e+"') " +
                                "OR (senderemail='"+e+"' And receiveremail='"+Backendless.UserService.CurrentUser().getEmail()+"')";


                        QueryOptions queryOptions = new QueryOptions();
                        List<String> sortBy = new ArrayList<String>();

                        sortBy.add( "created " );
                        queryOptions.setSortBy( sortBy );
                        BackendlessDataQuery dataQuery = new BackendlessDataQuery();
                        dataQuery.setQueryOptions( queryOptions );
                   dataQuery.setPageSize(100);
                        dataQuery.setWhereClause( whereClause );

                        Reservoir.putAsync("chatuser", u, new ReservoirPutCallback() {
                            @Override
                            public void onSuccess() {
                                //success
                            }

                            @Override
                            public void onFailure(Exception e) {
                                //error
                            }
                        });



                        Backendless.Persistence.of( Message.class).find(dataQuery,  new AsyncCallback<BackendlessCollection<Message>>(){

                            @Override
                            public void handleResponse(BackendlessCollection<Message> response) {

                                lusers=new ArrayList<Message>();

                                Iterator<Message> iterator=response.getData().iterator();
                                while( iterator.hasNext() )
                                {

                                    final Message message=iterator.next();





                                    lusers.add(message);





                                    //  Toast.makeText(getApplicationContext(), "Your  fdfdfddfd Location is - \nLat: " + ((GeoPoint)restaurant.getProperty( "location" )) + "\nLong: " + restaurant.getProperty("location"), Toast.LENGTH_LONG).show();


                                }

                                Log.e("greservoireee", String.valueOf(response.getData().size()));


                                try {
                                    Reservoir.put("chat"+e, lusers);
                                } catch (Exception ex) {
                                    //failure;
                                    Log.e("reservoireee",ex.getMessage());
                                }

                             ListView   listView = (ListView) v.findViewById(R.id.listView1);




                                chatArrayAdapter = new ChatArrayAdapter(getActivity().getApplicationContext(), R.layout.chat_singlemessage,lusers);
                                listView.setAdapter(chatArrayAdapter);
                                CircleButton    buttonSend1 = (CircleButton ) v.findViewById(R.id.buttonSend);
                                buttonSend1.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {


                                        final BackendlessUser current=      Backendless.UserService.CurrentUser();
                                        final Message n = new Message();

                                        n.setReceiver(u);
                                        n.setSender(current);
                                        n.setReceiveremail(u.getEmail());
                                        n.setSenderemail(current.getEmail());
                                        final EditText msg = (EditText) getView().findViewById(R.id.chatText);
                                        n.setMessage(msg.getText().toString());


                                        final String s = "  Message From: " +current.getProperty("name").toString();


                                        final MaterialDialog progress = new MaterialDialog.Builder(getActivity())
                                                .title("Sending message")
                                                .content("it wont take long")
                                                .progress(true, 0)
                                                .progressIndeterminateStyle(true)
                                                .show();
// To dismiss the dialog

                                        Backendless.Persistence.save( n, new AsyncCallback<Message>() {


                                            public void handleResponse( Message response )
                                            {

                                                new BounceAnimation(v)
                                                        .setBounceDistance(50)

                                                        .setDuration(500)
                                                        .animate();
                                                // new Contact instance
                                                new SendNotification(u.getProperty("mtoken").toString(),Uri.encode(s)).execute();
                                                msg.setText("");

                                                chatArrayAdapter.add(n);
                                                progress.dismiss();
                                                final Snackbar bar = Snackbar.make(getView(), "Message Sent", Snackbar.LENGTH_LONG)
                                                        .setAction("Dismiss", new View.OnClickListener() {
                                                            @Override
                                                            public void onClick(View v) {


                                                            }
                                                        });
                                                bar.show();


                                            }

                                            public void handleFault( BackendlessFault fault )
                                            {

                                            }
                                        });
                                    }
                                });

                            }

                            @Override
                            public void handleFault(BackendlessFault fault) {
                                Log.e("kkkkkkkk",fault.getMessage());

                            }
                        });






                                }


                    //add cli


                }
                pDialog.dismiss();
            }


            @Override
            public void handleFault(BackendlessFault fault) {
 pDialog.dismiss();
                try {
                    chatuser =Reservoir.get("chatuser", BackendlessUser.class);
                    Log.e("mmmmmmmmmmss",chatuser.getEmail());
                } catch (Exception e) {

                }

                Log.e("mmmmmmmmmm",fault.getMessage().toString());

                pDialog.dismiss();

                ListView   listView = (ListView) v.findViewById(R.id.listView1);

                chatArrayAdapter = new ChatArrayAdapter(getActivity().getApplicationContext(), R.layout.chat_singlemessage,lusers);
                listView.setAdapter(chatArrayAdapter);
                CircleButton    buttonSend1 = (CircleButton ) v.findViewById(R.id.buttonSend);
                buttonSend1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {


                        final BackendlessUser current=      Backendless.UserService.CurrentUser();
                        final Message n = new Message();

                        n.setReceiver(chatuser);
                        n.setSender(current);
                        n.setReceiveremail(chatuser.getEmail());
                        n.setSenderemail(current.getEmail());
                        final EditText msg = (EditText) getView().findViewById(R.id.chatText);
                        n.setMessage(msg.getText().toString());


                        final String s = "  Message From: " +current.getProperty("name").toString();


                        final MaterialDialog progress = new MaterialDialog.Builder(getActivity())
                                .title("Sending message")
                                .content("it wont take long")
                                .progress(true, 0)
                                .progressIndeterminateStyle(true)
                                .show();
// To dismiss the dialog

                        Backendless.Persistence.save( n, new AsyncCallback<Message>() {


                            public void handleResponse( Message response )
                            {
                                // new Contact instance
                                new SendNotification(chatuser.getProperty("mtoken").toString(),Uri.encode(s)).execute();
                                msg.setText("");

                                chatArrayAdapter.add(n);
                                progress.dismiss();
                                final Snackbar bar = Snackbar.make(getView(), "Message Sent", Snackbar.LENGTH_LONG)
                                        .setAction("Dismiss", new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {


                                            }
                                        });
                                bar.show();


                            }

                            public void handleFault( BackendlessFault fault )
                            {


                                new SweetAlertDialog(getActivity(), SweetAlertDialog.ERROR_TYPE)
                                        .setTitleText("Oops...")
                                        .setContentText("Check your internet Connection")
                                        .show();;


                            }




                        });
                    }
                });






            }

        });
        return v;
    }
}
