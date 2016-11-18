package com.example.gsc.template2;

import android.content.Context;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.backendless.BackendlessCollection;
import com.backendless.BackendlessUser;
import com.example.gsc.template2.Back.Users;
import com.squareup.okhttp.Cache;
import com.squareup.okhttp.Interceptor;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Response;
import com.squareup.picasso.OkHttpDownloader;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.List;

/**
 * Created by GSC on 07/11/2016.
 */
public class UsersAdapter extends ArrayAdapter<BackendlessUser> {

    public final static String TAG = "Article";
    private int resourceId = 0;
    private LayoutInflater inflater;
    Context c;

    public UsersAdapter(Context context, int resourceId, List<BackendlessUser> articles) {
        super(context, 0, articles);
        this.resourceId = resourceId;
        inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View view ;
       // ImageView imgLogo;
        TextView textName, textemail,textprice,textspec;
        ImageView profpic;

        view = inflater.inflate(resourceId, parent, false);


            Log.e("hello darknes","ffff") ;
            textName = (TextView)view.findViewById(R.id.input_name);
            textemail= (TextView)view.findViewById(R.id.input_email);
            textprice= (TextView)view.findViewById(R.id.input_price);
            textspec= (TextView)view.findViewById(R.id.input_speciality);

        profpic= (ImageView) view.findViewById(R.id.profpic);




        BackendlessUser item = getItem(position);


        textName.setText(item.getProperty("name").toString());
        textemail.setText(item.getProperty("email").toString());



        OkHttpClient okHttpClient = new OkHttpClient();
        okHttpClient.networkInterceptors().add(new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Response originalResponse = chain.proceed(chain.request());
                return originalResponse.newBuilder().header("Cache-Control", "max-age=" + (60 * 60 * 24 * 365)).build();
            }
        });


        try{


            okHttpClient.setCache(new Cache(getContext().getCacheDir(), Integer.MAX_VALUE));
            OkHttpDownloader okHttpDownloader = new OkHttpDownloader(okHttpClient);
            Picasso picasso = new Picasso.Builder(getContext()).downloader(okHttpDownloader).build();
            picasso.load(item.getProperty("pic").toString()).into(profpic);
        }
        catch (IOException e){


        }



        return view;
    }

}


