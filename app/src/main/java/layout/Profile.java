package layout;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.backendless.Backendless;
import com.backendless.BackendlessCollection;
import com.backendless.BackendlessUser;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;
import com.example.gsc.template2.AppName;
import com.example.gsc.template2.Back.Data.Request;
import com.example.gsc.template2.Back.Data.Student;
import com.example.gsc.template2.Back.Utils.FontChangeCrawler;
import com.example.gsc.template2.LoginActivity;
import com.example.gsc.template2.R;
import com.squareup.leakcanary.RefWatcher;
import com.squareup.okhttp.Cache;
import com.squareup.okhttp.Interceptor;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Response;
import com.squareup.picasso.OkHttpDownloader;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the

 * to handle interaction events.
 * Use the {@link Profile#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Profile extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;



    public Profile() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Profile.
     */
    // TODO: Rename and change types and number of parameters
    public static Profile newInstance(String param1, String param2) {
        Profile fragment = new Profile();
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
        View v =
                inflater.inflate(R.layout.fragment_profile, container, false);
        TextView profile = (TextView)  v.findViewById(R.id.user_profile_name);
        TextView  em = (TextView)  v.findViewById(R.id.user_profile_short_bio);
        TextView nam = (TextView)  v.findViewById(R.id.spec);
        TextView created = (TextView)  v.findViewById(R.id.datejoined);
        TextView lastlog = (TextView)  v.findViewById(R.id.lastlogin);
        List<String> relations = new ArrayList<>(); // create a list of relations

        relations.add("*"); //

        final BackendlessUser u = Backendless.UserService.CurrentUser();


        if (u != null) {
            String email = (String) u.getProperty("email");
            String name = (String) u.getProperty("name");
            String password = (String) u.getProperty("password");
            String Tel = (String) u.getProperty("Tel");
            String last = (String) u.getProperty("lastLogin").toString();
            String cr = (String) u.getProperty("created").toString();


            ImageView imgvw = (ImageView) v.findViewById(R.id.imageView);

imgvw.bringToFront();

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
                picasso.load(u.getProperty("pic").toString()).error(R.drawable.student).into(imgvw);
            }
            catch (Exception e){


            }


            profile.setText(name);
            nam.setText(nam.getText()+Tel);

            em.setText(email);
            lastlog.setText(lastlog.getText()+last);
            created.setText(created.getText()+cr);

        } else {
            Log.e("not connected", "");

        }
        FontChangeCrawler fontChanger = new FontChangeCrawler(getActivity().getAssets(), "fonts/myfont.ttf");
        fontChanger.replaceFonts((ViewGroup) v);
        return v;


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

    @Override public void onDestroy() {
        super.onDestroy();
        RefWatcher refWatcher = AppName.getRefWatcher(getActivity());
        refWatcher.watch(this);
    }

}
