package com.example.gsc.template2.Back.Adapter;


import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.backendless.BackendlessUser;
import com.example.gsc.template2.Back.Utils.RoundedTransformation;
import com.example.gsc.template2.R;
import com.squareup.okhttp.Cache;
import com.squareup.okhttp.Interceptor;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Response;
import com.squareup.picasso.OkHttpDownloader;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.ArrayList;

public class RVAdapter extends RecyclerView.Adapter<RVAdapter.PersonViewHolder> {
    public Context context;
    public interface OnItemClickListener {
        void onItemClick(BackendlessUser item);
    }

    public static class PersonViewHolder extends RecyclerView.ViewHolder {

        CardView cv;
        TextView personName;
        TextView personAge;
        ImageView personPhoto;


        PersonViewHolder(View itemView) {
            super(itemView);
            cv = (CardView)itemView.findViewById(R.id.cv);
            personName = (TextView)itemView.findViewById(R.id.name);
            personAge = (TextView)itemView.findViewById(R.id.email);
            personPhoto = (ImageView)itemView.findViewById(R.id.profpic);
        }

        public void bind(final BackendlessUser item, final OnItemClickListener listener) {

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override public void onClick(View v) {
                    listener.onItemClick(item);
                }
            });
        }
    }

    OnItemClickListener listener;


    ArrayList<BackendlessUser> persons;

    public  RVAdapter(ArrayList<BackendlessUser> persons , OnItemClickListener l){
        this.persons = persons;
        listener=l;
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    @Override
    public PersonViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.one_teacher, viewGroup, false);
        PersonViewHolder pvh = new PersonViewHolder(v);
        context=viewGroup.getContext();
        return pvh;
    }

    @Override
    public void onBindViewHolder(PersonViewHolder personViewHolder, int i) {
        personViewHolder.bind(persons.get(i), listener);

        personViewHolder.personName.setText(persons.get(i).getProperty("name").toString());
        personViewHolder.personAge.setText(persons.get(i).getEmail());


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
            picasso.load(persons.get(i).getProperty("pic").toString())
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
