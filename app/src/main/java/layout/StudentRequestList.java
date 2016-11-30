package layout;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.afollestad.materialdialogs.MaterialDialog;
import com.backendless.Backendless;
import com.backendless.BackendlessCollection;
import com.backendless.BackendlessUser;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;
import com.backendless.persistence.BackendlessDataQuery;
import com.example.gsc.template2.AppName;
import com.example.gsc.template2.Back.Adapter.RVAdapter;
import com.example.gsc.template2.Back.Adapter.Requestadapter;
import com.example.gsc.template2.Back.Data.Request;
import com.example.gsc.template2.MainActivity;
import com.example.gsc.template2.R;
import com.example.gsc.template2.Splash;

import java.util.ArrayList;
import java.util.Iterator;

import cn.pedant.SweetAlert.SweetAlertDialog;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link StudentRequestList.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link StudentRequestList#newInstance} factory method to
 * create an instance of this fragment.
 */
public class StudentRequestList extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    ArrayList<Request> lusers;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public StudentRequestList() {
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
        lusers=new ArrayList<Request>();
        String appVersion = "v1";
        // Backendless.initApp( getActivity(), "BBA71CAF-54D7-F483-FFBB-7A380218D700", "7D635662-27AE-F3F2-FF61-84EC108A1C00", appVersion );
        View view = inflater.inflate(R.layout.fragment_student_request_list, container, false);
        String s = ((AppName) getActivity().getApplication()).getSpec();
        Double d =((AppName) getActivity().getApplication()).getPrice();
        String whereClause = "senderemail ='"+Backendless.UserService.CurrentUser().getEmail()+"'";
        Log.e("whereeee",whereClause);
        BackendlessDataQuery dataQuery = new BackendlessDataQuery();
        dataQuery.setWhereClause( whereClause );

        final MaterialDialog pDialog = new MaterialDialog.Builder(getActivity())
                .title("Getting data")
                .content("it wont take long")
                .progress(true, 0)
                .progressIndeterminateStyle(true)
                .show();


        Backendless.Persistence.of( Request.class).find(dataQuery,  new AsyncCallback<BackendlessCollection<Request>>(){
            @Override

            public void handleResponse( BackendlessCollection<Request> foundContacts )

            {

                Iterator<Request> iterator=foundContacts.getCurrentPage().iterator();
                while( iterator.hasNext() )
                {
                    final Request restaurant=iterator.next();





                    lusers.add(restaurant);
                    Log.e("whereeee", String.valueOf(lusers.size()));




                    //  Toast.makeText(getApplicationContext(), "Your  fdfdfddfd Location is - \nLat: " + ((GeoPoint)restaurant.getProperty( "location" )) + "\nLong: " + restaurant.getProperty("location"), Toast.LENGTH_LONG).show();


                }


                RecyclerView rv=(RecyclerView) getView().findViewById(R.id.requestlist);

                RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
                rv.setLayoutManager(mLayoutManager);
                //  rv.setLayoutManager(llm);
                rv.setHasFixedSize(true);

             Requestadapter adapter = new   Requestadapter(lusers , new Requestadapter.OnItemClickListener() {


                 @Override
                 public void onItemClick(Request item) {

                 }

                 @Override
                 public void onItemLongclick(final Request item) {
              final SweetAlertDialog s =    new SweetAlertDialog(getActivity(), SweetAlertDialog.WARNING_TYPE);
                            s .setTitleText("Are you sure?")
                             .setContentText("do you want to delete this reques")
                             .setConfirmText("Yes,close it!")
                             .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                 @Override
                                 public void onClick(final SweetAlertDialog sDialog) {

                                     Backendless.Persistence.of( Request.class ).remove( item,
                                             new AsyncCallback<Long>()
                                             {
                                                 public void handleResponse( Long response )
                                                 {
                                                                                                 sDialog
                                                             .setTitleText("success!")
                                                             .setContentText("Reqest deleted!")
                                                             .setConfirmText("OK")
                                                             .setConfirmClickListener(null)
                                                             .changeAlertType(SweetAlertDialog.ERROR_TYPE);
                                                     getFragmentManager().beginTransaction().replace(R.id.content_main,new StudentRequestList()).addToBackStack(null).commit();


                                                 }
                                                 public void handleFault( BackendlessFault fault )
                                                 {
                                                     // dan error has occurred, the error code can be
                                                     // retrieved with fault.getCode()
                                                 }
                                             } );

                                 }
                             })
                             .show();
                 }
             });



                rv.setAdapter(adapter);


                // click event

            }
            @Override
            public void handleFault( BackendlessFault fault )
            {
                Log.e("efefefe",fault.getMessage());
            }
        });



//pDialog.dismiss();





        // Inflate the layout for this fragment
        return view;

        // Inflate the layout for this fragment

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
