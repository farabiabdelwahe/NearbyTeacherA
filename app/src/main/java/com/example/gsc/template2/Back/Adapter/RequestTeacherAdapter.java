package com.example.gsc.template2.Back.Adapter;


import android.content.Context;
import android.support.v4.view.MotionEventCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.backendless.BackendlessUser;
import com.example.gsc.template2.Back.Adapter.listener.OnCustomerListChangedListener;
import com.example.gsc.template2.Back.Adapter.listener.OnStartDragListener;
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
import java.util.Collections;

public class RequestTeacherAdapter extends RecyclerView.Adapter<RequestTeacherAdapter.requestViewHolder>  implements ItemTouchHelperAdapter {
    public Context context;




    @Override
    public void onItemMove(int fromPosition, int toPosition) {
        if (fromPosition < toPosition) {
            for (int i = fromPosition; i < toPosition; i++) {
                Collections.swap(persons, i, i + 1);
            }
        } else {
            for (int i = fromPosition; i > toPosition; i--) {
                Collections.swap(persons, i, i - 1);
            }
        }
        notifyItemMoved(fromPosition, toPosition);

    }





    @Override
    public void onItemDismiss(int position) {

    }




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

    private OnStartDragListener mDragStartListener;
    private OnCustomerListChangedListener mListChangedListener;

    OnItemClickListener listener;


    ArrayList<Request> persons;


    public RequestTeacherAdapter (ArrayList<Request> customers,
                               OnStartDragListener dragLlistener,
                               OnCustomerListChangedListener listChangedListener){
        persons = customers;
        mDragStartListener = dragLlistener;
        mListChangedListener = listChangedListener;
    }




    public  RequestTeacherAdapter( ArrayList<Request> persons, OnItemClickListener l){
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
    public void onBindViewHolder(final requestViewHolder personViewHolder, int i) {
        personViewHolder.bind(persons.get(i), listener);


        personViewHolder.datesent.setText(persons.get(i).getCreated().toString());
        personViewHolder.personName.setText(persons.get(i).getSender().getProperty("name").toString());

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
           . error(R.drawable.student)
                    .error(R.drawable.student)
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


