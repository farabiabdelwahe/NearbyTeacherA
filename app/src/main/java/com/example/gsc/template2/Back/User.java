package com.example.gsc.template2.Back;

import android.provider.ContactsContract;
import android.util.Log;

import com.backendless.Backendless;
import com.backendless.BackendlessUser;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;
import com.backendless.geo.GeoPoint;

/**
 * Created by GSC on 04/11/2016.
 */

public class User  extends BackendlessUser{

  public  GeoPoint location;
  public  String email ;



    public static  void  changeLocation (final double i , final double g ){

        final AsyncCallback<BackendlessUser> saveUserCallback = new AsyncCallback<BackendlessUser>()
        {

            @Override
            public void handleResponse( BackendlessUser user )
            {
                // user object has been updated with geopoint
            }

            @Override
            public void handleFault( BackendlessFault fault )
            {

            }
        };


        Log.e("gggggg",String.valueOf(i));

        AsyncCallback<BackendlessUser> findUserCallback = new AsyncCallback<BackendlessUser>()
        {


            @Override
            public void handleResponse( BackendlessUser user )
            {
                GeoPoint geoPoint = new GeoPoint(i , g);
                user.setProperty( "location", geoPoint );
                Backendless.Data.of( BackendlessUser.class ).save( user, saveUserCallback );


            }

            @Override
            public void handleFault( BackendlessFault fault )
            {
                Log.e("gggggg",fault.getMessage());
            }
        };

        Backendless.Data.of( BackendlessUser.class ).findFirst( findUserCallback );


    }





    }

