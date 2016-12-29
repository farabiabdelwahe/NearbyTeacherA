package com.example.gsc.template2.Back.Adapter;

/**
 * Created by GSC on 22/11/2016.
 */


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.backendless.Backendless;
import com.backendless.BackendlessUser;
import com.example.gsc.template2.Back.Data.Comment;
import com.example.gsc.template2.Back.Data.Message;
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
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class Feedbackadapter extends ArrayAdapter<Comment> {

    private TextView Text;
    private TextView owner;
    private CircleImageView photo  ;
    private List<Comment> Comments = new ArrayList();
    Context c ;

    //constructor

    public Feedbackadapter( Context c , int resourceid , ArrayList<Comment> list ){
        super(c,0,list);
        this.Comments=list;
        this.c = c ;
    }

    public void add(Comment object) {
        Comments.add(object);
        super.add(object);
    }

    public Feedbackadapter(Context context, int textViewResourceId) {
        super(context, textViewResourceId);
    }

    public int getCount() {
        return this.Comments.size();
    }

    public Comment getItem(int index) {
        return this.Comments.get(index);
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        if (row == null) {
            LayoutInflater inflater = (LayoutInflater) this.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = inflater.inflate(R.layout.onefeedback, parent, false);

        }

        Comment comment = getItem(position);
        Typeface blockFonts = Typeface.createFromAsset(c.getAssets(),"fonts/myfont.ttf");
       Text.setTypeface(blockFonts);
      owner.setTypeface(blockFonts);

        owner= (TextView) row.findViewById(R.id.owner);
        Text = (TextView) row.findViewById(R.id.text);
        photo = (CircleImageView) row.findViewById(R.id.photo);

Text.setText(comment.getMessage());
        owner.setText(comment.getSender().getProperty("name").toString());

        OkHttpClient okHttpClient = new OkHttpClient();
        okHttpClient.networkInterceptors().add(new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Response originalResponse = chain.proceed(chain.request());
                return originalResponse.newBuilder().header("Cache-Control", "max-age=" + (60 * 60 * 24 * 365)).build();
            }
        });


        try{



            Picasso.with(c).load(comment.getSender().getProperty("pic").toString())



                    .into( photo);

        }
        catch (Exception e){


        }






        return row;
    }



    public Bitmap decodeToBitmap(byte[] decodedByte) {
        return BitmapFactory.decodeByteArray(decodedByte, 0, decodedByte.length);
    }

}