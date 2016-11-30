package com.example.gsc.template2.Back.Adapter;


import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.backendless.Backendless;
import com.backendless.BackendlessUser;
import com.example.gsc.template2.Back.Data.Message;
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
import java.util.ArrayList;

public class Discussionadapter extends RecyclerView.Adapter<Discussionadapter .MessageViewHolder> {
    public Context context;
    public BackendlessUser current = Backendless.UserService.CurrentUser();
    public interface OnItemClickListener {
        void onItemClick(Message item);
        void onItemLongclick(Message item);

    }


    public static class MessageViewHolder extends RecyclerView.ViewHolder {

        CardView cv;
        TextView personName;
        TextView personAge;
        ImageView personPhoto;


       MessageViewHolder(View itemView) {
            super(itemView);
            cv = (CardView)itemView.findViewById(R.id.cv);
            personName = (TextView)itemView.findViewById(R.id.sender_name);
            personAge = (TextView)itemView.findViewById(R.id.datesent);
            personPhoto = (ImageView)itemView.findViewById(R.id.person_photo);
        }

        public void bind(final Message item, final OnItemClickListener listener) {

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


    ArrayList<Message> persons;

    public  Discussionadapter (ArrayList<Message> persons , OnItemClickListener l){
        this.persons = persons;
        listener=l;
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    @Override
    public MessageViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.onediscussion, viewGroup, false);
       MessageViewHolder pvh = new MessageViewHolder(v);
        context=viewGroup.getContext();
        return pvh;
    }

    @Override
    public void onBindViewHolder(MessageViewHolder personViewHolder, int i) {
        personViewHolder.bind(persons.get(i), listener);
if(persons.get(i).getReceiveremail().equals(current.getEmail())){
    personViewHolder.personName.setText(persons.
            get(i)
            .getSender()
            .getProperty("name").toString());
    personViewHolder.personAge.setText(persons.get(i).getCreated().toString());

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
        picasso.load(persons.get(i).getSender().getProperty("pic").toString())
                .transform(new RoundedTransformation(0, 0))
                .fit()
                .into(  personViewHolder.personPhoto);
    }
    catch (IOException e){


    }

}
        if(persons.get(i).getSenderemail().equals(current.getEmail())) {
            personViewHolder.personName.setText(persons.
                    get(i)
                    .getReceiver()
                    .getProperty("name").toString());
            personViewHolder.personAge.setText(persons.get(i).getCreated().toString());

            OkHttpClient okHttpClient = new OkHttpClient();
            okHttpClient.networkInterceptors().add(new Interceptor() {
                @Override
                public Response intercept(Chain chain) throws IOException {
                    Response originalResponse = chain.proceed(chain.request());
                    return originalResponse.newBuilder().header("Cache-Control", "max-age=" + (60 * 60 * 24 * 365)).build();
                }
            });


            try {


                okHttpClient.setCache(new Cache(context.getCacheDir(), Integer.MAX_VALUE));
                OkHttpDownloader okHttpDownloader = new OkHttpDownloader(okHttpClient);
                Picasso picasso = new Picasso.Builder(context).downloader(okHttpDownloader).build();
                picasso.load(persons.get(i).getReceiver().getProperty("pic").toString())
                        .transform(new RoundedTransformation(0, 0))
                        .fit()
                        .into(personViewHolder.personPhoto);
            } catch (IOException e) {


            }
        }

    }


    @Override
    public int getItemCount() {
        return persons.size();
    }
}
