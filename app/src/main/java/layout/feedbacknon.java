package layout;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.backendless.Backendless;
import com.backendless.BackendlessCollection;
import com.backendless.BackendlessUser;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;
import com.backendless.persistence.BackendlessDataQuery;
import com.backendless.persistence.QueryOptions;
import com.example.gsc.template2.Back.Adapter.ChatArrayAdapter;
import com.example.gsc.template2.Back.Adapter.Feedbackadapter;
import com.example.gsc.template2.Back.Async.SendNotification;
import com.example.gsc.template2.Back.Data.Comment;
import com.example.gsc.template2.Back.Data.Message;
import com.example.gsc.template2.MainActivity;
import com.example.gsc.template2.R;
import com.example.gsc.template2.TeacherActivity;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the

 * to handle interaction events.
 * Use the {@link Feedback#newInstance} factory method to
 * create an instance of this fragment.
 */
public class feedbacknon extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    ArrayList<Comment> lusers ;
    Feedbackadapter cmtadapter ;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;


    public feedbacknon() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Feedback.
     */
    // TODO: Rename and change types and number of parameters
    public static Feedback newInstance(String param1, String param2) {
        Feedback fragment = new Feedback();
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


        // Inflate the layout for this fragment

        BackendlessUser c = Backendless.UserService.CurrentUser();
        if(c.getProperty("ts").equals("t")){


            FloatingActionButton floatingActionButton = ((TeacherActivity) getActivity()).fab;
            if (floatingActionButton != null) {
                floatingActionButton.hide();
            }

        }
        else{
            FloatingActionButton floatingActionButton = ((MainActivity) getActivity()).fab;
            if (floatingActionButton != null) {
                floatingActionButton.hide();
            }

        }

        final View v = inflater.inflate(R.layout.fragment_feedback, container, false);
        lusers=new ArrayList<Comment>();

        SharedPreferences prefs = getActivity().getSharedPreferences("prefs", getActivity().MODE_PRIVATE);

        final String e = prefs.getString("email", null);



        final SweetAlertDialog pDialog = new SweetAlertDialog(getActivity(), SweetAlertDialog.PROGRESS_TYPE);
        pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        pDialog.setTitleText("Loading");
        pDialog.setCancelable(false);
        pDialog.show();



        String whereClause = "receiveremail=  '"+e+"'" ;


        QueryOptions queryOptions = new QueryOptions();
        List<String> sortBy = new ArrayList<String>();

        sortBy.add( "created " );
        queryOptions.setSortBy( sortBy );
        BackendlessDataQuery dataQuery = new BackendlessDataQuery();
        dataQuery.setQueryOptions( queryOptions );
        dataQuery.setWhereClause( whereClause );



        Backendless.Persistence.of( Comment.class).find(dataQuery,  new AsyncCallback<BackendlessCollection<Comment>>(){

            @Override
            public void handleResponse(BackendlessCollection<Comment> response) {



                Iterator<Comment> iterator=response.getCurrentPage().iterator();
                while( iterator.hasNext() )
                {
                    final Comment  comment=iterator.next();





                    lusers.add(comment);





                    //  Toast.makeText(getApplicationContext(), "Your  fdfdfddfd Location is - \nLat: " + ((GeoPoint)restaurant.getProperty( "location" )) + "\nLong: " + restaurant.getProperty("location"), Toast.LENGTH_LONG).show();


                }
                pDialog.dismiss();
                ListView listView = (ListView) v.findViewById(R.id.list_view);

                cmtadapter = new Feedbackadapter(getActivity().getApplicationContext(), R.layout.onefeedback,lusers);
                listView.setAdapter(cmtadapter);

                Button buttonSend1 = (Button) v.findViewById(R.id.buttonSend);
                pDialog.dismiss();
                buttonSend1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        new SweetAlertDialog(getActivity(), SweetAlertDialog.ERROR_TYPE)
                                .setTitleText("Oops...")
                                .setContentText("You can only rate your teachers")
                                .show();
                    }
                });




            }











            //add cli






            @Override
            public void handleFault(BackendlessFault fault) {
                Log.e("errror", fault.getMessage());
            }

        });
        return v;
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

}
