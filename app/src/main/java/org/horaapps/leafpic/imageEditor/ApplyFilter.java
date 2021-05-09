package org.horaapps.leafpic.imageEditor;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.widget.ImageView;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

public class ApplyFilter extends CommandEditor {

    private File outfile;
    private ImageView img;

    public ApplyFilter(ImageView show_img, File outfile, String path){

        super(path,outfile);
        System.out.println("El path que estoy guardando: "+path);
        this.outfile= outfile;
        this.img= show_img;
    }

    public void execute(){

        BitmapDrawable d2 = (BitmapDrawable) img.getDrawable();
        Bitmap bitmap = d2.getBitmap();

        FileOutputStream outputStream= null;
        try {
            outputStream= new FileOutputStream(outfile);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        bitmap.compress(Bitmap.CompressFormat.JPEG,100,outputStream);
    }

}
