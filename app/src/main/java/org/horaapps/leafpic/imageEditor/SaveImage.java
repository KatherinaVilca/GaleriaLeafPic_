package org.horaapps.leafpic.imageEditor;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;

import org.horaapps.leafpic.data.StorageHelper;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class SaveImage extends CommandEditor{

    protected String album_path;
    protected  String path;
    private final Context context;

    public SaveImage(Context context, String path, String album_path){

        super(null,null);
        this.context= context;
        this.album_path=album_path;
        this.path=path;
    }

    public void execute(){

        FileOutputStream outputStream= null;
        String filename= String.format("%d.jpg",System.currentTimeMillis());

        File file = Environment.getExternalStorageDirectory();
        File dir = new File(file.getPath() + "/Ediciones");
        File outfile= new File(dir,filename);

        try {
            outputStream= new FileOutputStream(outfile);
            BitmapFactory.decodeFile(path).compress(Bitmap.CompressFormat.JPEG,100,outputStream);
            StorageHelper.copyFile(context, outfile, dir);
            outputStream.flush();
            outputStream.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
