package layout;


import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.app.Fragment;
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
import com.backendless.Backendless;
import com.backendless.BackendlessCollection;
import com.backendless.BackendlessUser;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;
import com.backendless.persistence.BackendlessDataQuery;
import com.example.gsc.template2.Back.Adapter.ChatArrayAdapter;
import com.example.gsc.template2.Back.Async.SendNotification;
import com.example.gsc.template2.Back.Data.Message;
import com.example.gsc.template2.Back.Data.Request;
import com.example.gsc.template2.R;
import com.squareup.okhttp.Cache;
import com.squareup.okhttp.Interceptor;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Response;
import com.squareup.picasso.OkHttpDownloader;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

/**
 * A simple {@link Fragment} subclass.
 */
public class ChatFragment extends Fragment {

    private ChatArrayAdapter chatArrayAdapter;
    private ListView listView;
    private EditText chatText;
    private Button buttonSend;
    private ArrayList<Message> lusers ;


    public ChatFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final View v = inflater.inflate(R.layout.fragment_chat, container, false);
        lusers=new ArrayList<Message>();

        SharedPreferences prefs = getActivity().getSharedPreferences("prefs", getActivity().getApplicationContext().MODE_PRIVATE);
        final String e = prefs.getString("email", null);
        String whereClause = "email = '" + e + "'";
        BackendlessDataQuery dataQuery = new BackendlessDataQuery();
        dataQuery.setWhereClause(whereClause);



        Backendless.Persistence.of(BackendlessUser.class).find(dataQuery, new AsyncCallback<BackendlessCollection<BackendlessUser>>() {
            @Override
            public void handleResponse(BackendlessCollection<BackendlessUser> foundContacts)

            {

                Iterator<BackendlessUser> iterator = foundContacts.getCurrentPage().iterator();
                while (iterator.hasNext()) {
                    final BackendlessUser u = iterator.next();


                    if (u != null) {

                        String whereClause = "senderemail ='"+Backendless.UserService.CurrentUser().getEmail()+"' AND receiveremail='"+e+"'";
                        Log.e("whereeee",whereClause);
                        BackendlessDataQuery dataQuery = new BackendlessDataQuery();
                        dataQuery.setWhereClause( whereClause );


                        Backendless.Persistence.of( Message.class).find(dataQuery,  new AsyncCallback<BackendlessCollection<Message>>(){

                            @Override
                            public void handleResponse(BackendlessCollection<Message> response) {

                                Iterator<Message> iterator=response.getCurrentPage().iterator();
                                while( iterator.hasNext() )
                                {
                                    final Message message=iterator.next();





                                    lusers.add(message);
                                    Log.e("mmmmmmmmmm","mmmmmmmm");




                                    //  Toast.makeText(getApplicationContext(), "Your  fdfdfddfd Location is - \nLat: " + ((GeoPoint)restaurant.getProperty( "location" )) + "\nLong: " + restaurant.getProperty("location"), Toast.LENGTH_LONG).show();


                                }

                             ListView   listView = (ListView) v.findViewById(R.id.listView1);

                                chatArrayAdapter = new ChatArrayAdapter(getActivity().getApplicationContext(), R.layout.chat_singlemessage,lusers);
                                listView.setAdapter(chatArrayAdapter);
                             Button   buttonSend1 = (Button) v.findViewById(R.id.buttonSend);
                                buttonSend1.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {


                                        BackendlessUser current=      Backendless.UserService.CurrentUser();
                                        Message n = new Message();

                                        n.setReceiver(u);
                                        n.setSender(current);
                                        n.setReceiveremail(u.getEmail());
                                        n.setSenderemail(current.getEmail());
                                        EditText msg = (EditText) getView().findViewById(R.id.chatText);
                                        n.setMessage(msg.getText().toString());


                                        final String s = "  Message From: " +current.getProperty("name").toString();


                                        Backendless.Persistence.save( n, new AsyncCallback<Message>() {
                                            public void handleResponse( Message response )
                                            {
                                                // new Contact instance
                                                new SendNotification(u.getProperty("mtoken").toString(),Uri.encode(s)).execute();
                                                getFragmentManager().beginTransaction().replace(R.id.content_main,new ChatFragment()).commit();


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
                                Log.e("mmmmmmmmmm",fault.getMessage().toString());

                            }
                        });






                                }


                    //add cli


                }
            }


            @Override
            public void handleFault(BackendlessFault fault) {

            }

        });
        return v;
    }
}
