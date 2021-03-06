package com.example.gsc.template2.Back.Adapter;


import android.content.Context;
import android.graphics.Typeface;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.backendless.BackendlessUser;
import com.example.gsc.template2.Back.Data.Request;
import com.example.gsc.template2.Back.Utils.RoundedTransformation;
import com.example.gsc.template2.R;
import com.squareup.okhttp.Cache;
import com.squareup.okhttp.Interceptor;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Response;
import com.squareup.picasso.OkHttpDownloader;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class Requestadapter extends RecyclerView.Adapter<Requestadapter.requestViewHolder> {
    public Context context;
    public interface OnItemClickListener {
        void onItemClick(Request item);
        void onItemLongclick(Request item);

    }


    public static class requestViewHolder extends RecyclerView.ViewHolder {

        CardView cv;
        TextView personName;
        TextView datesent;
        ImageView personPhoto;




        requestViewHolder(View itemView) {
            super(itemView);
            cv = (CardView)itemView.findViewById(R.id.cv);
            personName = (TextView)itemView.findViewById(R.id.sender_name);
            datesent = (TextView)itemView.findViewById(R.id.datesent);

            personPhoto = (ImageView)itemView.findViewById(R.id.person_photo);


        }

        public void bind(final Request item, final OnItemClickListener listener) {

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override public void onClick(View v) {
                    listener.onItemClick(item);
                }
            });
            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    listener.onItemLongclick(item);
                    return true;
                }


            });


        }
    }

    OnItemClickListener listener;


    ArrayList<Request> persons;

    public  Requestadapter(ArrayList<Request> persons , OnItemClickListener l){
        this.persons = persons;
        listener=l;
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    @Override
    public requestViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.requestoneitem, viewGroup, false);
        requestViewHolder pvh = new requestViewHolder(v);
        context=viewGroup.getContext();
        return pvh;
    }

    @Override
    public void onBindViewHolder(requestViewHolder personViewHolder, int i) {
        personViewHolder.bind(persons.get(i), listener);

        personViewHolder.personName.setText(persons.get(i).getReceiver().getProperty("name").toString());
        personViewHolder.datesent.setText(persons.get(i).getCreated().toString());
        SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss");
        Typeface blockFonts = Typeface.createFromAsset(context.getAssets(),"fonts/myfont.ttf");
        personViewHolder.datesent.setTypeface(blockFonts);
        personViewHolder.personName.setTypeface(blockFonts);

        OkHttpClient okHttpClient = new OkHttpClient();
        okHttpClient.networkInterceptors().add(new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Response originalResponse = chain.proceed(chain.request());
                return originalResponse.newBuilder().header("Cache-Control", "max-age=" + (60 * 60 * 24 * 365)).build();
            }
        });


        try{


            okHttpClient.setCache(new Cache(context.getCacheDir(), Integer.MAX_VALUE));
            OkHttpDownloader okHttpDownloader = new OkHttpDownloader(okHttpClient);
            Picasso picasso = new Picasso.Builder(context).downloader(okHttpDownloader).build();
            picasso.load(persons.get(i).getReceiver().getProperty("pic").toString())
           . error(R.drawable.teacher)
                    .into(  personViewHolder.personPhoto);
        }
        catch (Exception e){


        }
    }


    @Override
    public int getItemCount() {
        return persons.size();
    }
}
