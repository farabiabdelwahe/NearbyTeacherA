package com.example.gsc.template2.Back.Utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;

import com.backendless.Backendless;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;
import com.backendless.files.BackendlessFile;
import com.example.gsc.template2.Back.Data.Message;
import com.example.gsc.template2.R;
import com.squareup.okhttp.*;
import com.squareup.picasso.OkHttpDownloader;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.atomic.AtomicReference;

/**
 * Created by GSC on 12/11/2016.
 */

public class Utils {

    public static String l  ;

    private static final String ALLOWED_CHARACTERS ="0123456789qwertyuiopasdfghjklzxcvbnm";

    public static String getRandomString(final int sizeOfRandomString)
    {
        final Random random=new Random();
        final StringBuilder sb=new StringBuilder(sizeOfRandomString);
        for(int i=0;i<sizeOfRandomString;++i)
            sb.append(ALLOWED_CHARACTERS.charAt(random.nextInt(ALLOWED_CHARACTERS.length())));
        return sb.toString();
    }

    public static Bitmap getFacebookProfilePicture(String userID) throws IOException {
        Bitmap bitmap=null ;
         try {

             URL imageURL = new URL("https://graph.facebook.com/" + userID + "/picture?type=large");
             bitmap = BitmapFactory.decodeStream(imageURL.openConnection().getInputStream());

         }
         catch(IOException E){


         }

        return bitmap;
    }


    public  static Bitmap getCroppedBitmap(Bitmap bitmap) {
        Bitmap output = Bitmap.createBitmap(bitmap.getWidth(),
                bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);

        final int color = 0xff424242;
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());

        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        // canvas.drawRoundRect(rectF, roundPx, roundPx, paint);
        canvas.drawCircle(bitmap.getWidth() / 2, bitmap.getHeight() / 2,
                bitmap.getWidth() / 2, paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);
        //Bitmap _bmp = Bitmap.createScaledBitmap(output, 60, 60, false);
        //return _bmp;
        return output;
    }

    public static Bitmap overlay(Bitmap bmp1, Bitmap bmp2) {
        Bitmap bmOverlay = Bitmap.createBitmap(bmp1.getWidth(), bmp1.getHeight(), bmp1.getConfig());
        Canvas canvas = new Canvas(bmOverlay);

        canvas.drawBitmap(bmp1, new Matrix(), null);
        canvas.drawBitmap(bmp2, new Matrix(), null);
        return bmOverlay;
    }



    public static Bitmap getMarkerBitmapFromView( Context context,Bitmap bmp ) {

        View customMarkerView = ((LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.fragment_blank, null);
        ImageView markerImageView = (ImageView) customMarkerView.findViewById(R.id.profile_image);
        markerImageView.setImageBitmap(bmp);
        customMarkerView.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
        customMarkerView.layout(0, 0, customMarkerView.getMeasuredWidth(), customMarkerView.getMeasuredHeight());
        customMarkerView.buildDrawingCache();
        Bitmap returnedBitmap = Bitmap.createBitmap(customMarkerView.getMeasuredWidth(), customMarkerView.getMeasuredHeight(),
                Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(returnedBitmap);
        canvas.drawColor(Color.WHITE, PorterDuff.Mode.SRC_IN);
        Drawable drawable = customMarkerView.getBackground();
        if (drawable != null)
            drawable.draw(canvas);
        customMarkerView.draw(canvas);
        return returnedBitmap;
    }



    public static Bitmap getScaledBitmapFromUrl(String imageUrl, int requiredWidth, int requiredHeight) throws IOException {
    URL url = new URL(imageUrl);
    BitmapFactory.Options options = new BitmapFactory.Options();
    options.inJustDecodeBounds = true;
    BitmapFactory.decodeStream(url.openConnection().getInputStream(),null, options);
    options.inSampleSize = calculateInSampleSize(options, requiredWidth, requiredHeight);
    options.inJustDecodeBounds = false;
    //don't use same inputstream object as in decodestream above. It will not work because
//decode stream edit input stream. So if you create
//InputStream is =url.openConnection().getInputStream(); and you use this in  decodeStream
//above and bellow it will not work!
    Bitmap bm = BitmapFactory.decodeStream(url.openConnection().getInputStream(),null, options);
    return bm;

}

    private static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
// Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {
            if (width > height) {
                inSampleSize = Math.round((float) height / (float) reqHeight);
            } else {
                inSampleSize = Math.round((float) width / (float) reqWidth);
            }
        }
        return inSampleSize;
    }


    public static Bitmap getResizedBitmap(Bitmap bm, int newWidth, int newHeight) {
        int width = bm.getWidth();
        int height = bm.getHeight();
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;
        // CREATE A MATRIX FOR THE MANIPULATION
        Matrix matrix = new Matrix();
        // RESIZE THE BIT MAP
        matrix.postScale(scaleWidth, scaleHeight);

        // "RECREATE" THE NEW BITMAP
        Bitmap resizedBitmap = Bitmap.createBitmap(
                bm, 0, 0, width, height, matrix, false);
        bm.recycle();
        return resizedBitmap;
    }


    //get Bitmap with picasso


 public static boolean exists(ArrayList<Message> l, Message m ){

     for(Message c :l){
         if (c.getSenderemail().equals(m.getSenderemail()) && c.getReceiveremail().equals(c.getReceiveremail())){
             return false ;
         }
         if (c.getReceiveremail().equals(m.getSenderemail()) && c.getSenderemail().equals(m.getReceiveremail())){
             return false ;
         }

     }

     return true;
 }


}



