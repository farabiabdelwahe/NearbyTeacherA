package com.example.gsc.template2.Back.Adapter;

/**
 * Created by GSC on 22/11/2016.
 */


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.backendless.Backendless;
import com.backendless.BackendlessUser;
import com.example.gsc.template2.Back.Data.Message;
import com.example.gsc.template2.R;

import java.util.ArrayList;
import java.util.List;

public class ChatArrayAdapter extends ArrayAdapter<Message> {

    private TextView chatText;
    private TextView info;
    private List<Message> chatMessageList = new ArrayList();
    private LinearLayout singleMessageContainer;
    //constructor

public ChatArrayAdapter( Context c , int resourceid , ArrayList<Message> list ){
    super(c,0,list);
    this.chatMessageList=list;
}

    public void add(Message object) {
        chatMessageList.add(object);
        super.add(object);
    }

    public ChatArrayAdapter(Context context, int textViewResourceId) {
        super(context, textViewResourceId);
    }

    public int getCount() {
        return this.chatMessageList.size();
    }

    public Message getItem(int index) {
        return this.chatMessageList.get(index);
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        if (row == null) {
            LayoutInflater inflater = (LayoutInflater) this.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = inflater.inflate(R.layout.chat_singlemessage, parent, false);
        }
        singleMessageContainer = (LinearLayout) row.findViewById(R.id.singleMessageContainer);
        Message chatMessageObj = getItem(position);
        chatText = (TextView) row.findViewById(R.id.singleMessage);


        chatText.setText(chatMessageObj.getMessage());
        BackendlessUser u = Backendless.UserService.CurrentUser();
        if (u.getEmail().equals(chatMessageObj.getSenderemail())){
             chatText.setBackgroundResource( R.drawable.in_message_bg );
             singleMessageContainer.setGravity( Gravity.RIGHT);





        }
        else {
            chatText.setBackgroundResource( R.drawable.out_message_bg );
            singleMessageContainer.setGravity( Gravity.LEFT);

        }



            return row;
    }



    public Bitmap decodeToBitmap(byte[] decodedByte) {
        return BitmapFactory.decodeByteArray(decodedByte, 0, decodedByte.length);
    }

}