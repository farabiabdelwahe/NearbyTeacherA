package com.example.gsc.template2;

import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDex;
import android.support.multidex.MultiDexApplication;

import com.squareup.leakcanary.LeakCanary;
import com.squareup.leakcanary.RefWatcher;
import com.squareup.picasso.OkHttpDownloader;
import com.squareup.picasso.Picasso;

/**
 * Created by Mohamed Raid Raddaou on 23/10/2016.
 */

public class AppName extends MultiDexApplication {

    private Boolean myStateManager=false;
     private  String spec ;
    private   double price ;
    private float rating  ;

    private RefWatcher refWatcher;

    public static RefWatcher getRefWatcher(Context context) {
        AppName application = (AppName) context.getApplicationContext();
        return application.refWatcher;
    }

    public float getRating() {
        return rating;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getSpec() {
        return spec;
    }

    public void setSpec(String spec) {
        this.spec = spec;
    }

    public Boolean getMyStateManager() {
        return myStateManager;
    }

    public void setMyStateManager(Boolean myStateManager) {
        this.myStateManager = myStateManager;
    }


    protected void attachBaseContext(Context base) {

        super.attachBaseContext(base);

        MultiDex.install(this);

    }

    @Override
    public void onCreate() {
        super.onCreate();







        Picasso.Builder builder = new Picasso.Builder(this);
        builder.downloader(new OkHttpDownloader(this,Integer.MAX_VALUE));
        Picasso built = builder.build();
        built.setLoggingEnabled(true);
        Picasso.setSingletonInstance(built);

    }




}


