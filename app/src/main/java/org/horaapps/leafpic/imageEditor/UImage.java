package org.horaapps.leafpic.imageEditor;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.support.annotation.Nullable;

import java.io.IOException;

public class UImage {

    public static Bitmap decodeBitmap(String imagePath, @Nullable  BitmapFactory.Options op) {

        int orientacion = 0;
        Bitmap bitmapImagen = null;

        try {
            ExifInterface exif = new ExifInterface(imagePath);
            orientacion = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION,ExifInterface.ORIENTATION_NORMAL);

        } catch (IOException e) {
            e.printStackTrace();
        }

        if( op == null) {
            bitmapImagen = BitmapFactory.decodeFile(imagePath);
        }
        else bitmapImagen = BitmapFactory.decodeFile(imagePath,op);

        Matrix matrix = new Matrix();
        matrix.postRotate(rotacion(orientacion));
        return Bitmap.createBitmap(bitmapImagen, 0, 0, bitmapImagen.getWidth(), bitmapImagen.getHeight(), matrix, true);
    }


    private static int rotacion (int val){

        switch (val){
            case (ExifInterface.ORIENTATION_ROTATE_90) : return 90;
            case (ExifInterface.ORIENTATION_ROTATE_180): return 180;
            case (ExifInterface.ORIENTATION_ROTATE_270): return 270;
            default :  return 0;
        }
    }
}
