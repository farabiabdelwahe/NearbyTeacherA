package layout;

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

import java.util.ArrayList;
import java.util.Iterator;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the

 * to handle interaction events.
 * Use the {@link Teacher#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Teacher extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    ArrayList<BackendlessUser> lusers= new ArrayList<BackendlessUser>() ;



    public Teacher() {
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

        View view = inflater.inflate(R.layout.fragment_teacher, container, false);
        String s = ((AppName) getActivity().getApplication()).getSpec();
        Double d =((AppName) getActivity().getApplication()).getPrice();
        float r  = ((AppName) getActivity().getApplication()).getRating();

        String whereClause = "ts = 't' AND speciality='"+s+"' AND price ="+d+" and (rating/nrating)>="+r;
        Log.e("whereeee",whereClause);
        BackendlessDataQuery dataQuery = new BackendlessDataQuery();
        dataQuery.setWhereClause( whereClause );

        Backendless.Persistence.of( BackendlessUser.class).find(dataQuery,  new AsyncCallback<BackendlessCollection<BackendlessUser>>(){
            @Override

            public void handleResponse( BackendlessCollection<BackendlessUser> foundContacts )

            {
                foundContacts.setPageSize(50000);

                Iterator<BackendlessUser> iterator=foundContacts.getCurrentPage().iterator();
                while( iterator.hasNext() )
                {
                    final BackendlessUser restaurant=iterator.next();





                   lusers.add(restaurant);





                    //  Toast.makeText(getApplicationContext(), "Your  fdfdfddfd Location is - \nLat: " + ((GeoPoint)restaurant.getProperty( "location" )) + "\nLong: " + restaurant.getProperty("location"), Toast.LENGTH_LONG).show();


                }


                RecyclerView rv=(RecyclerView) getView().findViewById(R.id.teacherlist);

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
                                        getFragmentManager().beginTransaction().replace(R.id.content_main,new profileTeacher()).addToBackStack(null).commit();






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

            }
        });







        FontChangeCrawler fontChanger = new FontChangeCrawler(getActivity().getAssets(), "fonts/myfont.ttf");
        fontChanger.replaceFonts((ViewGroup) view);

        // Inflate the layout for this fragment
        return view;
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


