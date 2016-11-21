package com.example.gsc.template2.Back.Async;

import android.os.AsyncTask;

import com.example.gsc.template2.Back.push.MyFirebaseInstanceIDService;

/**
 * Created by GSC on 21/11/2016.
 */

public class Savetoken extends AsyncTask<Void,Void,Void> {
    @Override
    protected Void doInBackground(Void... voids) {

        new MyFirebaseInstanceIDService().onTokenRefresh();
        return null;
    }
}
