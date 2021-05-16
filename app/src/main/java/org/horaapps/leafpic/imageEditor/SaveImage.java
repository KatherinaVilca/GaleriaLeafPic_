package org.horaapps.leafpic.imageEditor;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import org.horaapps.leafpic.data.StorageHelper;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

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
        File outfile= new File(album_path,filename);

        try {
            outputStream= new FileOutputStream(outfile);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        BitmapFactory.decodeFile(path).compress(Bitmap.CompressFormat.JPEG,100,outputStream);
        StorageHelper.copyFile(context, outfile, new File(album_path));
    }
}
