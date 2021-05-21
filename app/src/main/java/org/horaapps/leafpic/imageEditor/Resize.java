package org.horaapps.leafpic.imageEditor;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class Resize {

    public Resize(){}

    public Bitmap decodeSampledBitmapFromResource(String r, int reqWidth, int reqHeight) {

        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(r, options);

        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

        options.inJustDecodeBounds = false;

        return BitmapFactory.decodeFile(r, options) ;
    }

    static int calculateInSampleSize( BitmapFactory.Options options, int reqWidth, int reqHeight) {

        int inSampleSize = 1;
        final int height = options.outHeight;
        final int width = options.outWidth ;

        if (height > reqHeight || width > reqWidth) {

            final int halfHeight = height/ 2;
            final int halfWidth = height/ 2;

            while ((halfHeight / inSampleSize) > reqHeight && (halfWidth / inSampleSize) > reqWidth) {

                inSampleSize *= 2;
            }
        }
        return inSampleSize;
    }
}

