package org.horaapps.leafpic.imageEditor;

import android.graphics.Bitmap;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class ApplyFilter extends CommandEditor {

    private File outfile;
    private Bitmap bitmap;

    public ApplyFilter(Bitmap bitmap, File outfile, String path) {

        super(path, outfile);
        this.outfile = outfile;
        this.bitmap = bitmap;

        if(bitmap == null){
            System.out.println("es nulo");
        }
 }

    public void execute() {

        FileOutputStream outputStream = null;
        try {
            outputStream = new FileOutputStream(outfile);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
            outputStream.flush();
            outputStream.close();
        } catch (FileNotFoundException e ) {
            e.printStackTrace();
        }
        catch (IOException e){

        }

    }
}
