package org.horaapps.leafpic.imageEditor;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.widget.ImageView;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

public class ApplyFilter extends CommandEditor {

    private File outfile;
    private Bitmap bitmap;

    public ApplyFilter(ImageView show_img, File outfile, String path){

        super(path,outfile);

        this.outfile= outfile;
        BitmapDrawable d2 = (BitmapDrawable) show_img.getDrawable();
        bitmap = d2.getBitmap();
    }

    public void execute(){

        FileOutputStream outputStream= null;
        try {
            outputStream= new FileOutputStream(outfile);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        bitmap.compress(Bitmap.CompressFormat.JPEG,100,outputStream);
    }

}
