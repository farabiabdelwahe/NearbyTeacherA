package layout;

import android.app.AlertDialog;
import android.content.ContentUris;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.provider.CalendarContract;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.backendless.Backendless;
import com.backendless.BackendlessCollection;
import com.backendless.BackendlessUser;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;
import com.backendless.persistence.BackendlessDataQuery;
import com.example.gsc.template2.Back.Utils.FontChangeCrawler;
import com.example.gsc.template2.R;
import com.example.gsc.template2.UsersAdapter;
import com.squareup.okhttp.Cache;
import com.squareup.okhttp.Interceptor;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Response;
import com.squareup.picasso.OkHttpDownloader;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.Calendar;
import java.util.Iterator;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the

 * to handle interaction events.
 * Use the {@link profileTeacher#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MyteacherProfile extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;


    public MyteacherProfile() {
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
                inflater.inflate(R.layout.fragment_myprofile_teacher, container, false);


        TextView profile = (TextView) v.findViewById(R.id.user_profile_name);
        TextView emailt = (TextView) v.findViewById(R.id.input_email);
        TextView spec = (TextView) v.findViewById(R.id.spec);
        TextView price = (TextView) v.findViewById(R.id.price);
        TextView number = (TextView) v.findViewById(R.id.phone);
        TextView created = (TextView) v.findViewById(R.id.datejoined);
        TextView lastlog = (TextView) v.findViewById(R.id.lastlogin);
        ImageView imgvw = (ImageView) v.findViewById(R.id.imageView);
        imgvw.bringToFront();






        BackendlessUser u = Backendless.UserService.CurrentUser();

        if (u != null) {
            profile.setText(u.getProperty("name").toString());
            price.setText(price.getText()+u.getProperty("price").toString());
            emailt.setText(u.getEmail().toString());
            spec.setText( spec.getText()+u.getProperty("speciality").toString());
            number.setText(number.getText()+u.getProperty("Tel").toString());
            created.setText(created.getText()+u.getProperty("created").toString());
            //   lastlog.setText(lastlog.getText()+u.getProperty("lastLogin").toString());

            final RatingBar tratingBar = (RatingBar) v.findViewById(R.id.TeacherRating);

            Drawable progress = tratingBar.getProgressDrawable();
            DrawableCompat.setTint(progress, Color.WHITE);
            try {
                final float rating = Float.parseFloat(u.getProperty("rating").toString())/Float.parseFloat(u.getProperty("nrating").toString())  ;
                Log.e("fllloat", String.valueOf(rating));
                tratingBar.setRating(rating);

                if (tratingBar.getProgressDrawable() instanceof LayerDrawable) {
                    LayerDrawable stars = (LayerDrawable) tratingBar.getProgressDrawable();
                    DrawableCompat.setTint(stars.getDrawable(2), Color.parseColor("#DAA520"));
                }
                else {
                    // for Android 4.3, ratingBar.getProgressDrawable()=DrawableWrapperHoneycomb
                    DrawableCompat.setTint(tratingBar.getProgressDrawable(), Color.parseColor("#DAA520"));
                }
            }
            catch ( Exception e ) {
                tratingBar.setRating(2);

            }

 ImageView calender = (ImageView) v.findViewById(R.id.drop_down_option_menu);
            calender.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Uri.Builder builder = CalendarContract.CONTENT_URI.buildUpon();
                    builder.appendPath("time");
                    final int mYear, mMonth;

                    final Calendar c = Calendar.getInstance();
                    mYear = c.get(Calendar.YEAR);
                    mMonth = c.get(Calendar.MONTH);
                    ContentUris.appendId(builder, System.currentTimeMillis());
                    Intent intent = new Intent(Intent.ACTION_VIEW)
                            .setData(builder.build());
                    startActivity(intent);
                }
            });
try {
    String last = (String) u.getProperty("lastLogin").toString();
    lastlog.setText(lastlog.getText()+last);

} catch (Exception e ){
    lastlog.setText(lastlog.getText()+"never");

}


            OkHttpClient okHttpClient = new OkHttpClient();
            okHttpClient.networkInterceptors().add(new Interceptor() {
                @Override
                public Response intercept(Chain chain) throws IOException {
                    Response originalResponse = chain.proceed(chain.request());
                    return originalResponse.newBuilder().header("Cache-Control", "max-age=" + (60 * 60 * 24 * 365)).build();
                }
            });


            try {
                okHttpClient.setCache(new Cache(getActivity().getCacheDir(), Integer.MAX_VALUE));

                OkHttpDownloader okHttpDownloader = new OkHttpDownloader(okHttpClient);
                Picasso picasso = new Picasso.Builder(getActivity()).downloader(okHttpDownloader).build();
                picasso.load(u.getProperty("pic").toString()).into(imgvw);

                //  Toast.makeText(getApplicationContext(), "Your  fdfdfddfd Location is - \nLat: " + ((GeoPoint)restaurant.getProperty( "location" )) + "\nLong: " + restaurant.getProperty("location"), Toast.LENGTH_LONG).show();

            } catch (Exception e1) {
                e1.printStackTrace();
            }
        }



        // click event








        FontChangeCrawler fontChanger = new FontChangeCrawler(getActivity().getAssets(), "fonts/myfont.ttf");
        fontChanger.replaceFonts((ViewGroup) v);



        return v;

    }
}
