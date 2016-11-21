package com.example.gsc.template2.Back.Async;

import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by GSC on 21/11/2016.
 */

public class SendNotification extends AsyncTask<Void,Void,Void> {

    String id ;
    String message ;
    public SendNotification(String id , String message){
        this.id=id;
        this.message=message;

    }
    @Override
    protected Void doInBackground(Void... voids) {
        URL url = null;
        try {
            url = new URL("http://raidraidraid.net23.net/push.php?id="+this.id+"&message="+this.message);
            Log.e("fffffffffffffffff",url.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        HttpURLConnection urlConnection = null;
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {

            InputStream in = new BufferedInputStream(urlConnection.getInputStream());
            byte[] contents = new byte[1024];

            int bytesRead = 0;
            String strFileContents="";
            while((bytesRead = in.read(contents)) != -1) {
                strFileContents += new String(contents, 0, bytesRead);
            }

            System.out.print(strFileContents);
            Log.v("ssssssssssss",strFileContents);

        } finally {
            urlConnection.disconnect();

            return null ;
        }
    }



}
