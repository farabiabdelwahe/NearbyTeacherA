package layout;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.Bundle;
import android.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.afollestad.materialdialogs.MaterialDialog;
import com.anupcowkur.reservoir.Reservoir;
import com.backendless.Backendless;
import com.backendless.BackendlessCollection;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;
import com.backendless.persistence.BackendlessDataQuery;
import com.example.gsc.template2.Back.Adapter.RequestTeacherAdapter;
import com.example.gsc.template2.Back.Adapter.SimpleItemTouchHelperCallback;
import com.example.gsc.template2.Back.Data.Request;
import com.example.gsc.template2.Back.Utils.FontChangeCrawler;
import com.example.gsc.template2.R;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the

 * to handle interaction events.
 * Use the {@link StudentRequestList#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TeacherRefused extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private Paint p = new Paint();

    ArrayList<Request> lusers;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;


    public TeacherRefused() {
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

        String whereClause = "receiveremail ='" + Backendless.UserService.CurrentUser().getEmail() + "' and approved=2";
        Log.e("whereeee", whereClause);

        Type resultType = new TypeToken<List<Request>>() {}.getType();
        try {
            lusers= Reservoir.get("Teacherrequestrefused", resultType);
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
                foundContacts.setPageSize(500000);

                Iterator<Request> iterator = foundContacts.getCurrentPage().iterator();
                while (iterator.hasNext()) {
                    final Request next = iterator.next();


                    lusers.add(next);
                    Log.e("whereeee", String.valueOf(lusers.size()));


                    //  Toast.makeText(getApplicationContext(), "Your  fdfdfddfd Location is - \nLat: " + ((GeoPoint)next.getProperty( "location" )) + "\nLong: " + next.getProperty("location"), Toast.LENGTH_LONG).show();


                }
                pDialog.dismiss();



                try {
                    Reservoir.put("Teacherrequestrefused", lusers);
                } catch (Exception e) {
                    //failure;
                    Log.e("reservoireee",e.getMessage());
                }
                pDialog.dismiss();


                final RecyclerView rv = (RecyclerView) view.findViewById(R.id.requestlist);

                RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
                rv.setLayoutManager(mLayoutManager);
                //  rv.setLayoutManager(llm);
                rv.setHasFixedSize(true);

                RequestTeacherAdapter adapter = new RequestTeacherAdapter(lusers, new RequestTeacherAdapter.OnItemClickListener() {


                    @Override
                    public void onItemClick(final Request item) {

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
                Log.e("efefefe", fault.getMessage());

                pDialog.dismiss();


                final RecyclerView rv = (RecyclerView) getView().findViewById(R.id.requestlist);

                RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
                rv.setLayoutManager(mLayoutManager);
                //  rv.setLayoutManager(llm);
                rv.setHasFixedSize(true);

                final RequestTeacherAdapter adapter = new RequestTeacherAdapter( lusers, new RequestTeacherAdapter.OnItemClickListener() {


                    @Override
                    public void onItemClick(final Request item) {

                    }

                    @Override
                    public void onItemLongclick(final Request item) {

                    }
                });




                rv.setAdapter(adapter);
                ItemTouchHelper.Callback callback =
                        new SimpleItemTouchHelperCallback(adapter);
                ItemTouchHelper touchHelper = new ItemTouchHelper(callback);
                touchHelper.attachToRecyclerView(rv);


                // click event
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
