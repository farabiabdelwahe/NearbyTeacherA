package layout;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.support.design.widget.Snackbar;
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
import com.example.gsc.template2.Back.Adapter.Discussionadapter;
import com.example.gsc.template2.Back.Adapter.RequestTeacherAdapter;
import com.example.gsc.template2.Back.Data.Message;
import com.example.gsc.template2.Back.Data.Request;
import com.example.gsc.template2.Back.Utils.FontChangeCrawler;
import com.example.gsc.template2.Back.Utils.Utils;
import com.example.gsc.template2.R;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the

 * to handle interaction events.
 * Use the {@link FragmentDisucssions#newInstance} factory method to
 * create an instance of this fragment.
 *
 *
 */


public class FragmentDisucssions extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
   ArrayList<Message>   lusers ;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;



    public FragmentDisucssions() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FragmentDisucssions.
     */
    // TODO: Rename and change types and number of parameters
    public static FragmentDisucssions newInstance(String param1, String param2) {
        FragmentDisucssions fragment = new FragmentDisucssions();
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
      lusers = new ArrayList<Message>();

      final MaterialDialog pDialog = new MaterialDialog.Builder(getActivity())
              .title("Getting data")
              .content("it wont take long")
              .progress(true, 0)
              .progressIndeterminateStyle(true)
              .show();
      String appVersion = "v1";
      // Backendless.initApp( getActivity(), "BBA71CAF-54D7-F483-FFBB-7A380218D700", "7D635662-27AE-F3F2-FF61-84EC108A1C00", appVersion );
      final View view = inflater.inflate(R.layout.fragment_fragment_disucssions, container, false);
        try {
            String s = ((AppName) getActivity().getApplication()).getSpec();
      Double d = ((AppName) getActivity().getApplication()).getPrice();
      String whereClause = "receiveremail ='" + Backendless.UserService.CurrentUser().getEmail() + "' OR senderemail='" + Backendless.UserService.CurrentUser().getEmail() + "'";
      Log.e("whereeee", whereClause);


      Type resultType = new TypeToken<List<Message>>() {
      }.getType();
      try {
          lusers = Reservoir.get("disc", resultType);
          Log.e("reservoireee", String.valueOf(lusers.size()));
      } catch (Exception e) {
          Log.e("reservoireee", e.toString());

      }
      BackendlessDataQuery dataQuery = new BackendlessDataQuery();
      dataQuery.setWhereClause(whereClause);


      Backendless.Persistence.of(Message.class).find(dataQuery, new AsyncCallback<BackendlessCollection<Message>>() {
          @Override

          public void handleResponse(BackendlessCollection<Message> foundContacts)

          {

              lusers = new ArrayList<Message>();

              Iterator<Message> iterator = foundContacts.getCurrentPage().iterator();
              while (iterator.hasNext()) {
                  final Message restaurant = iterator.next();


                  if (Utils.exists(lusers, restaurant)) {

                      lusers.add(restaurant);

                  }


                  //  Toast.makeText(getApplicationContext(), "Your  fdfdfddfd Location is - \nLat: " + ((GeoPoint)restaurant.getProperty( "location" )) + "\nLong: " + restaurant.getProperty("location"), Toast.LENGTH_LONG).show();


              }

              pDialog.dismiss();
              try {
                  Reservoir.put("disc", lusers);
              } catch (Exception e) {
                  //failure;
                  Log.e("reservoireee", e.getMessage());
              }


              RecyclerView rv = (RecyclerView) view.findViewById(R.id.requestlist);

              RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
              rv.setLayoutManager(mLayoutManager);
              //  rv.setLayoutManager(llm);
              rv.setHasFixedSize(true);

              Discussionadapter adapter = new Discussionadapter(lusers, new Discussionadapter.OnItemClickListener() {


                  @Override
                  public void onItemClick(Message item) {
                      BackendlessUser cu = Backendless.UserService.CurrentUser();
                      String s;
                      if (item.getReceiveremail().equals(cu.getEmail())) {
                          s = item.getSenderemail();
                      } else {
                          s = item.getReceiveremail();
                      }
                      Log.e("ttttttt", s);

                      SharedPreferences.Editor editor = getActivity().getSharedPreferences("prefs", getActivity().MODE_PRIVATE).edit();
                      editor.putString("email", s);
                      editor.commit();


                      if (cu.getProperty("ts").equals("t")) {
                          getFragmentManager().beginTransaction().replace(R.id.content_teacher, new ChatFragment()).commit();

                      } else {
                          getFragmentManager().beginTransaction().replace(R.id.content_main, new ChatFragment()).commit();


                      }


                  }

                  @Override
                  public void onItemLongclick(Message item) {

                  }
              });


              rv.setAdapter(adapter);


              // click event

          }

          @Override
          public void handleFault(BackendlessFault fault) {
              Log.e("efefefe", fault.getMessage());

              pDialog.dismiss();


              RecyclerView rv = (RecyclerView) getView().findViewById(R.id.requestlist);

              RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
              rv.setLayoutManager(mLayoutManager);
              //  rv.setLayoutManager(llm);
              rv.setHasFixedSize(true);

              Discussionadapter adapter = new Discussionadapter(lusers, new Discussionadapter.OnItemClickListener() {


                  @Override
                  public void onItemClick(Message item) {
                      BackendlessUser cu = Backendless.UserService.CurrentUser();
                      String s;
                      if (item.getReceiveremail().equals(cu.getEmail())) {
                          s = item.getSenderemail();
                      } else {
                          s = item.getReceiveremail();
                      }
                      Log.e("ttttttt", s);

                      SharedPreferences.Editor editor = getActivity().getSharedPreferences("prefs", getActivity().MODE_PRIVATE).edit();
                      editor.putString("email", s);
                      editor.commit();


                      if (cu.getProperty("ts").equals("t")) {
                          getFragmentManager().beginTransaction().replace(R.id.content_teacher, new ChatFragment()).commit();

                      } else {
                          getFragmentManager().beginTransaction().replace(R.id.content_main, new ChatFragment()).commit();


                      }


                  }

                  @Override
                  public void onItemLongclick(Message item) {

                  }
              });


              rv.setAdapter(adapter);

          }
      });


      //     pDialog.dismiss();


      // Inflate the layout for this fragment
      return view;
  }


  catch (Exception e ){
      final Snackbar bar = Snackbar.make(getView(), "Server error", Snackbar.LENGTH_LONG)
              .setAction("Dismiss", new View.OnClickListener() {
                  @Override
                  public void onClick(View v) {


                  }
              });
      bar.show();


  }

        FontChangeCrawler fontChanger = new FontChangeCrawler(getActivity().getAssets(), "fonts/myfont.ttf");
        fontChanger.replaceFonts((ViewGroup) view);
 return view ;
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
