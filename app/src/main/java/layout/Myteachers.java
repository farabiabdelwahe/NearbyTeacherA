package layout;

/**
 * Created by GSC on 30/12/2016.
 */

import android.app.AlertDialog;
import android.app.Fragment;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.afollestad.materialdialogs.MaterialDialog;
import com.anupcowkur.reservoir.Reservoir;
import com.backendless.Backendless;
import com.backendless.BackendlessCollection;
import com.backendless.BackendlessUser;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;
import com.backendless.persistence.BackendlessDataQuery;
import com.example.gsc.template2.AppName;
import com.example.gsc.template2.Back.Adapter.RVAdapter;
import com.example.gsc.template2.Back.Data.Request;
import com.example.gsc.template2.Back.Utils.FontChangeCrawler;
import com.example.gsc.template2.R;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Iterator;



import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.backendless.Backendless;
import com.backendless.BackendlessCollection;
import com.backendless.BackendlessUser;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;
import com.backendless.persistence.BackendlessDataQuery;
import com.example.gsc.template2.AppName;
import com.example.gsc.template2.Back.Adapter.RVAdapter;
import com.example.gsc.template2.Back.Utils.FontChangeCrawler;
import com.example.gsc.template2.R;
import com.example.gsc.template2.UsersAdapter;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the

 * to handle interaction events.
 * Use the {@link Teacher#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Myteachers extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    ArrayList<BackendlessUser> lusers= new ArrayList<BackendlessUser>() ;



    public Myteachers() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Teacher.
     */
    // TODO: Rename and change types and number of parameters
    public static Teacher newInstance(String param1, String param2) {
        Teacher fragment = new Teacher();
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
        boolean showMinMax = true;
        final MaterialDialog dialog = new MaterialDialog.Builder(getActivity())
                .title("Getting data")
                .content("it wont take long")
                .progress(true, 0)
                .progressIndeterminateStyle(true)
                .show();
        lusers=new ArrayList<BackendlessUser>();

        final View view = inflater.inflate(R.layout.fragment_teacher, container, false);

        String whereClause = "senderemail ='" + Backendless.UserService.CurrentUser().getEmail() + "' and  approved=1";

        Type resultType = new TypeToken<List<BackendlessUser>>() {}.getType();
        try {
            lusers= Reservoir.get("myteachers2", resultType);
            Log.e("reservoireeexxxxx", String.valueOf(lusers.size()));
        } catch (Exception e) {
            Log.e("reservoireee",e.toString());

        }
        Log.e("whereeee",whereClause);
        BackendlessDataQuery dataQuery = new BackendlessDataQuery();
        dataQuery.setPageSize(100);
        dataQuery.setWhereClause( whereClause );

        Backendless.Persistence.of( Request.class).find(dataQuery,  new AsyncCallback<BackendlessCollection<Request>>(){
            @Override

            public void handleResponse( BackendlessCollection<Request> foundContacts )

            {

                lusers=new ArrayList<BackendlessUser>();
                foundContacts.setPageSize(50000);

                Iterator<Request> iterator=foundContacts.getCurrentPage().iterator();
                while( iterator.hasNext() )
                {
                    final Request request=iterator.next();


 if ( ! lusers.contains(request.getReceiver())) {


     lusers.add(request.getReceiver());

 }





                    //  Toast.makeText(getApplicationContext(), "Your  fdfdfddfd Location is - \nLat: " + ((GeoPoint)restaurant.getProperty( "location" )) + "\nLong: " + restaurant.getProperty("location"), Toast.LENGTH_LONG).show();


                }

                try {
                    Reservoir.put("myteachers2", lusers);
                } catch (Exception e) {
                    //failure;
                    Log.e("reservoireee",e.getMessage());
                }



                RecyclerView rv=(RecyclerView) view.findViewById(R.id.teacherlist);

                RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
                rv.setLayoutManager(mLayoutManager);
                //  rv.setLayoutManager(llm);
                rv.setHasFixedSize(true);

                RVAdapter adapter = new RVAdapter(lusers , new RVAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(final BackendlessUser item) {






                        AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity());


                        SharedPreferences.Editor editor = getActivity().getSharedPreferences("prefs", getActivity().MODE_PRIVATE).edit();
                        editor.putString("email", item.getEmail());

                        editor.commit();
                        getFragmentManager().beginTransaction().replace(R.id.content_main,new Myteacher()).addToBackStack(null).commit();






                    }
                });



                rv.setAdapter(adapter);


                // click event
                dialog.dismiss();
            }
            @Override
            public void handleFault( BackendlessFault fault )
            {
                Log.e("efefefe",fault.getMessage());
                dialog.setContent("error");
                dialog.dismiss();




                RecyclerView rv=(RecyclerView) view.findViewById(R.id.teacherlist);

                RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
                rv.setLayoutManager(mLayoutManager);
                //  rv.setLayoutManager(llm);
                rv.setHasFixedSize(true);

                RVAdapter adapter = new RVAdapter(lusers , new RVAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(final BackendlessUser item) {






                        AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity());


                        SharedPreferences.Editor editor = getActivity().getSharedPreferences("prefs", getActivity().MODE_PRIVATE).edit();
                        editor.putString("email", item.getEmail());

                        editor.commit();
                        getFragmentManager().beginTransaction().replace(R.id.content_main,new Myteacher()).addToBackStack(null).commit();






                    }
                });



                rv.setAdapter(adapter);


                // click event
                dialog.dismiss();

            }
        });







        FontChangeCrawler fontChanger = new FontChangeCrawler(getActivity().getAssets(), "fonts/myfont.ttf");
        fontChanger.replaceFonts((ViewGroup) view);

        // Inflate the layout for this fragment


        return view;
    }

    @Override
    public void onPause()
    {
        super.onPause();

        unbindDrawables(getActivity().findViewById(R.id.content_main));
        System.gc();
    }


    @Override
    public  void onDestroyView(){
        super.onPause();

        unbindDrawables(getActivity().findViewById(R.id.content_main));
        System.gc();

    }


    @Override
    public void onDestroy()
    {
        super.onDestroy();

        unbindDrawables(getActivity().findViewById(R.id.content_main));
        System.gc();
    }

    private void unbindDrawables(View view)
    {
        if (view.getBackground() != null)
        {
            view.getBackground().setCallback(null);
        }
        if (view instanceof ViewGroup && !(view instanceof AdapterView))
        {
            for (int i = 0; i < ((ViewGroup) view).getChildCount(); i++)
            {
                unbindDrawables(((ViewGroup) view).getChildAt(i));
            }
            ((ViewGroup) view).removeAllViews();
        }
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {


    }
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


